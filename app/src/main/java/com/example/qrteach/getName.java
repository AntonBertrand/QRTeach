package com.example.qrteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.qrteach.HelpSheet.TeacherID;
import static com.example.qrteach.Messages.teacherName;

public class getName extends AppCompatActivity {

    DatabaseReference reference3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieves Recipient Full Name from Database
        reference3 = FirebaseDatabase.getInstance().getReference("Users").child(TeacherID);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacherName = dataSnapshot.child("fullName").getValue().toString();
                startActivity(new Intent(getName.this, Chat.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //

    }
}