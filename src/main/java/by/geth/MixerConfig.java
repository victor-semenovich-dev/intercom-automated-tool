package by.geth;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    public MixerConfig(JsonObject jsonObject) {
        device = jsonObject.get("device").getAsString();
        leftButtonPressed = jsonObject.get("leftButtonPressed").getAsLong();
        rightButtonPressed = jsonObject.get("rightButtonPressed").getAsLong();
        camera1 = jsonObject.get("camera1").getAsLong();
        camera2 = jsonObject.get("camera2").getAsLong();
        camera3 = jsonObject.get("camera3").getAsLong();
        camera4 = jsonObject.get("camera4").getAsLong();

        JsonArray jsonArray = jsonObject.get("fader").getAsJsonArray();
        fader = new long[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            fader[i] = jsonArray.get(i).getAsLong();
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
