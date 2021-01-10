package by.geth;

public class InitStateReader {
    public static MixerState readInitState() {
        int leftCamera = 0;
        int rightCamera = 0;
        float fader = -1;

        while (leftCamera == 0) {
            int res = Utils.readInt("What camera is selected from the left side? (1-4): ");
            if (res >= 1 && res <= 4) {
                leftCamera = res;
            }
        }

        while (rightCamera == 0) {
            int res = Utils.readInt("What camera is selected from the right side? (1-4): ");
            if (res >= 1 && res <= 4) {
                rightCamera = res;
            }
        }

        while (fader < 0) {
            int res = Utils.readInt("Enter 0, if the fader is in the down position, 1 otherwise: ");
            if (res >= 0 && res <= 1) {
                fader = res;
            }
        }

        return new MixerState(leftCamera, rightCamera, fader);
    }
}
