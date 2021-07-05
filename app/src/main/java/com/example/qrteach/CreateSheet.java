package com.example.qrteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.qrteach.TeacherMenu.tfullName;


public class CreateSheet extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private String userID;

    private Button createQR;
    private Button cancel;
    private EditText worksheetName;
    private EditText worksheetSummary;
    private EditText worksheetInstructions;
    private EditText links;

    public static String sheetID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sheet);

        //Setting Username in Toolbar
        final TextView userName = (TextView) findViewById(R.id.usernameTag);
        userName.setText(tfullName);

        //Getting UserID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        createQR = (Button) findViewById(R.id.msgBtn);
        cancel = (Button) findViewById(R.id.cancel);
        worksheetName = (EditText) findViewById(R.id.worksheetName);
        worksheetSummary = (EditText) findViewById(R.id.worksheetSummary);
        worksheetInstructions = (EditText) findViewById(R.id.worksheetInstructions);
        links = (EditText) findViewById(R.id.links);

        createQR.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                startActivity(new Intent(this, TeacherMenu.class));
                break;

            case R.id.msgBtn:

                reference2 = FirebaseDatabase.getInstance().getReference("HelpSheets");
                String id = reference2.push().getKey();
                sheetID = id;

                //Saving Worksheet Fields to Database
                String name = worksheetName.getText().toString().trim();
                String summary = worksheetSummary.getText().toString().trim();
                String instructions = worksheetInstructions.getText().toString().trim();
                String worksheetlinks = links.getText().toString().trim();

                //Form Validation
                if (name.isEmpty()) {
                    worksheetName.setError("Name is required!");
                    worksheetName.requestFocus();
                    return;
                }

                if (summary.isEmpty()) {
                    worksheetSummary.setError("Summary is required!");
                    worksheetSummary.requestFocus();
                    return;
                }

                if (instructions.isEmpty()) {
                    worksheetInstructions.setError("Instructions is required!");
                    worksheetInstructions.requestFocus();
                    return;
                }

                HelpSheets helpSheets = new HelpSheets(id, name, summary, instructions, worksheetlinks, userID);
                reference2.child(id).setValue(helpSheets);

                startActivity(new Intent(CreateSheet.this, QRCode.class));

                break;
        }
    }
}