package com.example.activity5;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import java.io.IOException;
import android.widget.Button;
import android.widget.EditText;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



public class MainActivity extends AppCompatActivity {

    Uri geoLocation;
    private static final int REQUEST_VIDEO_CAPTURE = 1 ;
    private EditText Location;
    private Button OpenLocation;
  private static  final  int REQUEST_IMAGE_CAPTURE = 1;
  private Button mCaptureBtn;
    private Object ImageBitmap;
   Button OpenWebsite;
   EditText website;
EditText share;
Button ShareThisText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        OpenWebsite =  (Button)  findViewById(R.id. OpenWebsite);
        website =  (EditText)  findViewById(R.id. website);
        mCaptureBtn= findViewById(R.id.buttonStartCamera);
        OpenWebsite =  (Button)  findViewById(R.id. OpenWebsite);
        Location = (EditText) findViewById(R.id.Location);
         OpenLocation = (Button) findViewById(R.id.OpenLocation);
        share = (EditText) findViewById(R.id.share);
        ShareThisText = (Button) findViewById(R.id.ShareThisText);

        ShareThisText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToShare = share.getText().toString().trim();
                if (!textToShare.isEmpty()) {
                    // Create an Intent to share the text
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

                    // Start the activity to share the text
                    startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
            }

        });



        OpenLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = Location.getText().toString().trim();
                if (!address.isEmpty()) {
                    // Create a Uri with the geo scheme and the entered address
                    Uri geoLocation = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                    // Call the showMap method to open the map with the provided location
                    showMap(geoLocation);
                }

                }



        });


        OpenWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urll = website.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(urll));
                startActivity(intent);
                if (!urll.startsWith("http://") && !urll.startsWith("https://")) {
                    String.valueOf(Uri.parse("http://" + urll));
                }


                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

        });



        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();

            }
        });

        }

    private void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void dispatchTakePictureIntent(){

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }

        }

            //display error state to the user




        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode== REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();

                String imageUri = saveImageToSharedPreferences ((Bitmap) ImageBitmap);
                //mAdapter.addImage(imageUri);
                //mAdapter.notifyDataSetChange();
            }

        }
        private String saveImageToSharedPreferences (Bitmap imageBitmap){
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            int nextImageIndex= sharedPreferences.getInt( "nextImageIndex", 0);
            String imageUri = "image_" + nextImageIndex + ".png";
            try {

                FileOutputStream fos = openFileOutput(imageUri, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }catch (FileNotFoundException e) {
                throw new RuntimeException(e);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
     SharedPreferences. Editor editor = sharedPreferences.edit();
           editor.putInt( "nextImageInex", nextImageIndex + 1);
           editor.apply();
           return imageUri;

        }


        }
