package com.example.qrteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.qrteach.MainActivity.userRole;
import static com.example.qrteach.QRScanner.workSheetID;
import static com.example.qrteach.UserMenu.fullName;
import static com.example.qrteach.TeacherMenu.tfullName;

public class HelpSheet extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private Button message;
    private Button home;
    private TextView worksheetName;
    private TextView worksheetSummary;
    private TextView worksheetInstructions;
    private TextView links;

    public static String TeacherID;

    private DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_sheet);

        //Setting Username in Toolbar
        final TextView userName = (TextView) findViewById(R.id.usernameTag);
        if (userRole.equals("Teacher")) {
            userName.setText(tfullName);
        } else if (userRole.equals("Learner")) {
            userName.setText(fullName);
        } else {
            Toast.makeText(HelpSheet.this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
        }

        message = (Button) findViewById(R.id.msg);
        message.setOnClickListener(this);

        home = (Button) findViewById(R.id.home2);
        home.setOnClickListener(this);

        worksheetName = (TextView) findViewById(R.id.worksheetName);
        worksheetSummary = (TextView) findViewById(R.id.worksheetSummary);
        worksheetInstructions = (TextView) findViewById(R.id.worksheetInstructions);
        links = (TextView) findViewById(R.id.links);

        if (userRole.equals("Teacher")) {
            message.setVisibility(View.GONE);
        }

        reference = FirebaseDatabase.getInstance().getReference().child("HelpSheets").child(workSheetID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Retrieves HelpSheet Details from Database & Populates HelpSheet
                String value = dataSnapshot.child("worksheetName").getValue().toString();
                String value2 = dataSnapshot.child("worksheetSummary").getValue().toString();
                String value3 = dataSnapshot.child("worksheetInstructions").getValue().toString();
                String value4 = dataSnapshot.child("links").getValue().toString();
                worksheetName.setText(value);
                worksheetSummary.setText(value2);
                worksheetInstructions.setText(value3);
                links.setText(value4);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        //Retrieving TeacherID
        reference2 = FirebaseDatabase.getInstance().getReference().child("HelpSheets").child(workSheetID);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TeacherID = dataSnapshot.child("teacherID").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home2:

                if (userRole.equals("Teacher")) {
                    startActivity(new Intent(HelpSheet.this, TeacherMenu.class));
                } else if (userRole.equals("Learner")) {
                    startActivity(new Intent(HelpSheet.this, UserMenu.class));
                } else {
                    Toast.makeText(HelpSheet.this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.msg:
                startActivity(new Intent(HelpSheet.this, getName.class));
                break;
        }

    }
}