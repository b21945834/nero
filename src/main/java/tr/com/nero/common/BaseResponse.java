package tr.com.nero.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BaseResponse<T> {
    private NeroResponseStatus status;
    private List<String> messages;
    private T body;

    public BaseResponse() {
        this.messages = new ArrayList<>();
        this.status = NeroResponseStatus.SUCCESS;
    }

    public BaseResponse(T body) {
        this.body = body;
        this.messages = new ArrayList<>();
        this.status = NeroResponseStatus.SUCCESS;
    }

    public BaseResponse(String message, NeroResponseStatus status){
        this.status = status;
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }

    public BaseResponse(String message){
        this.status = NeroResponseStatus.ERROR;
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }

    public BaseResponse(List<String> messages) {
        this.status = NeroResponseStatus.ERROR;
        this.messages = messages;
    }
    public void addMessage(String message) {
        this.messages.add(message);
    }
}

