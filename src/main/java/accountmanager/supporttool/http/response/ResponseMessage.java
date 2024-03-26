package accountmanager.supporttool.http.response;

import java.io.Serializable;

public class ResponseMessage implements Serializable {

    protected String message = "";
    protected Object data = "";

    public ResponseMessage(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}