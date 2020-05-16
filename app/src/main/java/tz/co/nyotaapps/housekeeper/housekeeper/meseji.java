package tz.co.nyotaapps.housekeeper.housekeeper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class meseji extends AppCompatActivity {
    ///////NOTIFICATIONS////
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";


    String userid;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    String TAG = "FIREBASE";
    String CURRENTUSER_IDX;
    String CURRENTUSER_NAME;
    boolean yeye;
    String sendername;

    public String responseString2=null;
    public String responseString=null;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meseji);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#24292E"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);



        String currentuser ="currentuser";

        SQLiteDatabase myDB = this.openOrCreateDatabase("housekeeper", MODE_PRIVATE, null);
        /*retrieve data from database */
        Cursor c = myDB.rawQuery("SELECT * FROM " + currentuser , null);
        Log.e("DATABASE count",  Integer.toString(c.getColumnCount()));
        Log.e("DATABASE count2",  Integer.toString(c.getCount()));

        if (c == null || c.getColumnCount() == 0 || c.getCount() == 0){

            Log.e("DATABASE", "NO USER");
            Intent myIntent = new Intent(meseji.this, splash.class);
            myIntent.putExtra("key", 100); //Optional parameters
            meseji.this.startActivity(myIntent);
        }
        else{

            Log.e("DATABASEGGGGGG", c.toString());



            int Column1 = c.getColumnIndex("name");
            int Column2 = c.getColumnIndex("idx");

            c.moveToFirst();
            if (c != null) {
                // Loop through all Results

                CURRENTUSER_NAME = c.getString(Column1);
                CURRENTUSER_IDX  = c.getString(Column2);

            }

        }



        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userid = extras.getString("userid");
            sendername = extras.getString("sendername");
            Log.e("THE IDX", userid+" CURRENTUSER_IDX "+CURRENTUSER_IDX);
            Log.e("THE NAME", userid+" SENDING NAME "+sendername);
            //androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);


            TextView lt = toolbar.findViewById( R.id.newtitle );
            lt.setText(sendername);


        }



        final ScrollView scroll = findViewById( R.id.mesejiscroll );

        scroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        },100);

        com.google.android.material.textfield.TextInputEditText jox = findViewById( R.id.themesijiinput );


        jox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final ScrollView scroll = findViewById( R.id.mesejiscroll );

                scroll.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                },100);
                return false;
            }


        });
        startmesejilisterner();

       // new signin2().execute();

        MobileAds.initialize(this, "ca-app-pub-4482019772887748~8213515294");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }








    public void  sendmeseji(View v){
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm dd/MM");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        EditText jjp = findViewById(R.id.themesijiinput);
        String begonemeseji = jjp.getText().toString();

        if(begonemeseji.equalsIgnoreCase("")){}
        else {


            mDatabase = FirebaseDatabase.getInstance().getReference("messages/" + CURRENTUSER_IDX + "/");

            String key = mDatabase.child("messages/").push().getKey();

            HashMap<String, Object> postValues = new HashMap<String, Object>();
            // Mapping string values to int keys
            postValues.put("name", CURRENTUSER_IDX);
            postValues.put("text", begonemeseji);
            postValues.put("sendername", CURRENTUSER_NAME);
            postValues.put("sendeename", sendername);
            postValues.put("new", "one");
            postValues.put("time", sdf.format(timestamp));
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(userid + "/" + key, postValues);
            //childUpdates.put("messages/"+CURRENTUSER_IDX+"/"+userid+"/" + key, postValues);


            mDatabase.updateChildren(childUpdates);


            mDatabase2 = FirebaseDatabase.getInstance().getReference("messages/" + userid + "/");

            String key2 = mDatabase2.child("messages/").push().getKey();

            HashMap<String, Object> postValues2 = new HashMap<String, Object>();
            // Mapping string values to int keys
            postValues2.put("name", CURRENTUSER_IDX);
            postValues2.put("text", begonemeseji);
            postValues2.put("sendername", CURRENTUSER_NAME);
            postValues.put("sendeename", sendername);
            postValues2.put("new", "one");
            postValues2.put("time", sdf.format(timestamp));
            Map<String, Object> childUpdates2 = new HashMap<>();
            childUpdates2.put(CURRENTUSER_IDX + "/" + key2, postValues2);
            //childUpdates.put("messages/"+CURRENTUSER_IDX+"/"+userid+"/" + key, postValues);


            mDatabase2.updateChildren(childUpdates2);

           final ScrollView scroll = findViewById(R.id.mesejiscroll);

            scroll.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 100);

            com.google.android.material.textfield.TextInputEditText jox = findViewById(R.id.themesijiinput);
            jox.setText("");

            new SetMesejiStatus().execute();

        }

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


            String CREATEACCOUNT = "http://housekeepers.world/checknewmsj.php?idx="+CURRENTUSER_IDX+"&notify=100";

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




    public void startmesejilisterner(){



        mDatabase = FirebaseDatabase.getInstance().getReference("messages/"+CURRENTUSER_IDX+"/"+userid+"/");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("UPLOADING onChildAdded", "onChildAdded ");


                LinearLayout lt = (LinearLayout) findViewById( R.id.mesejidisplay );
                View tv = getLayoutInflater().inflate(R.layout.my_meseji, null);
                View tv2 = getLayoutInflater().inflate(R.layout.there_meseji, null);



                GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(t);
                //listRes.toString();
                Log.e("DATAA", map.toString());

                // using for-each loop for iteration over Map.entrySet()
                for (Map.Entry<String,String> entry : map.entrySet()){






                    if(entry.getKey().equalsIgnoreCase("name")){
                        if(entry.getValue().equals(CURRENTUSER_IDX)){
                            yeye =false;

                        }else{
                            yeye= true;


                        }

                    }else if(entry.getKey().equalsIgnoreCase("text")){

                        if(yeye){

                            TextView ggh = tv2.findViewById( R.id.textholder );
                            ggh.setText(entry.getValue());


                        }else{

                            TextView ggh = tv.findViewById( R.id.textholder );
                            ggh.setText(entry.getValue());


                        }

                    }else if(entry.getKey().equalsIgnoreCase("sendername")){

                        if(yeye){

                        }else{



                        }

                    }else if(entry.getKey().equalsIgnoreCase("new")){

                        if(yeye){

                        }else{



                        }

                    }else if(entry.getKey().equalsIgnoreCase("time")){

                        if(yeye){

                        }else{



                        }




                    }

                    if(yeye){
                        if(tv2.getParent() != null) {
                            ((ViewGroup)tv2.getParent()).removeView(tv2); // <- fix
                        }
                        TextView ggh = tv2.findViewById( R.id.textholder );
                        if(ggh.getText().toString().equalsIgnoreCase("")){}
                        else {
                            lt.addView(tv2);
                        }


                    }else{
                        if(tv.getParent() != null) {
                            ((ViewGroup)tv.getParent()).removeView(tv); // <- fix
                        }
                        TextView ggh = tv.findViewById( R.id.textholder );
                        if(ggh.getText().toString().equalsIgnoreCase("")){}
                        else {
                            lt.addView(tv);
                        }



                    }


                    final ScrollView scroll = findViewById( R.id.mesejiscroll );

                    scroll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },100);









                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("UPLOADI onChildChanged", "onChildChanged ");
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {

                    Log.e("UPLO onChildChanged key", locationSnapshot.getKey().toString());

                    Log.e("UPL onChildChanged text",  locationSnapshot.getValue().toString());



                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("UPLOADI onChildRemoved", "onChildRemoved ");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("UPLOADI onChildMoved", "onChildMoved ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("UPLOADI onCancelled", "onCancelled ");
            }
        });



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
