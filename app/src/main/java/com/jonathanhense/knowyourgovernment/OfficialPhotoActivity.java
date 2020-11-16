package com.jonathanhense.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class OfficialPhotoActivity extends AppCompatActivity {

        private TextView photoOffice;
        private TextView photoOfficeHolder;
        private TextView photoLocation;
        private ImageButton photoPhoto;
        private ImageView photoIcon;
        private ConstraintLayout constraintLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_photo);

            constraintLayout = findViewById(R.id.photoConstraintLayout);

            photoOffice = findViewById(R.id.photoOfficeNameID);
            photoOfficeHolder = findViewById(R.id.photoOfficeHolderID);
            photoLocation = findViewById(R.id.photoLocationID);
            photoPhoto = findViewById(R.id.photoImageButton);
            photoIcon = findViewById(R.id.photoImageView);

            initializePhoto();
        }

        private void initializePhoto(){
            Intent intent = getIntent();
            Official official = (Official) intent.getSerializableExtra("photoOfficial");

            photoOffice.setText(official.getOffice());
            photoOfficeHolder.setText(official.getOfficeHolder());

            if(official.getParty()!=null){
                if (official.getParty().equals("Democratic Party") || official.getParty().equals("Democratic")) {
                    constraintLayout.setBackgroundColor(Color.BLUE);
                    photoIcon.setImageResource(R.drawable.dem_logo);
                    photoPhoto.setBackgroundColor(Color.BLUE);
                }
                else if (official.getParty().equals("Republican Party") || official.getParty().equals("Republican")){
                    constraintLayout.setBackgroundColor(Color.RED);
                    photoIcon.setImageResource(R.drawable.rep_logo);
                    photoPhoto.setBackgroundColor(Color.RED);
                }
                else {
                    constraintLayout.setBackgroundColor(Color.BLACK);
                    photoIcon.setVisibility(View.GONE);
                    photoPhoto.setBackgroundColor(Color.BLACK);
                }
            }else {
                constraintLayout.setBackgroundColor(Color.BLACK);
                photoIcon.setVisibility(View.GONE);
                photoPhoto.setBackgroundColor(Color.BLACK);
            }

            if(intent.hasExtra("photoLocation")){
                String location = intent.getStringExtra("photoLocation");
                photoLocation.setText(location);
            }
            loadPhoto(official.getPhoto());
        }

    private void loadPhoto(String imageURL) {
        Picasso.get().load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(photoPhoto);
    }
}
