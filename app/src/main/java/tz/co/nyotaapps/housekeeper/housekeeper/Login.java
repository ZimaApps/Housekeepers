package tz.co.nyotaapps.housekeeper.housekeeper;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static org.apache.http.conn.socket.PlainConnectionSocketFactory.getSocketFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Login extends AppCompatActivity {
    public String TAG = "LOGIN";
    public static boolean OFFLINEMODE = false;
    private ProgressBar spinner;
    private ProgressDialog progressDialog;
    public static String CURRENTUSERNAME = "OFFLINEUSER";
    public String responseString = null;
    String majibu = "";
    ArrayList<Profile> list;
    MyAdapter adapter;
    String sysuser;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        if(isConnected()){

        }else{


        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sysuser = extras.getString("nambayasimu");
            Password = extras.getString("nenolasiri");

            Log.e("OYAAA", "THE SYSUSER: " + sysuser.toString());
            Log.e("OYAAA", "THE PASSWORD: " + Password.toString());

            new signin2().execute();
        }



        Button button= (Button) findViewById(R.id.email_sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //gotomain();
                TextInputEditText user = (TextInputEditText) findViewById(R.id.UserName);
                sysuser = user.getText().toString().trim();

                TextInputEditText Passwordx = (TextInputEditText) findViewById(R.id.Password);
                Password = Passwordx.getText().toString().trim();

                new signin2().execute();
            }
        });

        Button buttonx= (Button) findViewById(R.id.email_sign_i);
        buttonx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //gotomain();

                Intent myIntent = new Intent(Login.this, Register.class);
                myIntent.putExtra("key", 100); //Optional parameters
                Login.this.startActivity(myIntent);
            }
        });


    //new watu().execute();

    }



    /**
     * Uploading the file to server
     * */
    private class signin2 extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {


            TextView bb = findViewById(R.id.error);
            bb.setVisibility(View.GONE);



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






                    String CREATEACCOUNT = "http://housekeepers.world/login_android.php?nambayasimu="+sysuser+"&inputPassword="+Password+"";

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

            if(result.equals("null")){
                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
            }
            else{

                Log.e("UPLOADING DDDDDDD1", "Response from server: " + result);
                try {

                    if(isNumeric(result)){

                        TextView bb = findViewById(R.id.error);
                        bb.setText("Password au Simu ulio weka sio sahihi, jaribu tena au jiunge.");
                        bb.setVisibility(View.VISIBLE);
                    }else{



                        JSONObject obj = new JSONObject(result);

                        Log.e("UPLOADING DDDDDDD2", "Response from server: " + obj.toString());
                        String phone = obj.getJSONObject("mtu").getString("Namba");
                        String idx = obj.getJSONObject("mtu").getString("idx");
                        String name = obj.getJSONObject("mtu").getString("Mtangazaji");
                        String umri = obj.getJSONObject("mtu").getString("umri");
                        String area = obj.getJSONObject("mtu").getString("Mtaa");
                        String email = obj.getJSONObject("mtu").getString("emailxx");
                        String maelezo = obj.getJSONObject("mtu").getString("Maelezo1");
                        String gender = obj.getJSONObject("mtu").getString("gender");
                        String picha = obj.getJSONObject("mtu").getString("Image");
                        String love = obj.getJSONObject("mtu").getString("bad");
                        String uniqueID = obj.getJSONObject("mtu").getString("uniqueID");
                        Log.e("UPLOADING RRRRR31", "Response from server: " + phone);



                        String currentuser ="currentuser";


                        SQLiteDatabase myDB = Login.this.openOrCreateDatabase("housekeeper", MODE_PRIVATE, null);
                        //myDB.execSQL("DROP TABLE IF EXISTS " + currentuser);
                        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + currentuser + " (phone VARCHAR, idx VARCHAR, name VARCHAR, umri VARCHAR, area VARCHAR, email VARCHAR, maelezo VARCHAR, gender VARCHAR, picha VARCHAR, love VARCHAR, uniqueID VARCHAR);");
                        myDB.execSQL("INSERT INTO " + currentuser + " (phone, idx, name, umri, area, email, maelezo, gender, picha, love,uniqueID)" +
                                " VALUES ('"+phone+"','"+idx+"','"+name+"', '"+umri+"','"+area+"', '"+email+"','"+maelezo+"', '"+gender+"', '"+picha+"', '"+love+"','"+uniqueID+"');");




                        gotomain();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            super.onPostExecute(result);
        }

    }




    public void gotomain(){
        Intent myIntent = new Intent(Login.this, splash.class);
        myIntent.putExtra("key", 100); //Optional parameters
        Login.this.startActivity(myIntent);

    }




    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if ( networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }



    public String ConnectionQuality() {
        NetworkInfo info = getInfo(Login.this);
        if (info == null || !info.isConnected()) {
            return "UNKNOWN";
        }

        if(info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkClass = getNetworkClass(getNetworkType(Login.this));
            if(networkClass == 1)
                return "POOR";
            else if(networkClass == 2 )
                return "GOOD";
            else if(networkClass == 3 )
                return "EXCELLENT";
            else
                return "UNKNOWN";
        }else
            return "UNKNOWN";
    }

    public NetworkInfo getInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public int getNetworkClass(int networkType) {
        try {
            return getNetworkClassReflect(networkType);
        }catch (Exception ignored) {
        }

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case 16: // TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17: // TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return 2;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                return 3;
            default:
                return 0;
        }
    }

    private int getNetworkClassReflect(int networkType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getNetworkClass = TelephonyManager.class.getDeclaredMethod("getNetworkClass", int.class);
        if (!getNetworkClass.isAccessible()) {
            getNetworkClass.setAccessible(true);
        }
        return (Integer) getNetworkClass.invoke(null, networkType);
    }

    public static int getNetworkType(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
    }






    public static boolean isNumeric(String str) {

            try {
                Double.parseDouble(str);
                return true;
            } catch(NumberFormatException e){
                return false;
            }


    }

























}
