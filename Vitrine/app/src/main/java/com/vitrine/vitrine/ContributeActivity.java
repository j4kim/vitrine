package com.vitrine.vitrine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static com.google.android.gms.analytics.internal.zzy.w;

public class ContributeActivity extends AppCompatActivity {

    private Vitrine mVitrine;
    private static final int CAMERA_REQUEST = 1888;
    private String mCurrentPhotoPath;
    private String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);


        Intent i = getIntent();
        mVitrine = i.getParcelableExtra("vitrine");

        startCameraActivity();
    }

    /**
     * Launch the camera activty
     */
    private void startCameraActivity(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("PATH ERROR", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.vitrine.vitrine.contentprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    /**
     * Camera activity result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bigPhoto = BitmapFactory.decodeFile(mCurrentPhotoPath);

            /*
             * Scale the photo
             */
            int w =  bigPhoto.getWidth();
            int h = bigPhoto.getHeight();
            double rapport = w/(double)h;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bigPhoto, (int)(1080*rapport), 1080, false);
            final Bitmap photo = rotateImageIfRequired(mCurrentPhotoPath, scaledBitmap);

            //From https://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/
            //Showing the progress dialog
            final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.upload_picture_url),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            if(loading.isShowing())
                                loading.dismiss();
                            finish();
                            Toast.makeText(ContributeActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();

                            Log.i("upload_error", "onErrorResponse: " + volleyError.getMessage().toString());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    String image = getStringImage(photo);

                    //Getting Image Name
                    String name = imageFileName;
                    String vitrineId = String.valueOf(mVitrine.getId());

                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();

                    //Adding parameters : 1st param is the name of the parameter $_POST["image"]
                    params.put("image", image);
                    params.put("name", name);
                    params.put("vitrineId", vitrineId);

                    //returning parameters
                    return params;
                }
            };

            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //Adding request to the queue
            requestQueue.add(stringRequest);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Convert an image to String (Base64)
     * @param bmp image to convert
     * @return String of the image converted in Base64
     */
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }


    /**
     * Thanks a lot to this answer : http://stackoverflow.com/a/14066265
     */
    private static Bitmap rotateImageIfRequired(String photoPath, Bitmap original) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotated;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotated = rotateImage(original, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotated = rotateImage(original, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotated = rotateImage(original, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return original;
        }
        return rotated;

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
