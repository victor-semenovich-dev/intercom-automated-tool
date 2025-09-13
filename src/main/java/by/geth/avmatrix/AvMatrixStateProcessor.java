package by.geth.avmatrix;

import by.geth.server.IntercomServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AvMatrixStateProcessor {

    private IntercomServer server;
    private Config config;
    private String cookies;

    public static void start(IntercomServer server, String configFile) {
        AvMatrixStateProcessor processor = new AvMatrixStateProcessor(server, configFile);
        processor.startProcessing();
    }

    public AvMatrixStateProcessor(IntercomServer server, String configFile) {
        this.server = server;
        try {
            JsonObject configJson = (JsonObject) JsonParser.parseReader(new FileReader(configFile));
            config = Config.fromJson(configJson);
            System.out.println(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startProcessing() {
        new Thread(() -> {
            cookies = authenticate();
            while (true) {
                try {
                    AvMatrixState state = getState();
                    server.applyAvMatrixState(state);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        Thread.sleep(config.delayMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String authenticate() {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        HttpClient client = HttpClient
                .newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .build();

        try {
            String url = String.format("http://%s/cgi-bin/entry.cgi/system/login", config.ip);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString("{\"sUserName\": \"admin\", \"sPassword\": \"YWRtaW4=\"}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie: cookies) {
                if (cookie.getName().equals("token")) {
                    return String.format("%s=%s", cookie.getName(), cookie.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private AvMatrixState getState() {
        try {
            long start = System.currentTimeMillis();

            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("http://%s/cgi-bin/entry.cgi/event/contrl-menu-basic", config.ip);
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Cookie", cookies)
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseBody);
            int pgm = Integer.parseInt(jsonObject.get("PGM").getAsString()) - 1; // 0-based
            int pvw = Integer.parseInt(jsonObject.get("PVW").getAsString()) - 1; // 0-based

            long finish = System.currentTimeMillis();
            long duration = finish - start;

            AvMatrixState state = new AvMatrixState(pgm, pvw);
            System.out.println("Got new state " + state + " in " + duration + "ms");
            return state;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class Config {
        private String ip;
        private int delayMs;

        public static Config fromJson(JsonObject json) {
            String ip = json.get("ip").getAsString();
            int delayMs = json.get("delayMs").getAsInt();
            return new Config(ip, delayMs);
        }

        public Config(String ip, int delayMs) {
            this.ip = ip;
            this.delayMs = delayMs;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "ip='" + ip + '\'' +
                    ", delayMs=" + delayMs +
                    '}';
        }
    }
}
