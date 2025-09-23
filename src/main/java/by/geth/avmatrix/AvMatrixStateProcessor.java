package by.geth.avmatrix;

import by.geth.server.IntercomServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class AvMatrixStateProcessor {

    private final IntercomServer server;
    private Config config;
    private String cookies = "";

    public static void start(IntercomServer server, String configFile) {
        AvMatrixStateProcessor processor = new AvMatrixStateProcessor(server, configFile);
        processor.authenticateWithRetryPolicy();
        processor.startProcessing();
    }

    public AvMatrixStateProcessor(IntercomServer server, String configFile) {
        this.server = server;
        try {
            JsonObject configJson = (JsonObject) JsonParser.parseReader(new FileReader(configFile));
            config = Config.fromJson(configJson);
            System.out.println(config);
        } catch (Exception ignored) {
            System.out.println("Failed to parse the config file!");
            System.exit(1);
        }
    }

    public void startProcessing() {
        new Thread(() -> {
            while (true) {
                try {
                    AvMatrixState state = getState();
                    if (state != null) {
                        server.applyAvMatrixState(state);
                    }
                } catch (AuthenticationException e) {
                    System.out.println("Authentication error!");
                    authenticateWithRetryPolicy();
                } finally {
                    try {
                        Thread.sleep(config.delayMs);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();
    }

    private void authenticateWithRetryPolicy() {
        while (!authenticate()) {
            System.out.println("Failed to authenticate! Waiting for 3s...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private boolean authenticate() {
        System.out.println("Authentication...");

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        HttpClient client = HttpClient
                .newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .build();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.authUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString("{\"sUserName\": \"admin\", \"sPassword\": \"YWRtaW4=\"}"))
                    .timeout(Duration.ofSeconds(5))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie: cookies) {
                if (cookie.getName().equals("token")) {
                    this.cookies = String.format("%s=%s", cookie.getName(), cookie.getValue());
                    System.out.println("Successful authentication!");
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private AvMatrixState getState() throws AuthenticationException {
        try {
            long start = System.currentTimeMillis();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Cookie", cookies)
                    .uri(URI.create(config.stateUrl))
                    .timeout(Duration.ofSeconds(5))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(response.body());
            if (jsonObject.has("error")) {
                int code = jsonObject.get("error").getAsJsonObject().get("code").getAsInt();
                if (code == 401) {
                    throw new AuthenticationException();
                }
            }
            int pgm = Integer.parseInt(jsonObject.get("PGM").getAsString()) - 1; // 0-based
            int pvw = Integer.parseInt(jsonObject.get("PVW").getAsString()) - 1; // 0-based

            long finish = System.currentTimeMillis();
            long duration = finish - start;

            AvMatrixState state = new AvMatrixState(pgm, pvw);
            System.out.println("Got new state " + state + " in " + duration + "ms");
            return state;
        } catch (ClassCastException | IOException | InterruptedException e) {
            System.out.println("An error occurred on get state! Waiting for 3s...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            return null;
        }
    }

    private static class Config {
        private final String authUrl;
        private final String stateUrl;
        private final int delayMs;

        public static Config fromJson(JsonObject json) {
            String authUrl = json.has("authUrl") ? json.get("authUrl").getAsString() : null;
            String stateUrl = json.get("stateUrl").getAsString();
            int delayMs = json.get("delayMs").getAsInt();
            return new Config(authUrl, stateUrl, delayMs);
        }

        public Config(String authUrl, String stateUrl, int delayMs) {
            this.authUrl = authUrl;
            this.stateUrl = stateUrl;
            this.delayMs = delayMs;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "authUrl='" + authUrl + '\'' +
                    ", stateUrl='" + stateUrl + '\'' +
                    ", delayMs=" + delayMs +
                    '}';
        }
    }

    static class AuthenticationException extends Exception {}
}
