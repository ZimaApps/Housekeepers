package tz.co.nyotaapps.housekeeper.housekeeper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmEmail;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Or;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import com.yalantis.ucrop.UCrop;


import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static androidx.core.content.FileProvider.getUriForFile;

public class Register extends AppActivity implements Validator.ValidationListener {

    public View rootView;
    private ImageView imageView;
    private Button camera, gallery;

    //keep track of camera capture intent
    static final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 3;
    //keep track of gallery intent
    final int PICK_IMAGE_REQUEST = 2;
    //captured picture uri
    private Uri picUri;

    String mCurrentPhotoPath;


    private static final String TAG = Register.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    //private static final String TAG = ImagePickerActivity.class.getSimpleName();
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final String INTENT_ASPECT_RATIO_X = "aspect_ratio_x";
    public static final String INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y";
    public static final String INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio";
    public static final String INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality";
    public static final String INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height";
    public static final String INTENT_BITMAP_MAX_WIDTH = "max_width";
    public static final String INTENT_BITMAP_MAX_HEIGHT = "max_height";
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public AndroidMultiPartEntity entity3;
    public Bitmap BITMAPIMAGE;
    public File IMAGEFILE;

    private ProgressDialog progressDialog;

    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static String fileName ="";
    public String filePath3;
    public String USERNAMEX ="";
    public String NAMBAYASIMUX;
    public String NENOLASIRI;
    public  String AREAX ="";
    public String EMAIL = "no";
    public String MAELEZOX ="";
    public String GENDERX = "100";
    public String UMRIX ="";
    String myInternationalNumber ="";
    IntlPhoneInput phoneInputView;
    public String country ="";
    private String uniqueID ="";
    private String androidId ="";


    /////////////////////////////////////////////////////////////////////////////////////

    @NotEmpty(message = "Please enter Name")
    private EditText name;

    @NotEmpty(message = "Please enter Last Name")
    private EditText name2;

    @Email
    private EditText EMAILz;

    //@Password(min = 4, message = "Password must have at-least 4 characters")
    private EditText NENOLASIRIz;


   // @Pattern(regex = "[789][0-9]{9}", message = "Please enter valid Phone number")
   // private EditText phone_number_edt;

    @Length(max = 2, min = 1, message = "Input must be between 18 and 99")
    private EditText UMRIXz;

    @NotEmpty
    private EditText AREAXz;

    @NotEmpty
    private EditText MAELEZOXz;


    //@Checked
   // private CheckBox termsAndCondition;

   // private Button submit;


///////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        new UploadImagetoserverY().execute();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        uniqueID = UUID.randomUUID().toString();

        validator = new Validator(this);
        validator.setValidationListener(this);


        initView();




        final Button buttonxx = findViewById(R.id.email_sign_iGG);
        buttonxx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent myIntent = new Intent(Register.this, Login.class);

                //Register.this.startActivity(myIntent);

                //Checkdata();



                if(phoneInputView.isValid()) {
                    myInternationalNumber = phoneInputView.getNumber();
                    phoneInputView.getPhoneNumber();
                    phoneInputView.getSelectedCountry();
                    country = phoneInputView.getSelectedCountry().getName().toString();
                    Log.e("PHONE NUMBER", "THE NUMBER: " + phoneInputView.getNumber().toString());
                    Log.e("PHONE NUMBER", "THE NUMBER: " + phoneInputView.getPhoneNumber().toString());
                    Log.e("PHONE COUNTRY", "THE COUNTRY: " + phoneInputView.getSelectedCountry().getName().toString());

                    if(IMAGEFILE == null){
                        Toast toast = Toast.makeText(getApplicationContext(), "IMAGE IS NEEDED", Toast.LENGTH_LONG);
                        toast.show();
                    }else{

                        validator.validate();
                    }


                }else{

                    TextView bb = findViewById(R.id.thepon);
                    bb.setText("Please Enter a Valid phone number");
                    bb.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });



    }





    private void initView() {
        name = (EditText) findViewById(R.id.username1);
        name2 = (EditText) findViewById(R.id.username2);
        EMAILz = (EditText) findViewById(R.id.email);
        AREAXz = (EditText) findViewById(R.id.area);
        UMRIXz = (EditText) findViewById(R.id.umri);
        MAELEZOXz = (EditText) findViewById(R.id.maelezo);
        phoneInputView = (IntlPhoneInput) findViewById(R.id.my_phone_input);
        phoneInputView.setEmptyDefault("US");

    }



















    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (validated) {
            // Our form is successfully validated, so, do your stuffs here...
            //Toast.makeText(this, "Form Successfully Validated", Toast.LENGTH_LONG).show();
        }
    }















    public void setgendermale(View v){

        GENDERX = "100";
    }

    public void setgenderfemale(View v){

        GENDERX = "200";
    }





    public void getphoto1(View v){

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
            Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
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

            ImageView imageView1 = (ImageView) findViewById(R.id.theimx);
            //imageView1.setImageBitmap(bitmap);

            imageView1.setImageURI(imagePath);

            new fileFromBitmap().execute();

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
        return getUriForFile(Register.this, getPackageName() + ".fileprovider", image);
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





    private class UploadImagetoserverY extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            Log.e("CALLLEEEED", "CALLLEEEED : ");

            progressDialog = new ProgressDialog(Register.this);
            progressDialog.setMessage("Uploading Image..."); // Setting Message
            progressDialog.setTitle("REGISTERING"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);


            try{

                entity3 = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                                //progressDialog.setMessage("Uploading IMAGE..." +progress + " %"); // Setting Message
                            }
                        });



                USERNAMEX = name.getText().toString() + " "+ name2.getText().toString();
                EMAIL = EMAILz.getText().toString();
                AREAX = AREAXz.getText().toString();
                UMRIX = UMRIXz.getText().toString();
                MAELEZOX = MAELEZOXz.getText().toString();



                entity3.addPart("file", new FileBody(IMAGEFILE));
                //entity3.addPart("idx", new StringBody(randomAlphaNumeric(12)));
                entity3.addPart("idx", new StringBody(uniqueID));
                entity3.addPart("name", new StringBody(USERNAMEX));
                entity3.addPart("phone", new StringBody(myInternationalNumber));
                entity3.addPart("umri", new StringBody(UMRIX));
                entity3.addPart("area", new StringBody(AREAX));
                entity3.addPart("email", new StringBody(EMAIL));
                entity3.addPart("maelezo", new StringBody(MAELEZOX));
                entity3.addPart("gender", new StringBody(GENDERX));
                //entity3.addPart("password", new StringBody(NENOLASIRI));
                entity3.addPart("country", new StringBody(country));
                entity3.addPart("androidId", new StringBody(androidId));


            }catch (UnsupportedEncodingException ep){

                Log.e("UnsupportedEncodingExce", "error: " + ep);

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

            String FILE_UPLOAD_URLXX = "http://zimaapps.com/mobileApps/housekeepers/register_mobile.php";
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

                    Register.this.runOnUiThread(new Runnable() {
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


            progressDialog.cancel();

            //Log.e("result", "result from server: " + result);

            if(result.equalsIgnoreCase( null) || result.equalsIgnoreCase("")){

                Log.e("result", "result from server: NULL" + result);

                progressDialog = new ProgressDialog(Register.this);
                progressDialog.setMessage("We are unable to register you at this moment, Please try again later."); // Setting Message
                progressDialog.setTitle("ERROR"); // Setting Title
                //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(true);

            }else{

                Log.e("result", "result from server: NOT NULL" + result);

                if(result.equalsIgnoreCase("1000")){

                    progressDialog = new ProgressDialog(Register.this);
                    progressDialog.setMessage("We are unable to register you at this moment, Please try again later."); // Setting Message
                    progressDialog.setTitle("ERROR"); // Setting Title
                    //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(true);

                    Log.e("result", "result from server 1000: " + result);

                }else if(result.equalsIgnoreCase("100")){

                    Intent myIntent = new Intent(Register.this, Login.class);
                    myIntent.putExtra("nambayasimu", myInternationalNumber); //Optional parameters
                    myIntent.putExtra("nenolasiri", androidId); //Optional parameters
                    Register.this.startActivity(myIntent);

                    Log.e("result", "result from server 100: " + result);

                }



            }




            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            // Perform error post processing here...
        }

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
                File f = new File(Register.this.getCacheDir(), "housekeeper.jpg");
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




    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }



}