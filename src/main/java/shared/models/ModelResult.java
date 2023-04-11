package shared.models;

import java.io.Serializable;

public class ModelResult<T> implements Serializable {
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
