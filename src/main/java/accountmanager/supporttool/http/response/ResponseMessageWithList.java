package accountmanager.supporttool.http.response;

import java.util.List;

public class ResponseMessageWithList<T>{

    protected String message = "";
    protected List<T> data;

    public ResponseMessageWithList(){}
    public ResponseMessageWithList(String message, List<T> data){
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}