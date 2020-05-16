package tz.co.nyotaapps.housekeeper.housekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;



import com.facebook.drawee.backends.pipeline.Fresco;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.RequestHandler;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import static tz.co.nyotaapps.housekeeper.housekeeper.MyAdapter.dialog;
import static tz.co.nyotaapps.housekeeper.housekeeper.splash.USERPRESENT;



public class MainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;

    //ArrayList<Profile> list = new ArrayList<Profile>();
    private ArrayList<Profile> list = new ArrayList<>();
    AlertDialog alertDialog;
    MyAdapter adapter;
    public AndroidMultiPartEntity entity3;

    public String responseString=null;
    public String responseString2=null;
    private LinearLayoutManager mLayoutManager;

    ///////NOTIFICATIONS////

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;


    ///////////USER///////////
    public String name;
    //public String userid;
    public String CURRENTUSER_IDX;
    public boolean SKIPNOTIFICATION;
    public String GENDERX = "300";
    public String SEARCHKEYWORD = "100";

    //////////////ADS/////////////

    private final String TAG = MainActivity.class.getSimpleName();

    private String WAIT = null;


    public static String SELECTEDUSER = "0";
    public static String SELECTEDUSERNAME = "0";
    public static String EMPLOYERNAME = "0";
    public static String EMPLOYEREMAIL = "0";
    private RewardedVideoAd mRewardedVideoAd;
    public AdLoader adLoader;

    private UnifiedNativeAd nativeAd;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    public static InterstitialAd interstitialAd;
    private AdView adView;

    public static InterstitialAd mInterstitialAd;
    public static boolean isAppRunning;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);



        alertDialog = new AlertDialog.Builder(MainActivity.this).create(); // Read

        final Button button = findViewById(R.id.jiungex);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent myIntent = new Intent(MainActivity.this, Register.class);
                //myIntent.putExtra("SEARCHKEYWORD", ""); //Optional parameters
                //myIntent.putExtra("GENDERX", "300"); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
        final Button buttonxx = findViewById(R.id.fooButonX);
        buttonxx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                myIntent.putExtra("SEARCHKEYWORD", ""); //Optional parameters
                myIntent.putExtra("GENDERX", "300"); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });



        Fresco.initialize(this);





        if(USERPRESENT){


            String currentuser ="currentuser";

            SQLiteDatabase myDB = this.openOrCreateDatabase("housekeeper", MODE_PRIVATE, null);
            /*retrieve data from database */
            Cursor c = myDB.rawQuery("SELECT * FROM " + currentuser , null);
            Log.e("DATABASE count",  Integer.toString(c.getColumnCount()));
            Log.e("DATABASE count2",  Integer.toString(c.getCount()));

            if (c == null || c.getColumnCount() == 0 || c.getCount() == 0){

                Log.e("DATABASE", "NO USER");
                //Intent myIntent = new Intent(MainActivity.this, splash.class);
                //myIntent.putExtra("key", 100); //Optional parameters
                //MainActivity.this.startActivity(myIntent);
            }
            else{

                Log.e("DATABASEGGGGGG", c.toString());



                int Column1 = c.getColumnIndex("name");
                int Column2 = c.getColumnIndex("idx");

                c.moveToFirst();
                if (c != null) {
                    // Loop through all Results

                    //CURRENTUSER_NAME = c.getString(Column1);
                    CURRENTUSER_IDX  = c.getString(Column2);

                }

            }



            androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
            ImageView lt = toolbar.findViewById( R.id.ingiax );
            lt.setVisibility(View.VISIBLE);

            lt.setOnClickListener(new View.OnClickListener(){
                @Override
                //On click function
                public void onClick(View view) {
                    //Create the intent to start another activity
                    Intent intent = new Intent(view.getContext(), UserProfile.class);
                    startActivity(intent);
                }
            });

            //new CheckNewMeseji().execute();

        }else{

            androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
            Button lt = toolbar.findViewById( R.id.jiungex);
            lt.setVisibility(View.VISIBLE);




        }

       /* Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if(extras.getString("SEARCHKEYWORD").equalsIgnoreCase("100")){

            }else{
                SEARCHKEYWORD = extras.getString("SEARCHKEYWORD");
                GENDERX = extras.getString("GENDERX");
            }



        }*/


        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(MainActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        MobileAds.initialize(this, "ca-app-pub-4482019772887748~8213515294");
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4482019772887748/8285018008");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4482019772887748/8509213436");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        //FirebaseMessaging.getInstance().subscribeToTopic("all");





        WAIT = "ok";
        new watu().execute();
        ProgressBar oo =  findViewById(R.id.progressBar1xxc);
        oo.setVisibility(View.GONE);


    }








    private class watu extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {



            try{

                entity3 = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                                //progressDialog.setMessage("Uploading IMAGE..." +progress + " %"); // Setting Message
                            }
                        });

                entity3.addPart("country", new StringBody(splash.country));
                entity3.addPart("city", new StringBody(splash.city));
                entity3.addPart("countrycode", new StringBody(splash.countryCode));
                entity3.addPart("regionname", new StringBody(splash.regionName));
                entity3.addPart("gender", new StringBody(GENDERX));
                entity3.addPart("key", new StringBody(SEARCHKEYWORD));



            }catch (UnsupportedEncodingException ep){

                Log.e("UnsupportedEncodingExce", "error: " + ep);

            }








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

            String FILE_UPLOAD_URLXX = "https://zimaapps.com/mobileApps/housekeepers/getwatu.php";
            //String FILE_UPLOAD_URLXX = "http://192.168.103.129:3000/upload";
            String responseString = null;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_URLXX);

            try{
                URI website = new URI(FILE_UPLOAD_URLXX);
                httppost.setURI(website);
            }catch (URISyntaxException ex){
                Log.e("URISyntaxException", "error: " + ex);
            }



            try {


                httppost.setEntity(entity3);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                System.out.println("executing request " + httppost.getRequestLine());
                System.out.println("Now uploading your file ");

                //publishProgress(params.);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    //haso = responseString;

                } else {

                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //createdialog("ERROR DETECTED","Please rechack your data if its correct and try again. "+ statusCode,3);

                            Log.e("ClientProtocolException", "ERROR from server: ");
                        }
                    });


                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("ClientProtocolException", "ERROR from server: " + responseString);
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("IOException", "ERROR from server: " + responseString);
            }

            return responseString;



        }

        @Override
        protected void onPostExecute(String result) {
            if(result !=null) {
                Log.e("UPLOADING DDDDDDD", "Response from server: " + result);
                LinearLayout vog = findViewById(R.id.matokeox);
                vog.setVisibility(View.GONE);
                if (result.equalsIgnoreCase("700")) {
                    LinearLayout bvg = findViewById(R.id.matokeo);
                    bvg.setVisibility(View.VISIBLE);
                    TextView bbn = findViewById(R.id.nodata);
                    bbn.setVisibility(View.VISIBLE);
                    ProgressBar bbgv = findViewById(R.id.progressBar1xxc);
                    bbgv.setVisibility(View.GONE);
                    //Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                } else {

                    LinearLayout bvg = findViewById(R.id.matokeo);
                    bvg.setVisibility(View.GONE);

                    Log.e("UPLOADING DDDDDDD1", "Response from server: " + result);
                    try {
                        //SQLiteDatabase myDB = MainActivity.this.openOrCreateDatabase("Mchumba", MODE_PRIVATE, null);
                        //myDB.execSQL("DROP TABLE IF EXISTS " + currentuser);

                        JSONObject obj = new JSONObject(result);
                        JSONArray jsonArray = obj.getJSONArray("wadada");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject explrObject = jsonArray.getJSONObject(i); // you will get the json object

                            String idx = explrObject.getString("idx");
                            String name = explrObject.getString("Mtangazaji");
                            String umri = explrObject.getString("umri");
                            String area = explrObject.getString("Mtaa");
                            String email = "no value";
                            String maelezo = explrObject.getString("Maelezo1");
                            String gender = explrObject.getString("gender");
                            String phone = explrObject.getString("Namba");
                            String picha = explrObject.getString("Image");
                            String love = explrObject.getString("country");
                            //Log.e("UPLOADING RRRRR31", "Response from server: " + phone);

                            //new Profile(idx,name,umri,area,email,maelezo,gender,phone,picha);
                            list.add(new Profile(idx, name, umri, area, email, maelezo, gender, phone, picha, love));

                            //Log.e("INDIVIDUAL OBJECT", "Object: " + name.toString());
                            //Log.e("INDIVIDUAL PICHA", "Object: " + picha.toString());
                        }

                        //loadNativeAds();

                        //addBannerAds();

                        adapter = new MyAdapter(MainActivity.this, list, mNativeAds, MainActivity.this);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }else{
                LinearLayout vog = findViewById(R.id.matokeox);
                vog.setVisibility(View.VISIBLE);

            }
            super.onPostExecute(result);
        }

    }








    public void setgendermale(View v){

        GENDERX = "100";
    }

    public void setgenderfemale(View v){

        GENDERX = "200";
    }

    public void setgenderwote(View v){

        GENDERX = "300";
    }

    public void searchpeople(View v){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.searchdialog);

        RadioGroup mOption = (RadioGroup) dialog.findViewById(R.id.option);
        if(GENDERX.equalsIgnoreCase("100")){
            mOption.check(R.id.thirdx);
        }else if(GENDERX.equalsIgnoreCase("200")){
            mOption.check(R.id.secondx);
        }else{
            mOption.check(R.id.firstx);
        }
        TextView text = (TextView) dialog.findViewById(R.id.keyword);
        text.setText(SEARCHKEYWORD);

        Button dialogButton = (Button) dialog.findViewById(R.id.addsubject);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView text = (TextView) dialog.findViewById(R.id.keyword);
                String thekey = text.getText().toString();

                SEARCHKEYWORD = thekey;
                dialog.dismiss();

                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                myIntent.putExtra("SEARCHKEYWORD", SEARCHKEYWORD); //Optional parameters
                myIntent.putExtra("GENDERX", GENDERX); //Optional parameters
                MainActivity.this.startActivity(myIntent);


            }
        });

        Button dialogButtonx = (Button) dialog.findViewById(R.id.closex);
        dialogButtonx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();
    }











    private static String encodeParams(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }







    public void createdialog(String thetitle, String themeseji){


        // Update
        alertDialog.setTitle(thetitle);
        alertDialog.setMessage(themeseji);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //progressDialog.dismiss();
                        alertDialog.dismiss();



                    }


                });

        alertDialog.show();
    }







    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            finishAffinity();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
       // Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }














    public class FetchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            createdialog("PLEASE WAIT","SENDING "+SELECTEDUSERNAME+" CV TO "+EMPLOYERNAME+" "+EMPLOYEREMAIL);

            //createdialog("SENDING "+SELECTEDUSERNAME+" CV TO "+EMPLOYERNAME+" "+EMPLOYEREMAIL +"Please wait ",3);

            Log.e("APPODEAL GETTING MAIL", SELECTEDUSER);

            // setting progress bar to zero
            //progressBar.setProgress(0);
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("MAKESURE CALLEDDDDDD", SELECTEDUSER);
                Log.e("MAKESURE CALLEDDDDDD", SELECTEDUSER);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://zimaapps.com/mobileApps/housekeepers/getcv.php");

                // Add your data

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("SELECTEDUSER", SELECTEDUSER));
                nameValuePairs.add(new BasicNameValuePair("SELECTEDUSERNAME", SELECTEDUSERNAME));
                nameValuePairs.add(new BasicNameValuePair("EMPLOYERNAME", EMPLOYERNAME));
                nameValuePairs.add(new BasicNameValuePair("EMPLOYEREMAIL", EMPLOYEREMAIL));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                String result11 = sb.toString();

                // parsing data
                return result11;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null && !result.isEmpty()){

                //Log.e("EMAIL SERVER RESULT", result + " / ");
                createdialog("SUCCESS","CV has been sent to "+EMPLOYEREMAIL+". You may need to check the spam folder, if you can't find the email");

            }else{
                createdialog("INCORRECT DATA","CV was not sent, please try again.");
            }

        }
    }







}
