package com.example.ahmed150236.textdetection_ml;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button snpBtn;
    private Button detectBtn;
    private ImageView image;
    private TextView textView;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snpBtn = findViewById(R.id.snapBtn);
        detectBtn = findViewById(R.id.detectBtn);
        image = findViewById(R.id.image);
        textView = findViewById(R.id.textView);

        snpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detectTxt();
            }
        });



    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }


    private void detectTxt(){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        FirebaseVisionTextDetector detector= FirebaseVision.getInstance().getVisionTextDetector();

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {

            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private  void processTxt(FirebaseVisionText text){
        List<FirebaseVisionText.Block> blocks=text.getBlocks();

        if(blocks.size()==0){
            Toast.makeText(this, "No Text Detected", Toast.LENGTH_SHORT).show();
            return;
        }

        for (FirebaseVisionText.Block block:text.getBlocks()){
            String txt=block.getText();
            textView.setText(txt);
        }
    }

}
