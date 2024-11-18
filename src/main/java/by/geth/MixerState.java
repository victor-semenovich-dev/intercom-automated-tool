package by.geth;

import java.util.Locale;

public class MixerState {
    public int leftCamera;
    public int rightCamera;
    public float fader;
    public ServerLocation location;

    public MixerState() {
        this.leftCamera = 1;
        this.rightCamera = 1;
        this.fader = 0;
        this.location = ServerLocation.EU;
    }

    public MixerState(int leftCamera, int rightCamera, float fader, ServerLocation location) {
        this.leftCamera = leftCamera;
        this.rightCamera = rightCamera;
        this.fader = fader;
        this.location = location;
    }

    @Override
    public String toString() {
        return "(" + leftCamera + "; " + rightCamera + "; " + String.format(Locale.US, "%.2f", fader) + "; " +
                location + ")";
    }
}
