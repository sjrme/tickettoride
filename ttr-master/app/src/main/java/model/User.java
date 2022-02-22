package model;

public class User {
    private static final User user = new User();
    private User(){}


    private String username;
    private String authToken;

    public static User getMyUser(){
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setMyUsername(String username) {
        this.username = username;
    }

    public void setMyAuthToken(String authToken) {
        this.authToken = authToken;
    }




}
