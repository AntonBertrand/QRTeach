package com.example.qrteach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.qrteach.HelpSheet.TeacherID;
import static com.example.qrteach.MainActivity.userRole;
import static com.example.qrteach.Messages.teacherName;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton, backHome;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference, reference1, reference2;

    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        backHome = (ImageView) findViewById(R.id.backHome);

        // Getting UserID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference1 = FirebaseDatabase.getInstance().getReference("Messages" + userID + "_" + TeacherID);
        reference2 = FirebaseDatabase.getInstance().getReference("Messages" + TeacherID + "_" + userID);

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userRole.equals("Teacher")) {
                    startActivity(new Intent(Chat.this, TeacherMenu.class));
                } else if (userRole.equals("Learner")) {
                    startActivity(new Intent(Chat.this, UserMenu.class));
                } else {
                    Toast.makeText(Chat.this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
                }


            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", userID);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();


                if (userName.equals(userID)) {
                    addMessageBox("You:\n" + message, 1);
                } else {
                    addMessageBox(teacherName + ":\n" + message, 2);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Uses a Dark Chat Bubble for Messages received & Light Chat Bubble for Messages Sent
    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if (type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}