package com.example.qrteach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private EditText editRole, editFullName, editEmail, editPassword;
    private ProgressBar progressBar;
    private TextView registerUser;
    private ImageView banner;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        editRole = (EditText) findViewById(R.id.role);
        editFullName = (EditText) findViewById(R.id.fullname);
        editEmail = (EditText) findViewById(R.id.emailaddress);
        editPassword = (EditText) findViewById(R.id.pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        banner = (ImageView) findViewById(R.id.banner);
        registerUser = (Button) findViewById(R.id.registerUser);

        banner.setOnClickListener(this);
        registerUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editEmail.getText().toString().trim();
        String fullname = editFullName.getText().toString().trim();
        String role = editRole.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //Registration Form Validation
        if (role.isEmpty()) {
            editRole.setError("Role is required!");
            editRole.requestFocus();
            return;
        }

        if (fullname.isEmpty()) {
            editFullName.setError("Full name is required!");
            editFullName.requestFocus();
            return;
        }


        if (email.isEmpty()) {
            editEmail.setError("Email Address is required!");
            editEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email address!");
            editEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Desired Password is required!");
            editPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("Minimum password length is 6 characters!");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(fullname, email, role);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "Account Created!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));

                                        // redirects to Login layout

                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });

                        } else {
                            Toast.makeText(RegisterUser.this, "Failed to register!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

    }
}