package tz.co.nyotaapps.housekeeper.housekeeper;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


public class splash extends AppCompatActivity {

    public static int MESSAGESPRESENT = 0;
    public static boolean USERPRESENT = false;
    public static Context context;
    public JSONObject sendersx;
    public static String country_name = null;
    public static String IPaddress = "";
    public Boolean IPValue;
    public static String city = "";
    public static String country = "";
    public static String countryCode ="";
    public static String regionName = "";
    public String responseString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //getSupportActionBar().hide();
        ///getActionBar().hide();

        FirebaseApp.initializeApp(this);

        context = splash.this;

        sendersx = new JSONObject();


        if (checkinternet()) {

            CreateTables ();
            new getlocation().execute();

            if (getIntent().getExtras() != null) {

                MESSAGESPRESENT = 1;

            }

        } else {
            ProgressBar vog = findViewById(R.id.progressBar1xxc);
            vog.setVisibility(View.GONE);

            TextView vogj = findViewById(R.id.checkinta);
            vogj.setVisibility(View.VISIBLE);
        }





    }


    public static String getPublicIP() throws IOException
    {
        Document doc = Jsoup.connect("http://www.checkip.org").get();
        return doc.getElementById("yourip").select("h1").first().select("span").text();
    }


    public void CreateTables (){

        String currentuser ="currentuser";
        String users ="users";

        SQLiteDatabase myDB = this.openOrCreateDatabase("housekeeper", MODE_PRIVATE, null);
        //myDB.execSQL("DROP TABLE IF EXISTS " + currentuser);
        //myDB.execSQL("DROP TABLE IF EXISTS " + users);
        /* Create a Table in the Database. */

        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + users + " (id VARCHAR, idx VARCHAR,name VARCHAR," +
                " emailxx VARCHAR,phone VARCHAR, love VARCHAR,gender VARCHAR, picha VARCHAR,umri VARCHAR," +
                " area VARCHAR,maelezo VARCHAR, country VARCHAR, uniqueID VARCHAR);");


        //myDB.execSQL("CREATE TABLE IF NOT EXISTS " + currentuser + " (phone VARCHAR, idx VARCHAR, name VARCHAR, umri VARCHAR, area VARCHAR, email VARCHAR, maelezo VARCHAR, gender VARCHAR);");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + currentuser + " (phone VARCHAR, idx VARCHAR, name VARCHAR, umri VARCHAR, area VARCHAR, email VARCHAR, maelezo VARCHAR, gender VARCHAR, picha VARCHAR, love VARCHAR, uniqueID VARCHAR);");




    }


    private class forceUpdate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {


            PackageManager packageManager = splash.this.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String currentVersion = packageInfo.versionName;

            try{

                URL urlForGetRequest = new URL("http://zimaapps.com/mobileApps/housekeepers/version.php");
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

                    if (Objects.equals(response.toString(), new String(currentVersion)))
                    {
                        isuserpresent();


                    }else {

                        showForceUpdateDialog(response.toString(),currentVersion);

                        System.out.println("JSON String Result " + response.toString());
                    }


                } else {
                    Intent myIntent = new Intent(splash.this, splash.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    splash.this.startActivity(myIntent);

                    System.out.println("GET NOT WORKED");
                }
            }catch (MalformedURLException b){}catch (ProtocolException n){}catch (IOException h){}





            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }
    }


    public  void isuserpresent(){

        String currentuser ="currentuser";

        SQLiteDatabase myDB = this.openOrCreateDatabase("housekeeper", MODE_PRIVATE, null);
        /*retrieve data from database */
        Cursor c = myDB.rawQuery("SELECT * FROM " + currentuser , null);
        Log.e("DATABASE count",  Integer.toString(c.getColumnCount()));
        Log.e("DATABASE count2",  Integer.toString(c.getCount()));

        if (c == null || c.getColumnCount() == 0 || c.getCount() == 0){

            Log.e("DATABASE", ">>USER IS NOOOT PRESENT 1");

            USERPRESENT = false;
            gotomain();

            //Intent myIntent = new Intent(splash.this, Login.class);
            //myIntent.putExtra("key", value); //Optional parameters
            //splash.this.startActivity(myIntent);
        }
        else{

            Log.e("DATABASE", c.toString());



            // Check if our result was valid.
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results


                do {
                    Log.e("DATABASE", ">>USER IS PRESENT");

                }while(c.moveToNext());
                USERPRESENT = true;
                gotomain();

            }else {
                Log.e("DATABASE", ">>USER IS NOOOT PRESENT");
            }

        }


    }




    public void showForceUpdateDialog(String latestVersion, String currentVersion){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.MyAlertDialogStyle));

        alertDialogBuilder.setTitle(context.getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(context.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + " " +context.getString(R.string.youAreNotUpdatedMessage1));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                gotomain();
                //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.cancel();
            }
        });

        runOnUiThread(new Runnable() {
            public void run()
            {
                alertDialogBuilder.show();
            }
        });

    }



    public boolean checkinternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;

        }


        return connected;


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
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




    public void gotomain(){
        Intent myIntent = new Intent(splash.this, MainActivity.class);
        myIntent.putExtra("key", 100); //Optional parameters
        splash.this.startActivity(myIntent);

    }



    /**
     * Uploading the file to server
     * */
    private class getlocation extends AsyncTask<Void, Integer, String> {
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

            String thez = "";

        try {
            thez = getPublicIP();
            Log.e("PUBLIC IP", "Response from server: " + thez);
        }catch (IOException x){

        }


            String CREATEACCOUNT = "http://ip-api.com/json/"+thez+"";

            //String CREATEACCOUNT = "https://freegeoip.net/json/"+thez+"";

            //String CREATEACCOUNT = "http://acb.nyotaapps.co.tz/login.php?username="+sysuser+"&inputPassword="+Password+"";

            while (responseString==null){

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
                        responseString = response.toString();
                        System.out.println("JSON String Result " + response.toString());


                    } else {

                        //System.out.println("SERVER RESPO" + conection.getErrorStream().toString());

                    }
                }catch (MalformedURLException b){}catch (ProtocolException n){}catch (IOException h){}






                break;
            }



            return responseString;



        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("UPLOADING DDDDDDD", "Response from server: " + result);

            if(result==null){
                //Toast.makeText(splash.this, "user not found", Toast.LENGTH_LONG).show();
                //new forceUpdate().execute();
                isuserpresent();
            }
            else{

                //Log.e("UPLOADING DDDDDDD1", "Response from server: " + result);
                try {


                        JSONObject obj = new JSONObject(result);


                        city = obj.getString("city");
                        country = obj.getString("country");
                        countryCode = obj.getString("countryCode");
                        regionName = obj.getString("regionName");

                        //new forceUpdate().execute();
                        isuserpresent();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            super.onPostExecute(result);
        }

    }






}









