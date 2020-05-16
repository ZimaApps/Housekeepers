package tz.co.nyotaapps.housekeeper.housekeeper;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.AppBarLayout;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
        import android.util.DisplayMetrics;
        import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
        import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.GridView;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TabHost;
        import android.widget.TextView;
        import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


        import java.io.BufferedInputStream;
        import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
        import java.net.URLConnection;
        import java.nio.charset.Charset;
        import java.security.Timestamp;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
        import java.util.ListIterator;
        import java.util.Map;
        import java.util.Random;

import static androidx.core.content.FileProvider.getUriForFile;


public class UserProfile extends AppCompatActivity {

    GridView mGrid;
    MainGridAdapter mAdapter;
    ArrayList<mObject> mArray = new ArrayList<>();
    //String userid;
    String responseString=null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    String TAG = "FIREBASE";
    String CURRENTUSER_IDX;
    boolean yeye;
    String name;

    ///////NOTIFICATIONS////
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    /////////////IMAGE//////////

    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public AndroidMultiPartEntity entity3;
    public Bitmap BITMAPIMAGE;
    public File IMAGEFILE;


    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static String fileName;
    private ProgressDialog progressDialog;

    Map<String, Map> mesejiz = new HashMap<String, Map>();

    String THESENDERNAME = "";
    public String responseString2=null;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(Color.BLACK);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#24292E"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        //Fade out CollapsingToolbarLayout title ...from white to transparent
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        MobileAds.initialize(this, "ca-app-pub-4482019772887748~8213515294");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        String currentuser ="currentuser";

        SQLiteDatabase myDB = this.openOrCreateDatabase("housekeeper", MODE_PRIVATE, null);
        /*retrieve data from database */
        Cursor c = myDB.rawQuery("SELECT * FROM " + currentuser , null);
        Log.e("DATABASE count",  Integer.toString(c.getColumnCount()));
        Log.e("DATABASE count2",  Integer.toString(c.getCount()));

        if (c == null || c.getColumnCount() == 0 || c.getCount() == 0){

            Log.e("DATABASE", "NO USER");
            Intent myIntent = new Intent(UserProfile.this, splash.class);
            myIntent.putExtra("key", 100); //Optional parameters
            UserProfile.this.startActivity(myIntent);
        }
        else{

            Log.e("DATABASEGGGGGG", c.toString());



            int Column1 = c.getColumnIndex("name");
            int Column2 = c.getColumnIndex("idx");
            int Column3 = c.getColumnIndex("area");
            int Column4 = c.getColumnIndex("phone");
            int Column5 = c.getColumnIndex("maelezo");
            int Column6 = c.getColumnIndex("umri");
            //int Column7 = c.getColumnIndex("love");
            int Column8 = c.getColumnIndex("url");
            int Column9 = c.getColumnIndex("picha");


            c.moveToFirst();
            if (c != null) {
                // Loop through all Results

                    name = c.getString(Column1);
                    CURRENTUSER_IDX  = c.getString(Column2);
                    String area = c.getString(Column3);
                    String namba = c.getString(Column4);
                    String maelezo = c.getString(Column5);
                    String umri = c.getString(Column6);
                    //String love = c.getString(Column7);
                    String url = c.getString(Column9);
                    String mylink = c.getString(Column9);

                    //toolbar.setTitle(name);
                    //this.setTitle(name);
                collapsingToolbarLayout.setTitle(name);
                collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));
                collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));


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
                    ImageView zz = findViewById(R.id.header);

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

        }


        mGrid = (GridView) findViewById(R.id.main_gridview);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 1) {

                    Log.e("CLICKED", "ONE CLICKED");
                    FloatingActionButton bbc = findViewById(R.id.floating_action_button);
                    bbc.hide();

                    seechat();

                } else if (tab.getPosition() == 2) {

                    Log.e("CLICKED", "TWO CLICKED");
                    FloatingActionButton bbc = findViewById(R.id.floating_action_button);
                    bbc.hide();
                    seeinfo();

                } else {
                    Log.e("CLICKED", "OTHER CLICKED");
                    FloatingActionButton bbc = findViewById(R.id.floating_action_button);
                    bbc.show();
                    seeimages();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Map<String, String> namekeep = new HashMap<String, String>();

        mDatabase = FirebaseDatabase.getInstance().getReference("messages/"+CURRENTUSER_IDX+"/");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("USER IDX", "onChildAdded "+CURRENTUSER_IDX);
                String gumeseji ="";
                String gudate = "";
                String gunamex ="";



                Log.e("MAIL OBJECT ", dataSnapshot.getValue().toString());
                Log.e("MAIL OBJECT KEYx", dataSnapshot.getKey().toString());
                String MtumajiId = dataSnapshot.getKey().toString();



                LinearLayout parent = findViewById(R.id.mesejidisplay);
                LayoutInflater inflater = (LayoutInflater) UserProfile.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.meseji_layout, parent,false);
                TextView mesejiz = (TextView) convertView.findViewById(R.id.mesejiz);
                TextView namezText = (TextView) convertView.findViewById(R.id.namez);
                ImageView newz = (ImageView) convertView.findViewById(R.id.newz);
                TextView datez = (TextView) convertView.findViewById(R.id.datez);
                TextView usernameyo = (TextView) convertView.findViewById(R.id.usernameyo);

                if (dataSnapshot.hasChildren()) {
                    // The root node of the snapshot has children
                    long count = dataSnapshot.getChildrenCount();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        Log.e("MAIL OBJECT CHILD KEY", child.getKey().toString());
                        //Log.e("MAIL OBJECT CHILD VALUE", child.getValue(String.class).toString());
                        Log.e("MAIL OBJECT CHILD VALUE", child.getValue().toString());

                        Map<String, String> mapzz = (Map<String, String>) child.getValue();

                        Iterator it = mapzz.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();

                            if(pair.getKey().toString().equalsIgnoreCase("text")){
                                gumeseji= String.valueOf(pair.getValue());

                                Log.e("UP THEMESEJI", "Is the >>" + String.valueOf(pair.getValue()));

                            }else
                            if(pair.getKey().toString().equalsIgnoreCase("sendername")){
                                //usernameyo.setText(String.valueOf(pair.getValue()));
                                if(String.valueOf(pair.getValue()).equalsIgnoreCase(name)){
                                }else{
                                    namezText.setText(String.valueOf(pair.getValue()));
                                }

                                Log.e("UP THESENDERNAME", "Is the >>" + String.valueOf(pair.getValue()));

                            }else
                            if(pair.getKey().toString().equalsIgnoreCase("new")){
                                if(String.valueOf(pair.getValue()).equalsIgnoreCase("one")){
                                    newz.setVisibility(View.VISIBLE);
                                }
                                Log.e("UP THENEW", "Is the >>" + String.valueOf(pair.getValue()));

                            }else
                            if(pair.getKey().toString().equalsIgnoreCase("time")){
                                gudate = String.valueOf(pair.getValue());

                                Log.e("UP THETIME", "Is the >>" + String.valueOf(pair.getValue()));
                            }else
                                if(pair.getKey().toString().equalsIgnoreCase("sendeename")){
                                    if(namezText.getText().toString().equalsIgnoreCase("")){
                                        namezText.setText(String.valueOf(pair.getValue()));
                                        Log.e("UP NEW NAME", "Is the >>" + String.valueOf(pair.getValue()));
                                    }

                            }





                            Log.e("OBJECT CHILD RESULT", pair.getKey() + " = " + pair.getValue());
                            it.remove(); // avoids a ConcurrentModificationException
                        }

                    }
                }
            ////////////////////////////////////////////////////////////////////////////////////////////



                mesejiz.setText(gumeseji);
                datez.setText(gudate);
                com.facebook.drawee.view.SimpleDraweeView pichaz = (com.facebook.drawee.view.SimpleDraweeView) convertView.findViewById(R.id.pichaz);
                pichaz.setImageURI("http://housekeepers.world/getsenderImage.php?idx="+MtumajiId);


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent myIntent = new Intent(UserProfile.this, meseji.class);
                        myIntent.putExtra("userid", MtumajiId); //Optional parameters
                        myIntent.putExtra("sendername", namezText.getText().toString()); //Optional parameters
                        UserProfile.this.startActivity(myIntent);
                    }
                });

                parent.addView(convertView);

                convertView = null;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("WEWEEE", "onChildChanged ");
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

            String CREATEACCOUNT = "http://housekeepers.world/getpicha.php?idx="+CURRENTUSER_IDX;

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
                //Toast.makeText(UserProfile.this, "user not found", Toast.LENGTH_LONG).show();
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
                    UserProfile.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int size = dm.widthPixels / 3;
                    //CustomAdapter adapter = new CustomAdapter(list, size, getActivity());
                    mAdapter = new MainGridAdapter(getApplicationContext(),mArray,size);

                    mGrid.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    /**
                     * On Click event for Single Gridview Item
                     * */
                    mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            TextView getit = v.findViewById(R.id.url2);
                            String ooi = getit.getText().toString();
                            // Sending image id to FullScreenActivity
                            Intent i = new Intent(getApplicationContext(), FullImage.class);
                            // passing array index
                            i.putExtra("url", ooi);
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
        LinearLayout xxb = (LinearLayout) findViewById(R.id.thechat);
        xxb.setVisibility(View.GONE);

    }
    public void seeimages(){
        System.out.println("JSON String Result called2");
        LinearLayout xx = (LinearLayout) findViewById(R.id.theinfo);
        xx.setVisibility(View.GONE);
        LinearLayout xxa = (LinearLayout) findViewById(R.id.theimages);
        xxa.setVisibility(View.VISIBLE);
        LinearLayout xxb = (LinearLayout) findViewById(R.id.thechat);
        xxb.setVisibility(View.GONE);

    }
    public void seechat(){
        System.out.println("JSON String Result called3");
        LinearLayout xx = (LinearLayout) findViewById(R.id.theinfo);
        xx.setVisibility(View.GONE);
        LinearLayout xxa = (LinearLayout) findViewById(R.id.theimages);
        xxa.setVisibility(View.GONE);
        LinearLayout xxb = (LinearLayout) findViewById(R.id.thechat);
        xxb.setVisibility(View.VISIBLE);
        new SetMesejiStatusSeen().execute();
    }






    ///////////////////////////////////IMAGES//////////////////////////////////

    public void uploadimages(View v){

        getphoto();
    }


    public void getphoto(){


        try {



            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                fileName = System.currentTimeMillis() + ".jpg";

                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                                //File imageFile = new File(imageFilePath);
                                //picUri = Uri.fromFile(imageFile); // convert path to Uri
                                //takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT,  picUri );

                                //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();


            //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
            //File imageFile = new File(imageFilePath);
            //picUri = Uri.fromFile(imageFile); // convert path to Uri
            //takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT,  picUri );
            //startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

        } catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "your device doesn't support capturing images!";
            Toast.makeText(UserProfile.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    cropImage(getCacheImagePath(fileName));
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    cropImage(imageUri);
                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    handleUCropResult(data);
                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                Log.e(TAG, "Crop error: " + cropError);
                setResultCancelled();
                break;
            default:
                setResultCancelled();
        }
    }

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private void handleUCropResult(Intent data) {
        if (data == null) {
            setResultCancelled();
            return;
        }
        final Uri resultUri = UCrop.getOutput(data);
        setResultOk(resultUri);
    }

    private void setResultOk(Uri imagePath) {

        try{

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath);
            BITMAPIMAGE = bitmap;

            Log.e("OYAAA", "THE IMAGE: " + imagePath.toString());
            Log.e("OYAAA", "THE IMAGE: " + imagePath.toString());

            new fileFromBitmap().execute();

            AlertDialog.Builder alertadd = new AlertDialog.Builder(UserProfile.this);
            LayoutInflater factory = LayoutInflater.from(UserProfile.this);
            final View view = factory.inflate(R.layout.imagedialog, null);
            ImageView fcv = view.findViewById(R.id.dialog_imageview);
            fcv.setImageBitmap(bitmap);
            alertadd.setView(view);
            alertadd.setNeutralButton("UPLOAD", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dlg, int sumthin) {

                    new UploadImagetoserverY().execute();

                }
            });

            alertadd.show();

        }catch (FileNotFoundException rr){

            Log.e("FileNotFoundException", "THE FILE: " + rr.toString());
        }catch (IOException xx){
            Log.e("IOException", "THE FILE: " + xx.toString());
        }





    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(UserProfile.this, getPackageName() + ".fileprovider", image);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }







    public class fileFromBitmap extends AsyncTask<Void, Integer, String> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {


            try{
                //create a file to write bitmap data
                File f = new File(UserProfile.this.getCacheDir(), "housekeeper.jpg");
                f.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap = BITMAPIMAGE;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                IMAGEFILE = f;
                Log.e("the file", f.getAbsolutePath().toString());
                Log.e("the file",bitmapdata.toString() );

            }catch (IOException cc){


            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }






    private class UploadImagetoserverY extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            Log.e("CALLLEEEED", "CALLLEEEED : ");

            progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setMessage("Uploading Image..."); // Setting Message
            progressDialog.setTitle(""); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);


                entity3 = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                                //progressDialog.setMessage("Uploading IMAGE..." +progress + " %"); // Setting Message
                            }
                        });


                try{

                    entity3.addPart("idx", new StringBody(CURRENTUSER_IDX));
                    entity3.addPart("len", new StringBody("1"));
                    entity3.addPart("file", new FileBody(IMAGEFILE));

                }catch (UnsupportedEncodingException vv){

                }








            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setMessage("Uploading IMAGE..." +progress + " %"); // Setting Message


        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            final String haso;

            String FILE_UPLOAD_URLXX = "http://housekeepers.world/upload_image_android.php";
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


                Log.e("UPLOADING IMAGE", "executing request " + httppost.getRequestLine());

                //publishProgress(params.);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    //haso = responseString;

                } else {

                    UserProfile.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //createdialog("ERROR DETECTED","Please rechack your data if its correct and try again. "+ statusCode,3);

                            Log.e("ClientProtocolException", "ERROR UPLOADING IMAGE");
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

            progressDialog.cancel();

            Log.e("RESULT FROM SERVER", "RESULTS from server: " + result);
            //new getpicha().execute();

            //mAdapter.notifyDataSetChanged();

            Intent myIntent = new Intent(UserProfile.this, UserProfile.class);
            myIntent.putExtra("key", 100); //Optional parameters
            UserProfile.this.startActivity(myIntent);


            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            // Perform error post processing here...
        }

    }








    /**
     * Uploading the file to server
     * */
    private class SetMesejiStatusSeen extends AsyncTask<Void, Integer, String> {
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


            String CREATEACCOUNT = "http://housekeepers.world/checknewmsj.php?idx="+CURRENTUSER_IDX+"&notify=200";

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
