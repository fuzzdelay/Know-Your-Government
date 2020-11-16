package com.jonathanhense.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private final static String TAG = "AboutActivity";
    private TextView google;
    String gAPI = "https://developers.google.com/civic-information/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        google = findViewById(R.id.googleAPI);
        link();
    }
    private void link() {
        Linkify.addLinks(google, Linkify.ALL);
        google.setLinkTextColor(Color.WHITE);

    }

    public void openGoogleDev(View v){
        if (gAPI == null)
            return;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(gAPI));
        startActivity(i);
    }
}
