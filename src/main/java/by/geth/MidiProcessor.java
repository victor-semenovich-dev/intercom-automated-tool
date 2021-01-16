package by.geth;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MidiProcessor implements MidiMonitor.Callback {

    private final Database database = new Database();
    private MidiMonitor monitor;

    private MixerConfig mixerConfig;
    private final MixerState mixerState;
    private Boolean isLeftButtonPressed;

    public MidiProcessor(String configFile, MixerState initState) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject configJson = (JSONObject) parser.parse(new FileReader(configFile));
            mixerConfig = new MixerConfig(configJson);
            monitor = new MidiMonitor(mixerConfig.device, this);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        mixerState = initState;
        uploadMixerState();
        System.out.println(mixerState);
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

        boolean isMixerStateChanged = false;
        if (name.equals(mixerConfig.device)) {
            if (messageInt == mixerConfig.leftButtonPressed) {
                isLeftButtonPressed = true;
            } else if (messageInt == mixerConfig.rightButtonPressed) {
                isLeftButtonPressed = false;
            } else if (messageInt == mixerConfig.camera1 && isLeftButtonPressed != null) {
                if (isLeftButtonPressed)
                    mixerState.leftCamera = 1;
                else
                    mixerState.rightCamera = 1;
                isMixerStateChanged = true;
            } else if (messageInt == mixerConfig.camera2 && isLeftButtonPressed != null) {
                if (isLeftButtonPressed)
                    mixerState.leftCamera = 2;
                else
                    mixerState.rightCamera = 2;
                isMixerStateChanged = true;
            } else if (messageInt == mixerConfig.camera3 && isLeftButtonPressed != null) {
                if (isLeftButtonPressed)
                    mixerState.leftCamera = 3;
                else
                    mixerState.rightCamera = 3;
                isMixerStateChanged = true;
            } else if (messageInt == mixerConfig.camera4 && isLeftButtonPressed != null) {
                if (isLeftButtonPressed)
                    mixerState.leftCamera = 4;
                else
                    mixerState.rightCamera = 4;
                isMixerStateChanged = true;
            } else if (message.getMessage()[0] == mixerConfig.fader[0] && message.getMessage()[1] == mixerConfig.fader[1]) {
                mixerState.fader = message.getMessage()[2] / 127f;
                isMixerStateChanged = true;
            }
        }

        if (isMixerStateChanged) {
            System.out.println(mixerState);
            uploadMixerState();
        }
    }

    private void uploadMixerState() {
        boolean camera1Live = mixerState.leftCamera == 1 && mixerState.fader < 1 || mixerState.rightCamera == 1 && mixerState.fader > 0;
        boolean camera2Live = mixerState.leftCamera == 2 && mixerState.fader < 1 || mixerState.rightCamera == 2 && mixerState.fader > 0;
        boolean camera3Live = mixerState.leftCamera == 3 && mixerState.fader < 1 || mixerState.rightCamera == 3 && mixerState.fader > 0;
        boolean camera4Live = mixerState.leftCamera == 4 && mixerState.fader < 1 || mixerState.rightCamera == 4 && mixerState.fader > 0;

        database.writeValue("camera/1/isLive", camera1Live);
        database.writeValue("camera/2/isLive", camera2Live);
        database.writeValue("camera/3/isLive", camera3Live);
        database.writeValue("camera/4/isLive", camera4Live);

        if (camera1Live) {
            database.writeValue("camera/1/isRequested", false);
        }
        if (camera2Live) {
            database.writeValue("camera/2/isRequested", false);
        }
        if (camera3Live) {
            database.writeValue("camera/3/isRequested", false);
        }
        if (camera4Live) {
            database.writeValue("camera/4/isRequested", false);
        }
    }
}
