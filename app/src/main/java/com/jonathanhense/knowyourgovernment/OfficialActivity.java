package com.jonathanhense.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import androidx.appcompat.app.AppCompatActivity;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";

    private Official official;
    private TextView location;
    private TextView office;
    private TextView name;
    private TextView party;
    private TextView address;
    private TextView phone;
    private TextView email;
    private TextView website;
    private TextView addressData;
    private TextView phoneData;
    private TextView emailData;
    private TextView websiteData;
    private ImageView icon;
    private ImageButton photo;
    private ImageButton facebook;
    private ImageButton twitter;
    private ImageButton youtube;
    private String loc;
    private static String partyUrl;
    private static String officialUrl;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        location = findViewById(R.id.locationID);
        office = findViewById(R.id.officeTitleID);
        name = findViewById(R.id.nameID);
        party = findViewById(R.id.partyID);
        address = findViewById(R.id.addressLabel);
        phone = findViewById(R.id.phoneLabel);
        email = findViewById(R.id.emailLabel);
        website = findViewById(R.id.websiteLabel);
        addressData = findViewById(R.id.addressID);
        phoneData = findViewById(R.id.phoneID);
        emailData = findViewById(R.id.emailID);
        websiteData = findViewById(R.id.websiteID);
        photo = findViewById(R.id.imageButton);
        facebook = findViewById(R.id.facebookButton);
        twitter = findViewById(R.id.twitterButton);
        youtube = findViewById(R.id.youTubeButton);
        scrollView = findViewById(R.id.scrollView);
        icon = findViewById(R.id.imageView);
        initialize();
        link();
    }


    private void link() {
        Linkify.addLinks(addressData, Linkify.ALL);
        addressData.setLinkTextColor(Color.WHITE);
        Linkify.addLinks(phoneData, Linkify.ALL);
        phoneData.setLinkTextColor(Color.WHITE);
        Linkify.addLinks(emailData, Linkify.ALL);
        emailData.setLinkTextColor(Color.WHITE);
        Linkify.addLinks(websiteData, Linkify.ALL);
        websiteData.setLinkTextColor(Color.WHITE);
    }


    private void initialize() {
        Intent intent = getIntent();
        if (intent.hasExtra("location")) {
            loc = intent.getStringExtra("location");
            location.setText(intent.getStringExtra("location"));
        }
        official = (Official) intent.getSerializableExtra("official");
        if (official.getParty() != null) {
            if (official.getParty().equals("Republican") || official.getParty().equals("Republican Party")) {
                if (official.getParty().equals("Republican")) {
                    party.setText(String.format("(%s Party)", official.getParty()));
                } else {
                    party.setText(String.format("(%s)", official.getParty()));
                    scrollView.setBackgroundColor(Color.RED);
                    twitter.setBackgroundColor(Color.RED);
                    facebook.setBackgroundColor(Color.RED);
                    youtube.setBackgroundColor(Color.RED);
                    icon.setImageResource(R.drawable.rep_logo);
                    photo.setBackgroundColor(Color.RED);
                    partyUrl = "https://www.gop.com";
                }

            } else if (official.getParty().equals("Democratic") || official.getParty().equals("Democratic Party")) {
                if (official.getParty().equals("Republican")) {
                    party.setText(String.format("(%s Party)", official.getParty()));
                } else {
                    party.setText(String.format("(%s)", official.getParty()));
                    scrollView.setBackgroundColor(Color.BLUE);
                    facebook.setBackgroundColor(Color.BLUE);
                    twitter.setBackgroundColor(Color.BLUE);
                    youtube.setBackgroundColor(Color.BLUE);
                    icon.setImageResource(R.drawable.dem_logo);
                    photo.setBackgroundColor(Color.BLUE);
                    partyUrl = "https://democrats.org";
                }
            } else {
                scrollView.setBackgroundColor(Color.BLACK);
                icon.setVisibility(View.GONE);
                facebook.setBackgroundColor(Color.BLACK);
                twitter.setBackgroundColor(Color.BLACK);
                youtube.setBackgroundColor(Color.BLACK);
                photo.setBackgroundColor(Color.BLACK);
            }

        } else {
            scrollView.setBackgroundColor(Color.BLACK);
            icon.setVisibility(View.GONE);
            twitter.setBackgroundColor(Color.BLACK);
            facebook.setBackgroundColor(Color.BLACK);
            youtube.setBackgroundColor(Color.BLACK);
            photo.setBackgroundColor(Color.BLACK);
        }
        office.setText(official.getOffice());
        name.setText(official.getOfficeHolder());


        if (official.getAddress() != null) {
            addressData.setText(official.getAddress());
            Log.d(TAG, "initialize: " + official.getAddress());

        } else {
            address.setVisibility(View.GONE);
            addressData.setVisibility(View.GONE);
        }
        if (official.getPhone() != null) {
            phoneData.setText(official.getPhone());
            Log.d(TAG, "initialize: " + official.getPhone());
        } else {
            phone.setVisibility(View.GONE);
            phoneData.setVisibility(View.GONE);
        }
        if (official.getEmail() != null) {
            emailData.setText(official.getEmail());
            Log.d(TAG, "initialize: " + official.getEmail());
        } else {
            email.setVisibility(View.GONE);
            emailData.setVisibility(View.GONE);
        }
        if (official.getUrl() != null) {
            websiteData.setText(official.getUrl());
            officialUrl = official.getUrl();
            Log.d(TAG, "initialize: " + official.getUrl());
        } else {
            website.setVisibility(View.GONE);
            websiteData.setVisibility(View.GONE);
        }
        if (official.getSocialMedia() == null) {
            facebook.setVisibility(View.GONE);
            youtube.setVisibility(View.GONE);
            twitter.setVisibility(View.GONE);
        } else if (official.getSocialMedia().getFacebook() == null) {
            facebook.setVisibility(View.GONE);
        } else if (official.getSocialMedia().getTwitter() == null) {
            twitter.setVisibility(View.GONE);
        }else if (official.getSocialMedia().getYouTube() == null) {
            youtube.setVisibility(View.GONE);
        }
        loadImage(official.getPhoto());
    }

    private void loadImage(final String image) {
        if (image == null) {
            photo.setImageResource(R.drawable.missing);
        } else {
            Picasso.get().load(image)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        }
    }

    public void facebookOnClick(View v) {
        String facebook = "https://www.facebook.com/" + official.getSocialMedia().getFacebook();
        String url;
        PackageManager packageManager = getPackageManager();
        try {
            int version = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (version >= 3002850) {
                url = "fb://facewebmodal/f?href=" + facebook;
            } else {
                url = "fb://page/" + official.getSocialMedia().getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            url = facebook;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(url));
        startActivity(facebookIntent);
    }

    public void youTubeOnClick(View v) {
        String name = official.getSocialMedia().getYouTube();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void twitterOnClick(View v) {
        Intent intent;
        String name = official.getSocialMedia().getTwitter();
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void openPartyWebsite(View v) {
        if (partyUrl == null)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(partyUrl));
        startActivity(intent);
    }

    public void launchPhotoActivity(View v) {
        if (official.getPhoto() == null)
            return;
        Intent intent = new Intent(this, OfficialPhotoActivity.class);
        intent.putExtra("photoLocation", loc);
        intent.putExtra("photoOfficial", official);
        startActivity(intent);
    }

    public void clickMap(View v) {
        String address = official.getAddress();

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Unable to open the specified address.", Toast.LENGTH_LONG).show();
        }
    }

    public void clickCall(View v) {
        String number = official.getPhone();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Unable to complete the call.", Toast.LENGTH_LONG).show();
        }
    }


    public void clickUrl(View v) {
        if (officialUrl == null)
            return;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(officialUrl));
        startActivity(i);
    }

    public void clickEmail(View v) {
        String addresses = official.getEmail();

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Urgent Matter from email EXTRA_SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "Vote NO on email content text from EXTRA_TEXT");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 111);
        } else {
            Toast.makeText(this, "Unable to send an email.", Toast.LENGTH_LONG).show();
        }
    }


}
