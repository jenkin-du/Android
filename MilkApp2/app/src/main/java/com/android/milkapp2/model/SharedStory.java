package com.android.milkapp2.model;


public class SharedStory {

    private String Id;
    private String Message;
    private String ImageName;
    private String Time;
    private String Longitude;
    private String Latitude;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        this.ImageName = imageName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    @Override
    public String toString() {
        return "SharedStory{" +
                "Id='" + Id + '\'' +
                ", Message='" + Message + '\'' +
                ", ImageName='" + ImageName + '\'' +
                ", Time='" + Time + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Latitude='" + Latitude + '\'' +
                '}';
    }
}
