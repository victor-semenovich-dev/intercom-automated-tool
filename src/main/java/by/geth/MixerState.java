package by.geth;

import java.util.Locale;

public class MixerState {
    public int leftCamera;
    public int rightCamera;
    public float fader;

    public MixerState(int leftCamera, int rightCamera, float fader) {
        this.leftCamera = leftCamera;
        this.rightCamera = rightCamera;
        this.fader = fader;
    }

    @Override
    public String toString() {
        return "(" + leftCamera + "; " + rightCamera + "; " + String.format(Locale.US, "%.2f", fader) + ")";
    }
}
