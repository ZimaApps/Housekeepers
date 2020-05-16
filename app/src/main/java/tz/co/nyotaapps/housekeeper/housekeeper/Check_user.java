package tz.co.nyotaapps.housekeeper.housekeeper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import static tz.co.nyotaapps.housekeeper.housekeeper.splash.USERPRESENT;


public class Check_user extends AppCompatActivity {

    GridView mGrid;
    MainGridAdapter mAdapter;
    ArrayList<mObject> mArray = new ArrayList<>();
    String userid;
    String responseString=null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    String TAG = "FIREBASE";
    String CURRENTUSER_IDX;
    boolean yeye;
    String name;

    ///////NOTIFICATIONS////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setTitleTextColor(Color.BLACK);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#24292E"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //getaccounts();

        Bundle extras = getIntent().getExtras();
        String userName;

        if (extras != null) {
           name = extras.getString("name");
            userid = extras.getString("userid");
            String area = extras.getString("area");
            String namba = extras.getString("namba");
            String maelezo = extras.getString("maelezo");
            String umri = extras.getString("umri");
            String love = extras.getString("love");
            final String url = extras.getString("url");
            String mylink = extras.getString("picha");
            // and get whatever type user account id is

            toolbar.setTitle(name);
            this.setTitle(name);

            TextView v = findViewById(R.id.jina);
            v.setText(name);
            TextView v1 = findViewById(R.id.umri);
            v1.setText(umri);
            TextView v2 = findViewById(R.id.nambax);
            v2.setText(namba);
            TextView v3 = findViewById(R.id.eneox);
            v3.setText(area);
            TextView v4 = findViewById(R.id.maelezoz);
            v4.setText(maelezo);
            final ImageView zz = findViewById(R.id.header);


            //Picasso.get().load("http://mchumba.nyotaapps.co.tz/"+url).placeholder(R.drawable.placeholder).fit().into(zz);
            // We need to adjust the height if the width of the bitmap is
            // smaller than the view width, otherwise the image will be boxed.

            zz.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        // Wait until layout to call Picasso
                        @Override
                        public void onGlobalLayout() {
                            // Ensure we call this only once
                            zz.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);


                            Picasso.get()
                                    .load("http://housekeepers.world/images/"+url)
                                    .placeholder(R.drawable.placeholder)
                                    .resize(0, zz.getHeight())
                                    .into(zz);
                        }
                    });



            Log.e("THE LINKII", "http://housekeepers.world/images/"+url);

            new getpicha().execute();
        }


        mGrid = (GridView) findViewById(R.id.main_gridview);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 1) {

                    Log.e("CLICKED", "ONE CLICKED");
                    seeimages();


                } else {
                    Log.e("CLICKED", "OTHER CLICKED");

                    seeinfo();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Appodeal.setTesting(true);



        NativeAds();
    }


    public void NativeAds(){



        }


    /**
     * Uploading the file to server
     * */
    private class getpicha extends AsyncTask<Void, Integer, String> {
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

            String CREATEACCOUNT = "http://housekeepers.world/getpicha.php?idx="+userid;

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
                        Log.e("UPLOADING ZZZZZ", "Response from server: " + responseString);


                    } else {
                        Log.e("UPLOADING ZZZZZ", "Response from server: " + conection.getErrorStream().toString());
                        System.out.println("SERVER RESPO" + conection.getErrorStream().toString());

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
                //Toast.makeText(Check_user.this, "user not found", Toast.LENGTH_LONG).show();
            }
            else{

                Log.e("UPLOADING DDDDDDD1", "Response from server: " + result);
                try {


                    JSONObject obj = new JSONObject(result);
                    JSONArray jsonArray = obj.getJSONArray("images");
                    mArray.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i); // you will get the json object

                        String image = explrObject.getString("image");

                        mArray.add(new mObject("", "http://housekeepers.world/"+image));

                        Log.e("INDIVIDUAL OBJECT", "Object: " + image.toString());
                    }




                    DisplayMetrics dm = new DisplayMetrics();
                    Check_user.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int size = dm.widthPixels / 3;
                    //CustomAdapter adapter = new CustomAdapter(list, size, getActivity());
                    mAdapter = new MainGridAdapter(getApplicationContext(),mArray,size);
                    mGrid.setAdapter(mAdapter);

                    /**
                     * On Click event for Single Gridview Item
                     * */
                    mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {

                            // Sending image id to FullScreenActivity
                            Intent i = new Intent(getApplicationContext(), FullImage.class);
                            // passing array index
                            i.putExtra("id", position);
                            startActivity(i);
                        }

                    });



                    //ProgressBar oo =  findViewById(R.id.progressBar1xxc);
                    //oo.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            super.onPostExecute(result);
        }

    }





    public void seeinfo(){
        System.out.println("JSON String Result called");
        LinearLayout xx = (LinearLayout) findViewById(R.id.theinfo);
        xx.setVisibility(View.VISIBLE);
        LinearLayout xxa = (LinearLayout) findViewById(R.id.theimages);
        xxa.setVisibility(View.GONE);
        FloatingActionButton cvf = findViewById(R.id.floating_actionk_button);
        cvf.show();


    }
    public void seeimages(){
        System.out.println("JSON String Result called2");
        LinearLayout xx = (LinearLayout) findViewById(R.id.theinfo);
        xx.setVisibility(View.GONE);
        LinearLayout xxa = (LinearLayout) findViewById(R.id.theimages);
        xxa.setVisibility(View.VISIBLE);
        FloatingActionButton cvf = findViewById(R.id.floating_actionk_button);
        cvf.hide();



    }





    public void createMesaji(View v){

        if(USERPRESENT) {



            Intent myIntent = new Intent(Check_user.this, meseji.class);
            myIntent.putExtra("userid", userid); //Optional parameters
            myIntent.putExtra("sendername", name); //Optional parameters
            Check_user.this.startActivity(myIntent);

        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyActivity.this, R.style.MyDialogTheme);

            builder.setTitle("Registration is needed");
            builder.setMessage("If you wish to Chat with this individual, Please register. Otherwise, Call Her/His Number");

            builder.setPositiveButton("REGISTER/SIGN IN", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    dialog.dismiss();

                    Intent myIntent = new Intent(Check_user.this, Register.class);
                    myIntent.putExtra("userid", userid); //Optional parameters
                    myIntent.putExtra("sendername", name); //Optional parameters
                    Check_user.this.startActivity(myIntent);
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
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
