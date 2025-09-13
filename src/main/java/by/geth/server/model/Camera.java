package by.geth.server.model;

import com.google.gson.JsonObject;

public class Camera {
    private boolean live;
    private boolean preview;
    private boolean ready; // ready for live
    private boolean attention; // a camera man wants to make camera live
    private boolean change; // a camera man needs to change the frame

    public Camera() {
        this.live = false;
        this.preview = false;
        this.ready = true;
        this.attention = false;
        this.change = false;
    }

    public boolean isLive() {
        return live;
    }

    public boolean isPreview() {
        return preview;
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

    public void setPreview(boolean preview) {
        this.preview = preview;
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
        object.addProperty("preview", preview);
        object.addProperty("ready", ready);
        object.addProperty("attention", attention);
        object.addProperty("change", change);
        return object;
    }
}
