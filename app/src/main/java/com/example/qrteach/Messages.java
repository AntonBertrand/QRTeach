package com.example.qrteach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.qrteach.HelpSheet.TeacherID;
import static com.example.qrteach.MainActivity.userRole;
import static com.example.qrteach.TeacherMenu.tfullName;
import static com.example.qrteach.UserMenu.fullName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Messages extends AppCompatActivity implements View.OnClickListener {
    private ListView usersList;
    private TextView noUsersText;
    private ArrayList<String> idList = new ArrayList<>();
    private int totalUsers = 0;
    private ProgressDialog pd;
    private Button homeBtn;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    public static String teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        final TextView userName = (TextView) findViewById(R.id.usernameTag);
        if (userRole.equals("Teacher")) {
            userName.setText(tfullName);
        } else if (userRole.equals("Learner")) {
            userName.setText(fullName);
        } else {
            Toast.makeText(Messages.this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
        }


        //Getting UserID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(this);

        //Display Loading Message whilst Retrieving User Details
        pd = new ProgressDialog(Messages.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://qrteach-default-rtdb.firebaseio.com/Users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Messages.this);
        rQueue.add(request);

        //If User ID is clicked, Their ID is taken from the array and stored in 'TeacherId'
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TeacherID = idList.get(position);

                startActivity(new Intent(Messages.this, getName.class));

            }
        });
    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                //If not the current user then add their key to the array list ((Full Name needs to be changed to UID)
                if (!key.equals(userID)) {
                    idList.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //If no users to add to list, display No Users Text, Otherwise populate user list with array
        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, idList));
        }

        pd.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeBtn:

                if (userRole.equals("Teacher")) {
                    startActivity(new Intent(this, TeacherMenu.class));
                } else if (userRole.equals("Learner")) {
                    startActivity(new Intent(this, UserMenu.class));
                } else {
                    Toast.makeText(this, "Something is Wrong! User has no role!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}