package by.geth;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class MidiMonitor {

    public interface Callback {
        void onMessageReceived(MidiDevice device, MidiMessage message);
    }

    private final List<MidiDevice> openedDevices = new ArrayList<>();
    private final Callback callback;

    public MidiMonitor(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        MidiDevice.Info[] deviceInfoArray = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info deviceInfo: deviceInfoArray) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                if (device.getMaxTransmitters() != 0) {
                    device.open();
                    openedDevices.add(device);

                    Transmitter transmitter = device.getTransmitter();
                    transmitter.setReceiver(new DeviceReceiver(device, callback));
                }
            } catch (MidiUnavailableException e) {
                System.err.println("Device not available: " + deviceInfo.getName());
            }
        }
    }

    public void stop() {
        for (MidiDevice device: openedDevices) {
            device.close();
        }
    }

    private static class DeviceReceiver implements Receiver {
        private final MidiDevice device;
        private final MidiMonitor.Callback callback;

        public DeviceReceiver(MidiDevice device, MidiMonitor.Callback callback) {
            this.device = device;
            this.callback = callback;
        }

        @Override
        public void send(MidiMessage midiMessage, long l) {
            callback.onMessageReceived(device, midiMessage);
        }

        @Override
        public void close() { }
    }
}
