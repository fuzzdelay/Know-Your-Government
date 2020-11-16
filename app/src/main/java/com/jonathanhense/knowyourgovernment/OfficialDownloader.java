package com.jonathanhense.knowyourgovernment;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OfficialDownloader implements Runnable {

    private static final String TAG = "OfficialDownloader";
    private static final String googleCivicAPIurl = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private static final String googleLink = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    //replace with your own google API key
    private static final String key = "";

    private static final String locationPrefix = "&address=";

    private final MainActivity mainActivity;
    private final String target;

    public OfficialDownloader(MainActivity mainActivity, String target) {
        this.mainActivity = mainActivity;
        this.target = target;
    }

    @Override
    public void run() {
        Uri.Builder uriBuilder = Uri.parse(googleLink + target).buildUpon();
        String urlToUse = uriBuilder.toString();

        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + connection.getResponseCode());
                return;
            }

            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(input)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            return;
        }

        process(sb.toString());
        Log.d(TAG, "run: ");
    }

    private void process(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONObject normalizedInput = jObjMain.getJSONObject("normalizedInput");
            JSONArray officesArray = jObjMain.getJSONArray("offices");
            JSONArray officialsArray = jObjMain.getJSONArray("officials");
            mainActivity.clearOfficialList();


            for (int i = 0; i < officesArray.length(); i++) {
                JSONObject jObj = officesArray.getJSONObject(i);
                String office = jObj.getString("name");
                Log.d(TAG, "process: " + office);

                JSONArray indices = jObj.getJSONArray("officialIndices");
                //ArrayList<Integer> indices = new ArrayList<>();

                for (int j = 0; j < indices.length(); j++) {
                    int pos = Integer.parseInt(indices.getString(j));
                    Official official = new Official(office);
                    JSONObject officialObj = officialsArray.getJSONObject(pos);

                    official.setOfficeHolder(officialObj.getString("name"));

                    if(officialObj.has("address")) {
                        JSONArray addressArray = officialObj.getJSONArray("address");
                        JSONObject jsonAddress = addressArray.getJSONObject(0);

                        String address = "";

                        if (jsonAddress.has("line1"))
                            address += jsonAddress.getString("line1") + '\n';
                        if (jsonAddress.has("line2"))
                            address += jsonAddress.getString("line2") + '\n';
                        if (jsonAddress.has("line3"))
                            address += jsonAddress.getString("line3") + '\n';
                        if (jsonAddress.has("city"))
                            address += jsonAddress.getString("city") + ", ";
                        if (jsonAddress.has("state"))
                            address += jsonAddress.getString("state") + ' ';
                        if (jsonAddress.has("zip"))
                            address += jsonAddress.getString("zip");

                        official.setAddress(address);
                    }

                    if (officialObj.has("party"))
                        official.setParty(officialObj.getString("party"));
                    if (officialObj.has("phones"))
                        official.setPhone(officialObj.getJSONArray("phones").getString(0));
                    if (officialObj.has("urls"))
                        official.setUrl(officialObj.getJSONArray("urls").getString(0));
                    if (officialObj.has("emails"))
                        official.setEmail(officialObj.getJSONArray("emails").getString(0));

                    if (officialObj.has("channels")) {
                        SocialMedia socialMedia = new SocialMedia();

                        JSONArray channels = officialObj.getJSONArray("channels");
                        for (int k = 0; k < channels.length(); k++) {
                            JSONObject jChannel = channels.getJSONObject(k);
                            if (jChannel.getString("type").equals("Facebook"))
                                socialMedia.setFacebook(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Twitter"))
                                socialMedia.setTwitter(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("YouTube"))
                                socialMedia.setYouTube(jChannel.getString("id"));
                        }
                        official.setSocialMedia(socialMedia);
                    }
                    if (officialObj.has("photoUrl"))
                        official.setPhoto(officialObj.getString("photoUrl"));
                    Log.d(TAG, "process: " + official.toString());

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                mainActivity.setLocation(normalizedInput.getString("city"), normalizedInput.getString("state"), normalizedInput.get("zip").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mainActivity.updateOfficials(official);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
