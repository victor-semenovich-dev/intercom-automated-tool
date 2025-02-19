package by.geth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import by.geth.midi.MidiProcessor;
import by.geth.server.IntercomServer;

public class Main {
    public static void main(String[] args) {
//        MixerState initState = InitStateReader.readInitState();
        confirmVideoMixerPrepared();

        IntercomServer server = IntercomServer.startServer();
        MidiProcessor processor = new MidiProcessor(args[0], server);

        processor.start();
        runInfiniteLoop();
        processor.stop();
    }

    private static void runInfiniteLoop() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String line = reader.readLine();
                if ("exit".equals(line)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void confirmVideoMixerPrepared() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println(
                    "Чтобы запустить сервер для Интеркома, убедись в следующем и нажми Enter:\n" +
                    "1. Видеопульт включён;\n" +
                    "2. Камера 1 выбрана с обеих сторон A и B;\n" +
                    "3. Центральный фейдер опущен."
            );
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
