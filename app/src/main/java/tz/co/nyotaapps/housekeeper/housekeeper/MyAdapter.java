package tz.co.nyotaapps.housekeeper.housekeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import static java.security.AccessController.getContext;
import static tz.co.nyotaapps.housekeeper.housekeeper.MainActivity.SELECTEDUSER;
import static tz.co.nyotaapps.housekeeper.housekeeper.MainActivity.SELECTEDUSERNAME;
import static tz.co.nyotaapps.housekeeper.housekeeper.MainActivity.EMPLOYERNAME;
import static tz.co.nyotaapps.housekeeper.housekeeper.MainActivity.EMPLOYEREMAIL;
import static tz.co.nyotaapps.housekeeper.housekeeper.MainActivity.mInterstitialAd;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Profile> profiles;
    Uri imageUri;
    String imagelink;
    List<UnifiedNativeAd> ListnativeAd;
    int LOOPNUMBER;
    Activity activity;
    public static Dialog dialog;
    AlertDialog alertDialog;




    public UnifiedNativeAdView adView;

    public UnifiedNativeAdView getAdView() {
        return adView;
    }




    public MyAdapter(Context c , ArrayList<Profile> p, List<UnifiedNativeAd> x, Activity o)
    {
        context = c;
        profiles = p;
        ListnativeAd = x;
        activity = o;
        SELECTEDUSER = "22";
        SELECTEDUSERNAME = "0";
        EMPLOYERNAME = "0";
        EMPLOYEREMAIL = "0";
        dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.member,parent,false));


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        if(position == 3 || position == 15 || position == 30 || position == 45 || position == 60 || position == 75 || position == 90 || position == 105 || position == 120 || position == 135 || position == 150){

            int thesize = ListnativeAd.size();
            if(thesize != 0) {


                if (thesize == 1) {
                    LOOPNUMBER = 0;
                } else {
                    LOOPNUMBER = getRandomNumberInRange(0, (thesize - 1));

                }

                populateNativeAdView(ListnativeAd.get(LOOPNUMBER), adView);

            }



        }else{







            Log.e("NORMAL NEEDED", "NUMBER : "+Integer.toString(position));

            holder.member.setVisibility(View.VISIBLE);

            holder.name.setText(profiles.get(position).getname());
            holder.area.setText(profiles.get(position).getarea());
            if(profiles.get(position).getphone().trim()==""){
                holder.namba.setVisibility(View.GONE);
            }else{
                holder.namba.setText(replaceLastFour(profiles.get(position).getphone()));
            }

            holder.maelezo.setText(profiles.get(position).getmaelezo());
            holder.umri.setText("("+profiles.get(position).getumri().toString()+")");
            //holder.love.setText(profiles.get(position).getlove().toString());
            holder.userid.setText(profiles.get(position).getidx().toString());
            holder.url.setText(profiles.get(position).getpicha().toString());
            // Uri imageUri = Uri.parse("http://mchumba.nyotaapps.co.tz/"+profiles.get(position).getpicha());

            ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
            progressBarDrawable.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            progressBarDrawable.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            progressBarDrawable.setRadius(5);
            progressBarDrawable.setBarWidth(22);


            Uri uri = Uri.parse("http://zimaapps.com/mobileApps/housekeepers/images/"+profiles.get(position).getpicha());
            //SimpleDraweeView draweeView = (SimpleDraweeView) holder.findViewById(R.id.image);
            holder.picha.getHierarchy().setProgressBarImage(progressBarDrawable);
            holder.picha.getHierarchy().setPlaceholderImage(R.drawable.placeholder);
            //draweeView.getHierarchy().setFailureImage(R.drawable.failure);

            holder.picha.setImageURI(uri);


            //imageUri = Uri.parse("http://zimaapps.com/mobileApps/housekeepers/images/"+profiles.get(position).getpicha());
            imagelink = "http://zimaapps.com/mobileApps/housekeepers/images/"+profiles.get(position).getpicha();

            //Log.e("UPLOADING URI", "http://mchumba.nyotaapps.co.tz/"+profiles.get(position).getpicha());

            //Log.e("UPLOADING URI XXz", imageUri.toString());

            //holder.picha.setImageURI(imageUri);



            //holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);



        }


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,namba,area,umri,maelezo,love,userid,url;
        TextView tvTitle,tvDescription,tvAgeRestrictions;
        RatingBar ratingBar;
        Button ctaButton;
        ImageView Icon;
        RelativeLayout member;
        FrameLayout providerViewContainer;

        Button gocheckout;


        SimpleDraweeView picha;
        //Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);

            member = (RelativeLayout) itemView.findViewById(R.id.two_line_item);
            name = (TextView) itemView.findViewById(R.id.name);
            area = (TextView) itemView.findViewById(R.id.area);
            namba = (TextView) itemView.findViewById(R.id.namba);
            maelezo = (TextView) itemView.findViewById(R.id.maelezo);
            umri = (TextView) itemView.findViewById(R.id.umri);
            //love = (TextView) itemView.findViewById(R.id.love);
            userid = (TextView) itemView.findViewById(R.id.userid);
            url = (TextView) itemView.findViewById(R.id.url);
            picha = (SimpleDraweeView) itemView.findViewById(R.id.picha);
            //btn = (Button) itemView.findViewById(R.id.action_button_1);
            gocheckout = itemView.findViewById(R.id.getcontact);

            //////////////////////////////////////////////////////////////////


            ///////////////////////////LISTERNERS///////////////////////////////////////


           /* member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(context, Check_user.class);
                    myIntent.putExtra("name", name.getText().toString()); //Optional parameters
                    myIntent.putExtra("userid", userid.getText().toString()); //Optional parameters
                    myIntent.putExtra("area", area.getText().toString()); //Optional parameters
                    myIntent.putExtra("namba", namba.getText().toString()); //Optional parameters
                    myIntent.putExtra("maelezo", maelezo.getText().toString()); //Optional parameters
                    myIntent.putExtra("umri", umri.getText().toString()); //Optional parameters
                    //myIntent.putExtra("love", love.getText().toString()); //Optional parameters
                    myIntent.putExtra("picha", imagelink.toString()); //Optional parameters
                    myIntent.putExtra("url", url.getText().toString()); //Optional parameters
                    context.startActivity(myIntent);
                }
            });*/

            gocheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                    showDialog(userid.getText().toString(), name.getText().toString());

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                }
            });







        }
        public void onClick(final int position)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                   Intent myIntent = new Intent(context, Check_user.class);
                    myIntent.putExtra("name", name.getText().toString()); //Optional parameters
                    myIntent.putExtra("userid", userid.getText().toString()); //Optional parameters
                    myIntent.putExtra("area", area.getText().toString()); //Optional parameters
                    myIntent.putExtra("namba", namba.getText().toString()); //Optional parameters
                    myIntent.putExtra("maelezo", maelezo.getText().toString()); //Optional parameters
                    myIntent.putExtra("umri", umri.getText().toString()); //Optional parameters
                    //myIntent.putExtra("love", love.getText().toString()); //Optional parameters
                    myIntent.putExtra("picha", imagelink.toString()); //Optional parameters
                    myIntent.putExtra("url", url.getText().toString()); //Optional parameters
                    context.startActivity(myIntent);
                }
            });
        }
    }


    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String replaceLastFour(String s) {
        int length = s.length();
        //Check whether or not the string contains at least four characters; if not, this method is useless
        if (length < 4) return "No Contact";
        return s.substring(0, length - 4) + "****";
    }


        public void showDialog(String user, String name){

            // custom dialog

            dialog.setContentView(R.layout.dialog);
            dialog.setTitle("Title");

            ImageView fc = dialog.findViewById(R.id.theclose);
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            Button fcz = dialog.findViewById(R.id.gobutton);
            fcz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SELECTEDUSER = user;
                    SELECTEDUSERNAME = name;
                    EditText fcf = dialog.findViewById(R.id.usernamex);
                    EMPLOYERNAME = fcf.getText().toString();
                    EditText fcfc = dialog.findViewById(R.id.emailc);
                    EMPLOYEREMAIL = fcfc.getText().toString().trim();

                    Log.e("THE EMAIL", "EMAIL IS: " + EMPLOYEREMAIL);
                    Log.e("THE EMAIL", "EMAIL IS: " + EMPLOYEREMAIL);

                    if(EMPLOYEREMAIL.equalsIgnoreCase("")){
                        createdialog("EMAIL NEEDED",SELECTEDUSERNAME+" : Pease, enter an Email to receive a CV of this Housekeeper");
                    }else{
                        ///////REWARDED VIDEO GOES HERE
                        new FetchTask().execute();
                    }





                }
            });

            dialog.show();


        }



    public void createdialog(String thetitle, String themeseji){

        new AlertDialog.Builder(context)
                .setTitle(thetitle)
                .setMessage(themeseji)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        //this.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }







    public class FetchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog.dismiss();

            createdialog("AN EMAIL WITH ",""+SELECTEDUSERNAME+" CV, HAS BEEN SENT TO "+EMPLOYERNAME+" "+EMPLOYEREMAIL+" IT WILL TAKE A MINUTE. YOU MIGHT NEED TO CHECK YOUR SPAM FOLDER");

            //createdialog("SENDING "+SELECTEDUSERNAME+" CV TO "+EMPLOYERNAME+" "+EMPLOYEREMAIL +"Please wait ",3);

            Log.e("CALLED APPODEAL", SELECTEDUSER);

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
                HttpPost httppost = new HttpPost("https://zimaapps.com/mobileApps/housekeepers/getcv.php");

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

                Log.e("EMAIL STATUS", result);

                Log.e("EMAIL SERVER RESULT", result + " / ");
                createdialog("SUCCESS","CV has been sent to "+EMPLOYEREMAIL+". You may need to check the spam folder, if you can't find the email");

            }else{
                createdialog("INCORRECT DATA","CV was not sent, please try again.");
            }

        }
    }










    private static void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }








}








