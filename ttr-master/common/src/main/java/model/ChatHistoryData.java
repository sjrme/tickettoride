package model;

import java.io.Serializable;

/**
 * TODO: description
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-14
 */
public class ChatHistoryData implements Serializable {

    private String username;
    private String message;

    public ChatHistoryData(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
