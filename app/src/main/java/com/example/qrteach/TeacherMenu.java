package com.example.qrteach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherMenu extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private ImageButton qrScanner;
    private ImageButton createSheet;
    private ImageButton messages;
    private String userID;
    public static String tfullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        qrScanner = (ImageButton) findViewById(R.id.scanQR);
        qrScanner.setOnClickListener(this);

        messages = (ImageButton) findViewById(R.id.messages);
        messages.setOnClickListener(this);

        createSheet = (ImageButton) findViewById(R.id.createSheet);
        createSheet.setOnClickListener(this);

        final TextView userName = (TextView) findViewById(R.id.usernameTag);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                tfullName = userProfile.fullName;
                userName.setText(tfullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TeacherMenu.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log:
                Toast.makeText(TeacherMenu.this, "You've Been Signed Out!", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.scanQR:
                startActivity(new Intent(this, QRScanner.class));
                break;

            case R.id.createSheet:
                startActivity(new Intent(this, CreateSheet.class));
                break;

            case R.id.messages:
                startActivity(new Intent(this, Messages.class));
                break;
        }

    }
}