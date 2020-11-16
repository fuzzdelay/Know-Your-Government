package com.jonathanhense.knowyourgovernment;

import java.io.Serializable;

public class SocialMedia implements Serializable {

    private String facebook;
    private String youTube;
    private String twitter;

    public SocialMedia() {
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYouTube() {
        return youTube;
    }

    public void setYouTube(String youTube) {
        this.youTube = youTube;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
