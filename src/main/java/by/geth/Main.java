package by.geth;

import by.geth.avmatrix.AvMatrixStateProcessor;
import by.geth.server.IntercomServer;

public class Main {
    public static void main(String[] args) {
        IntercomServer server = IntercomServer.startServer();
        if (args.length > 0) {
            AvMatrixStateProcessor.start(server, args[0]);
        }
    }
}
