package com.jonathanhense.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final List<Official> officials = new ArrayList<Official>();
    private OfficialAdapter officialAdapter;
    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private TextView location;
    private Criteria criteria;
    private static int MY_LOCATION_REQUEST_CODE_ID = 111;
    private String selection;
    private LocationManager locationManager;
    private Location loc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.locationID);

        recyclerView = findViewById(R.id.nameID);
        officialAdapter = new OfficialAdapter(officials, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(!checkNetworkConnection()){
            buildNoConnectionDialog();
        }
        getLocation();
    }

    private void buildNoConnectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("No data can be accessed without an internet connection.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", location.getText().toString());
        intent.putExtra("official", officials.get(position));
        startActivity(intent);
    }

    private void getLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        //using gps by default
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        // uncomment to use network for location
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        } else {
            setLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void setLocation() {

        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (bestProvider != null) {
            loc = locationManager.getLastKnownLocation(bestProvider);
        }
        if (loc != null) {
            findZip(loc.getLatitude(), loc.getLongitude());
            Log.d(TAG, "setLocation: " + loc.getLatitude() + " " + loc.getLongitude());
        } else {
            Log.d(TAG, "setLocation: " + "Unable to determine location");
        }
    }

    public void findZip(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String zip = addresses.get(0).getPostalCode();
            //Log.d(TAG, "findPostalCode: " + postalCode);
            loadOfficial(zip);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void loadOfficial(String postalCode){
        OfficialDownloader officialDownloader = new OfficialDownloader(this, postalCode);
        new Thread(officialDownloader).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                launchAboutActivity();
                return true;
            case R.id.locationEntry:
                buildSearchDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void updateOfficials(Official official) {
        officials.add(official);
        officialAdapter.notifyDataSetChanged();
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void buildSearchDialog() {
        if (!checkNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed without an internet connection.");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                selection = et.getText().toString().trim();
                OfficialDownloader officialDownloader = new OfficialDownloader(MainActivity.this, selection);
                new Thread(officialDownloader).start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setMessage("Enter a City, State or a Zip Code: ");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setLocation(String city, String state, String zip) {
        String userLocation = city + "," + state + " " + zip;
        location.setText(userLocation);
    }

    public void clearOfficialList() {
        officials.clear();
    }

}