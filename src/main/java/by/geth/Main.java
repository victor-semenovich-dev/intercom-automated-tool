package by.geth;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers ) {
            logger.setLevel(Level.OFF);
        }

        MixerState initState = InitStateReader.readInitState();
        MidiProcessor processor = new MidiProcessor(args[0], initState);

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
