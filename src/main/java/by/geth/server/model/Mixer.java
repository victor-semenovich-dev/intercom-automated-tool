package by.geth.server.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Mixer {
    private List<Camera> cameras;

    public Mixer() {
        this.cameras = new ArrayList<>();
        cameras.add(new Camera());
        cameras.add(new Camera());
        cameras.add(new Camera());
        cameras.add(new Camera());
    }

    public Mixer(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for (Camera camera: cameras) {
            array.add(camera.toJson());
        }
        object.add("cameras", array);
        return object;
    }
}
