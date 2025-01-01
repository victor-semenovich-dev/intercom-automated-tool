package by.geth.midi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.Arrays;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;

import by.geth.server.IntercomServer;

public class MidiProcessor implements MidiMonitor.Callback {

    private final IntercomServer server;
    private MidiMonitor monitor;

    private MidiConfig midiConfig;
    private final MidiState midiState;
    private Boolean chooseLeft = true; // the mixer contains left and right buttons

    public MidiProcessor(String configFile, IntercomServer server) {
        this(configFile, server, new MidiState());
    }

    public MidiProcessor(String configFile, IntercomServer server, MidiState initState) {
        this.midiState = initState;
        this.server = server;

        try {
            JsonObject configJson = (JsonObject) JsonParser.parseReader(new FileReader(configFile));
            midiConfig = new MidiConfig(configJson);
            monitor = new MidiMonitor(midiConfig.device, this);
            System.out.println("MidiMonitor created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.server.applyMidiState(this.midiState);
        System.out.println(midiState);
    }

    public void start() {
        monitor.start();
    }

    public void stop() {
        monitor.stop();
    }

    @Override
    public void onMessageReceived(MidiDevice device, MidiMessage message) {
        String name = device.getDeviceInfo().getName();
        String messageBytes = Arrays.toString(message.getMessage());
        int messageInt = Utils.byteArrayToInt(message.getMessage());
        System.out.println(name + ": " + messageBytes + " (" + messageInt + ")");

        boolean isMidiStateChanged = false;

        if (name.equals(midiConfig.device)) {
            if (messageInt == midiConfig.leftButtonPressed) {
                chooseLeft = true;
            } else if (messageInt == midiConfig.rightButtonPressed) {
                chooseLeft = false;
            } else if (messageInt == midiConfig.camera1) {
                if (chooseLeft)
                    midiState.leftCamera = 0;
                else
                    midiState.rightCamera = 0;
                isMidiStateChanged = true;
            } else if (messageInt == midiConfig.camera2) {
                if (chooseLeft)
                    midiState.leftCamera = 1;
                else
                    midiState.rightCamera = 1;
                isMidiStateChanged = true;
            } else if (messageInt == midiConfig.camera3) {
                if (chooseLeft)
                    midiState.leftCamera = 2;
                else
                    midiState.rightCamera = 2;
                isMidiStateChanged = true;
            } else if (messageInt == midiConfig.camera4) {
                if (chooseLeft)
                    midiState.leftCamera = 3;
                else
                    midiState.rightCamera = 3;
                isMidiStateChanged = true;
            } else if (message.getMessage()[0] == midiConfig.fader[0] && message.getMessage()[1] == midiConfig.fader[1]) {
                midiState.fader = message.getMessage()[2] / 127f;
                isMidiStateChanged = true;
            }
        }

        if (isMidiStateChanged) {
            this.server.applyMidiState(midiState);
            System.out.println(midiState);
        }
    }
}
