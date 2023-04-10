package client.models;

public class ModelResult<T> {
    public T data;
    public boolean success;
    public String message;

    public ModelResult() {
        this(null, false, "");
    }

    public ModelResult(T data, boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }
}
