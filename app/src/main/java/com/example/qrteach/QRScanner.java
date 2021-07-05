package com.example.qrteach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import static com.example.qrteach.MainActivity.userRole;
import static com.example.qrteach.UserMenu.fullName;
import static com.example.qrteach.TeacherMenu.tfullName;


public class QRScanner extends AppCompatActivity implements View.OnClickListener {

    private CodeScanner mCodeScanner;
    public static String workSheetID;

    String[] PERMISSIONS = {Manifest.permission.CAMERA};
    CodeScannerView scannerView;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);
        scannerView = findViewById(R.id.scanner_view);

        //Setting Username in Toolbar
        final TextView userName = (TextView) findViewById(R.id.usernameTag);
        if (userRole.equals("Teacher")) {
            userName.setText(tfullName);
        } else if (userRole.equals("Learner")) {
            userName.setText(fullName);
        } else {
            Toast.makeText(QRScanner.this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
        }

        home = (Button) findViewById(R.id.backToHome);
        home.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(PERMISSIONS, 2002);
            } else
                startScanner();
            ;
        } else
            startScanner();
    }

    //If Camera Permissions not Granted, Alert User that they're required
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2002) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startScanner();
            else
                Toast.makeText(QRScanner.this, "Camera Permission Required!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startScanner() {
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Gets the WorksheetID held in the QR Code and saves it to be used in the HelpSheet Class
                        workSheetID = result.getText();
                        startActivity(new Intent(QRScanner.this, HelpSheet.class));
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null)
            mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backToHome:

                if (userRole.equals("Teacher")) {
                    startActivity(new Intent(QRScanner.this, TeacherMenu.class));
                } else if (userRole.equals("Learner")) {
                    startActivity(new Intent(QRScanner.this, UserMenu.class));
                } else {
                    Toast.makeText(QRScanner.this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}