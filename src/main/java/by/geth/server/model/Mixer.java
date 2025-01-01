package by.geth.server.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Mixer {
    private List<Camera> cameras = new ArrayList<>();
    private List<Message> incomingMessages = new ArrayList<>();
    private List<Message> outcomingMessages = new ArrayList<>();

    public Mixer() {
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

    public List<Message> getIncomingMessages() {
        return incomingMessages;
    }

    public List<Message> getOutcomingMessages() {
        return outcomingMessages;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        JsonArray camerasArray = new JsonArray();
        for (Camera camera: cameras) {
            camerasArray.add(camera.toJson());
        }
        JsonArray incomingArray = new JsonArray();
        for (Message message: incomingMessages) {
            incomingArray.add(message.toJson());
        }
        JsonArray outcomingArray = new JsonArray();
        for (Message message: outcomingMessages) {
            outcomingArray.add(message.toJson());
        }
        object.add("cameras", camerasArray);
        object.add("incoming", incomingArray);
        object.add("outcoming", outcomingArray);
        return object;
    }
}
