package by.geth.server.model;

import com.google.gson.JsonObject;

public class Camera {
    private boolean live;
    private boolean ready;

    public Camera() {
        this.live = false;
        this.ready = false;
    }

    public Camera(boolean live, boolean ready) {
        this.live = live;
        this.ready = ready;
    }

    public boolean isLive() {
        return live;
    }

    public boolean isReady() {
        return ready;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("live", live);
        object.addProperty("ready", ready);
        return object;
    }
}
