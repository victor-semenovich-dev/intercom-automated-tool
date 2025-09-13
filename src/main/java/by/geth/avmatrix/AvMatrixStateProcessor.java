package by.geth.avmatrix;

import by.geth.server.IntercomServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AvMatrixStateProcessor {

    private IntercomServer server;
    private Config config;

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
            while (true) {
                try {
                    AvMatrixState state = getState();
                    server.applyAvMatrixState(state);
                    Thread.sleep(config.delayMs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private AvMatrixState getState() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseBody);
            int pgm = Integer.parseInt(jsonObject.get("PGM").getAsString()) - 1; // 0-based
            int pvw = Integer.parseInt(jsonObject.get("PVW").getAsString()) - 1; // 0-based
            return new AvMatrixState(pgm, pvw);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class Config {
        private String url;
        private int delayMs;

        public static Config fromJson(JsonObject json) {
            String url = json.get("url").getAsString();
            int delayMs = json.get("delayMs").getAsInt();
            return new Config(url, delayMs);
        }

        public Config(String url, int delayMs) {
            this.url = url;
            this.delayMs = delayMs;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "url='" + url + '\'' +
                    ", delayMs=" + delayMs +
                    '}';
        }
    }
}
