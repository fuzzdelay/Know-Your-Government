package com.jonathanhense.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

    private String office;
    private String officeHolder;
    private String party;
    private String address;
    private String phone;
    private String email;
    private String url;
    private String photo;
    private SocialMedia socialMedia;

    //public static final String unknown = "Unknown";
    //public static final String noData = "No Data Available";

    public Official(String office){
        this.office = office;
    }


    @Override
    public String toString() {
        return "Official{" +
                "office: '" + office + '\'' +
                ", officeHolder: '" + officeHolder + '\'' +
                ", address: '" + address + '\'' +
                ", party: '" + party + '\'' +
                ", phone: '" + phone + '\'' +
                ", url: '" + url + '\'' +
                ", email: '" + email + '\'' +
                ", socialMedia: " + socialMedia +
                ", photo: '" + photo + '\'' +
                '}';
    }

    public String getOffice(){
        return office;
    }

    public String getOfficeHolder() {
        return officeHolder;
    }

    public void setOfficeHolder(String officeHolder) {
        this.officeHolder = officeHolder;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    public SocialMedia getSocialMedia() {
        return socialMedia;
    }
}

