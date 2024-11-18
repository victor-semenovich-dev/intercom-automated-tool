package by.geth.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import by.geth.server.model.Mixer;

public class IntercomServer extends WebSocketServer {
    private Mixer mixer = new Mixer();

    public IntercomServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        log("onOpen", conn.getRemoteSocketAddress());
        conn.send(mixer.toJson());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log("onClose", conn.getRemoteSocketAddress(), code, reason, remote);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        log("onMessage", conn.getRemoteSocketAddress(), message);
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

    public static void main(String[] args) {
        try (final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String host = socket.getLocalAddress().getHostAddress();
            int port = 8080;

            InetSocketAddress socketAddress = new InetSocketAddress(host, port);

            System.out.println("Run server at " + socketAddress);
            WebSocketServer server = new IntercomServer(socketAddress);
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
