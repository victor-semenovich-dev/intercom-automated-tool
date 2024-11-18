package by.geth.server.model;

import com.google.gson.JsonObject;

public class Camera {
    private boolean live;
    private boolean ready; // ready for live
    private boolean attention; // a camera man wants to make camera live
    private boolean change; // a camera man needs to change the frame

    public Camera() {
        this.live = false;
        this.ready = false;
        this.attention = false;
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

    public boolean isAttention() {
        return attention;
    }

    public boolean isChange() {
        return change;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setAttention(boolean attention) {
        this.attention = attention;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("live", live);
        object.addProperty("ready", ready);
        object.addProperty("attention", attention);
        object.addProperty("change", change);
        return object;
    }
}
