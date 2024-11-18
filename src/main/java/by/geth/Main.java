package by.geth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
//        MixerState initState = InitStateReader.readInitState();
        MidiProcessor processor = new MidiProcessor(args[0]);

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
}
