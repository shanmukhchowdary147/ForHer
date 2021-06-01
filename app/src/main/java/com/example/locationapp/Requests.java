package com.example.locationapp;

public class Requests {
    private String name,phone,latitude,longitude,address,date,time,image;

    public Requests() {
    }

    public Requests(String name, String phone, String latitude, String longitude, String address, String date, String time) {
        this.name = name;
        this.phone = phone;
        this.latitude=latitude;
        this.longitude = longitude;
        this.address = address;
        this.date = date;
        this.time=time;
//        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAdditionalInfo(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

//    public String getImage() {
//        return image;
//    }

//    public void setImage(String image) {
//        this.image = image;
//    }
}
