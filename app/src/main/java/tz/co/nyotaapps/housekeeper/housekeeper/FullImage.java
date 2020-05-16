package tz.co.nyotaapps.housekeeper.housekeeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FullImage extends AppCompatActivity {
    private AdView mAdView;
    private String responseString2 = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setBackgroundDrawable(new ColorDrawable(0xff00DDED));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        //toolbar.setTitleTextColor(Color.BLACK);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        MobileAds.initialize(this, "ca-app-pub-4482019772887748~8213515294");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        final String url = i.getExtras().getString("url");
        Log.e("IMAGE URL", "NO WAY URL"+ url);
        final ImageView imageView = (ImageView) findViewById(R.id.header);
        //Picasso.get().load(url).placeholder(R.drawable.placeholder).fit().into(imageView);


        //Picasso.get().load("http://mchumba.nyotaapps.co.tz/"+url).placeholder(R.drawable.placeholder).fit().into(zz);
        // We need to adjust the height if the width of the bitmap is
        // smaller than the view width, otherwise the image will be boxed.

        imageView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    // Wait until layout to call Picasso
                    @Override
                    public void onGlobalLayout() {
                        // Ensure we call this only once
                        imageView.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);


                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.placeholder)
                                .resize(0, imageView.getHeight())
                                .into(imageView);
                    }
                });

    }


    public void deleteimage(View v){

        //new SetMesejiStatus().execute();

    }



    /**
     * Uploading the file to server
     * */
    private class SetMesejiStatus extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {


        }

        @Override
        protected String doInBackground(Void... params) {

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            String CREATEACCOUNT = "http://mchumba.nyotaapps.co.tz/deleteimg.php?idx=";

            Log.e("MESAJE STATUS", CREATEACCOUNT);


            while (responseString2==null){

                try{

                    URL urlForGetRequest = new URL(CREATEACCOUNT);
                    String readLine = null;
                    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
                    conection.setRequestMethod("GET");
                    conection.setUseCaches(false);
                    conection.setDefaultUseCaches(false);
                    conection.setRequestProperty("Cache-Control", "no-cache");
                    conection.setRequestProperty("Cache-Control", "no-store");
                    int responseCode = conection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(conection.getInputStream()));
                        StringBuffer response = new StringBuffer();
                        while ((readLine = in .readLine()) != null) {
                            response.append(readLine);
                        } in .close();
                        // print result
                        responseString2 = response.toString();
                        System.out.println("JSON String Result " + response.toString());


                    } else {

                        System.out.println("SERVER RESPO" + conection.getErrorStream().toString());

                    }
                }catch (MalformedURLException b){}catch (ProtocolException n){}catch (IOException h){}






                break;
            }



            return responseString2;



        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("SET NEW MESEJI", "Response from server: " + result);

            super.onPostExecute(result);
        }

    }



    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




}