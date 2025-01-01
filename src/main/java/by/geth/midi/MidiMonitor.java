package by.geth.midi;

import javax.sound.midi.*;

public class MidiMonitor {

    public interface Callback {
        void onMessageReceived(MidiDevice device, MidiMessage message);
    }

    private final String deviceName;
    private MidiDevice device;
    private final Callback callback;

    public MidiMonitor(String deviceName, Callback callback) {
        this.deviceName = deviceName;
        this.callback = callback;
    }

    public void start() {
        MidiDevice.Info[] deviceInfoArray = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info deviceInfo: deviceInfoArray) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                if (deviceName.equals(deviceInfo.getName()) && device.getMaxTransmitters() != 0) {
                    this.device = device;
                    device.open();

                    Transmitter transmitter = device.getTransmitter();
                    transmitter.setReceiver(new DeviceReceiver(device, callback));
                }
            } catch (MidiUnavailableException e) {
                System.err.println("Device not available: " + deviceInfo.getName());
            }
        }
    }

    public void stop() {
       if (device != null) {
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
