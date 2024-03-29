package by.geth;

public class InitStateReader {
    public static MixerState readInitState() {
        int leftCamera = 0;
        int rightCamera = 0;
        float fader = -1;
        ServerLocation location = null;

        while (leftCamera == 0) {
            int res = Utils.readInt("Какая камера выбрана с левой стороны? (1-4): ");
            if (res >= 1 && res <= 4) {
                leftCamera = res;
            }
        }

        while (rightCamera == 0) {
            int res = Utils.readInt("Какая камера выбрана с правой стороны? (1-4): ");
            if (res >= 1 && res <= 4) {
                rightCamera = res;
            }
        }

        while (fader < 0) {
            int res = Utils.readInt("Введи 0, если фейдер опущен, или 1, если он поднят: ");
            if (res >= 0 && res <= 1) {
                fader = res;
            }
        }

        while (location == null) {
            int res = Utils.readInt("Выбери сервер (0 - USA, 1 - EU): ");
            if (res == 0) {
                location = ServerLocation.USA;
            } else if (res == 1) {
                location = ServerLocation.EU;
            }
        }

        return new MixerState(leftCamera, rightCamera, fader, location);
    }
}
