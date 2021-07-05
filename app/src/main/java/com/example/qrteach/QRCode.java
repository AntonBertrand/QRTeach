package com.example.qrteach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import android.widget.TextView;

import static com.example.qrteach.CreateSheet.sheetID;
import static com.example.qrteach.TeacherMenu.tfullName;


public class QRCode extends AppCompatActivity implements View.OnClickListener {

    Button homeBtn;
    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView userName = (TextView) findViewById(R.id.usernameTag);
        userName.setText(tfullName);

        homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(this);

        qrImage = findViewById(R.id.qrPlaceHolder);

        //Generates QR Code containing Associated HelpSheet ID
        QRGEncoder qrgEncoder = new QRGEncoder(sheetID, null, QRGContents.Type.TEXT, 500);
        Bitmap qrBits = qrgEncoder.getBitmap();
        qrImage.setImageBitmap(qrBits);
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.homeBtn:
                startActivity(new Intent(this, TeacherMenu.class));
                break;
        }

    }

}