package model;


import com.google.firebase.Timestamp;

public class Evocate {
    private String userID,title,description,username;
    private String imageURL;
    private Timestamp timeAdded;

    public Evocate() {
    }

    public Evocate(String userID, String title, String description, String username, String imageURL, Timestamp timeAdded) {
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.username = username;
        this.imageURL = imageURL;
        this.timeAdded = timeAdded;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
