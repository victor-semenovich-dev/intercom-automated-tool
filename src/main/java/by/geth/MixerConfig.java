package by.geth;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class MixerConfig {
    public String device;

    public long leftButtonPressed;
    public long rightButtonPressed;

    public long camera1;
    public long camera2;
    public long camera3;
    public long camera4;

    public long[] fader;

    public MixerConfig(JSONObject jsonObject) {
        device = (String) jsonObject.get("device");
        leftButtonPressed = (long) jsonObject.get("leftButtonPressed");
        rightButtonPressed = (long) jsonObject.get("rightButtonPressed");
        camera1 = (long) jsonObject.get("camera1");
        camera2 = (long) jsonObject.get("camera2");
        camera3 = (long) jsonObject.get("camera3");
        camera4 = (long) jsonObject.get("camera4");

        JSONArray jsonArray = (JSONArray) jsonObject.get("fader");
        fader = new long[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            fader[i] = (long) jsonArray.get(i);
        }
    }

    @Override
    public String toString() {
        return "MixerConfig{" +
                "device='" + device + '\'' +
                ", leftButtonPressed=" + leftButtonPressed +
                ", rightButtonPressed=" + rightButtonPressed +
                ", camera1=" + camera1 +
                ", camera2=" + camera2 +
                ", camera3=" + camera3 +
                ", camera4=" + camera4 +
                ", fader=" + Arrays.toString(fader) +
                '}';
    }
}
