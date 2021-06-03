package com.example.locationapp.Model;

public class Users {
    public String name,phone,password,email, profilePic, phone1,phone2 ;

    public Users()
    {

    }

    public Users(String name, String phone, String password, String email, String profilePic, String phone1,String phone2) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.profilePic = profilePic;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email=email;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() { return phone2; }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
