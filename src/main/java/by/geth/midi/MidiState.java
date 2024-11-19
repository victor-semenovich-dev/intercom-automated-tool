package by.geth.midi;

import java.util.Locale;

public class MidiState {
    public int leftCamera; // 0 - 3
    public int rightCamera; // 0 - 3
    public float fader; // 0 - 1

    public MidiState() {
        this(0, 0, 0);
    }

    public MidiState(int leftCamera, int rightCamera, float fader) {
        this.leftCamera = leftCamera;
        this.rightCamera = rightCamera;
        this.fader = fader;
    }

    @Override
    public String toString() {
        return "(" + leftCamera + "; " + rightCamera + "; " + String.format(Locale.US, "%.2f", fader) + ")";
    }
}
