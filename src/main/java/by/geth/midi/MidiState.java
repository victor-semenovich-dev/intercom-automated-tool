package by.geth.midi;

import java.util.Locale;

import by.geth.firebase.FirebaseServerLocation;

public class MidiState {
    public int leftCamera; // 0 - 3
    public int rightCamera; // 0 - 3
    public float fader; // 0 - 1
    public FirebaseServerLocation location;

    public MidiState() {
        this(0, 0, 0, null);
    }

    public MidiState(int leftCamera, int rightCamera, float fader, FirebaseServerLocation location) {
        this.leftCamera = leftCamera;
        this.rightCamera = rightCamera;
        this.fader = fader;
        this.location = location;
    }

    @Override
    public String toString() {
        return "(" + leftCamera + "; " + rightCamera + "; " + String.format(Locale.US, "%.2f", fader) + ")";
    }
}
