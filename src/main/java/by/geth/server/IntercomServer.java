package by.geth.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import by.geth.midi.MidiState;
import by.geth.server.model.Camera;
import by.geth.server.model.Message;
import by.geth.server.model.Mixer;

public class IntercomServer extends WebSocketServer {
    private final Mixer mixer = new Mixer();

    public static IntercomServer startServer() {
        try (final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String host = socket.getLocalAddress().getHostAddress();
            int port = 8080;

            InetSocketAddress socketAddress = new InetSocketAddress(host, port);

            System.out.println("Run the socket at " + socketAddress);
            IntercomServer server = new IntercomServer(socketAddress);

            new Thread(server).start();

            return server;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public IntercomServer(InetSocketAddress address) {
        super(address);
    }

    public void applyMidiState(MidiState state) {
        for (int i = 0; i < mixer.getCameras().size(); i++) {
            Camera camera = mixer.getCameras().get(i);
            camera.setLive(state.leftCamera == i && state.fader < 1 || state.rightCamera == i && state.fader > 0);
            if (camera.isLive()) {
                camera.setAttention(false);
                camera.setChange(false);
            }
        }
        broadcastMixer();
    }

    private void broadcastMixer() {
        System.out.println(mixer.toJson());
        broadcast(mixer.toJson().toString());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        log("onOpen", conn.getRemoteSocketAddress());
        conn.send(mixer.toJson().toString());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log("onClose", conn.getRemoteSocketAddress(), code, reason, remote);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // TODO check thread
        log("onMessage", conn.getRemoteSocketAddress(), message);

        try {
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(message);
            if (jsonObject.has("id")) {
                //update a camera state
                int id = jsonObject.get("id").getAsInt();
                if (jsonObject.has("live")) {
                    boolean live = jsonObject.get("live").getAsBoolean();
                    if (live) {
                        for (int i = 0; i < mixer.getCameras().size(); i++) {
                            mixer.getCameras().get(i).setLive(i == id);
                        }
                    } else {
                        mixer.getCameras().get(id).setLive(false);
                    }
                }
                if (jsonObject.has("ready")) {
                    boolean ready = jsonObject.get("ready").getAsBoolean();
                    mixer.getCameras().get(id).setReady(ready);
                }
                if (jsonObject.has("attention")) {
                    boolean attention = jsonObject.get("attention").getAsBoolean();
                    mixer.getCameras().get(id).setAttention(attention);
                }
                if (jsonObject.has("change")) {
                    boolean change = jsonObject.get("change").getAsBoolean();
                    mixer.getCameras().get(id).setChange(change);
                }
            } else if (jsonObject.has("message")) {
                // add new message
                Integer to = null;
                Integer from = null;
                long timestamp = jsonObject.get("timestamp").getAsLong();
                String messageText = jsonObject.get("message").getAsString();
                if (jsonObject.has("to")) {
                    to = jsonObject.get("to").getAsInt();
                }
                if (jsonObject.has("from")) {
                    from = jsonObject.get("from").getAsInt();
                }

                if (to != null) {
                    mixer.getOutcomingMessages().add(new Message(to, timestamp, messageText));
                }
                if (from != null) {
                    mixer.getIncomingMessages().add(new Message(from, timestamp, messageText));
                }
            } else if (jsonObject.has("command")) {
                // process other commands
                String command = jsonObject.get("command").getAsString();
                switch (command) {
                    case "cancelIncomingMessages":
                        mixer.getIncomingMessages().clear();
                        break;
                    case "cancelOutcomingMessages":
                        int to = jsonObject.get("to").getAsInt();
                        List<Message> messagesToRemove = new ArrayList<>();
                        for (Message outcomingMessage: mixer.getOutcomingMessages()) {
                            if (outcomingMessage.getCameraId() == to) {
                                messagesToRemove.add(outcomingMessage);
                            }
                        }
                        mixer.getOutcomingMessages().removeAll(messagesToRemove);
                        break;
                }
            }

            broadcastMixer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        log("onMessage", conn.getRemoteSocketAddress(), message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logError("onError", ex);
    }

    @Override
    public void onStart() {
        log("onStart");
    }

    private void log(String message, Object... args) {
        System.out.println(IntercomServer.class.getSimpleName() + ": " + message + ", " + Arrays.toString(args));
    }

    private void logError(String message, Object... args) {
        System.err.println(IntercomServer.class.getSimpleName() + ": " + message + ", " + Arrays.toString(args));
    }
}
