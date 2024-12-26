package by.geth.server.model;

import com.google.gson.JsonObject;

public class Message {
    private int cameraId;
    private long timestamp;
    private String message;
    private String userName;

    public Message(int cameraId, long timestamp, String message, String userName) {
        this.cameraId = cameraId;
        this.timestamp = timestamp;
        this.message = message;
        this.userName = userName;
    }

    public int getCameraId() {
        return cameraId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("cameraId", cameraId);
        object.addProperty("timestamp", timestamp);
        object.addProperty("message", message);
        object.addProperty("userName", userName);
        return object;
    }
}
