package com.blizzard.app.findmyblood;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText register_email;
    private EditText register_password;
    private EditText register_fullname;
    private EditText register_age;
    private EditText register_address;
    private EditText register_location;
    private EditText register_phone;
    private Spinner register_spinner;
    private Button register_button;
    private TextView register_heading;

    private FirebaseAuth fbAuth;
    private DatabaseReference fDb;
    ArrayAdapter<CharSequence> adapter;
    private String deviceToken;

    String fullname="",age="",address="",location="",phone="",bloodgroup="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_fullname=(EditText) findViewById(R.id.register_fullname);
        register_email=(EditText) findViewById(R.id.register_email);
        register_password=(EditText) findViewById(R.id.register_password);
        register_age=(EditText) findViewById(R.id.register_age);
        register_address=(EditText) findViewById(R.id.register_address);
        register_location=(EditText) findViewById(R.id.register_location);
        register_phone=(EditText) findViewById(R.id.register_phone);
        register_heading=(TextView)findViewById(R.id.register_heading);
        register_heading.setText("Register an Account");

        register_spinner=(Spinner)findViewById(R.id.register_spinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        register_spinner.setAdapter(adapter);
        register_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgroup=parent.getItemAtPosition(position).toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        register_button=findViewById(R.id.register_button);
        fbAuth=FirebaseAuth.getInstance();
        register_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view==register_button){
            registerUser();
        }
    }



    private void registerUser() {
        String email=register_email.getText().toString().trim();
        String password=register_password.getText().toString().trim();
        fullname=register_fullname.getText().toString().trim();
        age=register_age.getText().toString().trim();
        address=register_address.getText().toString().trim();
        location=register_location.getText().toString().trim();
        phone=register_phone.getText().toString().trim();

        if(email.isEmpty()){
            Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        fbAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    deviceToken=instanceIdResult.getToken();
                                }
                            });

                            String uid=fbAuth.getCurrentUser().getUid();

                            fDb=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("Uid",uid);
                            userMap.put("FullName",fullname);
                            userMap.put("Age",age);
                            userMap.put("Address",address);
                            userMap.put("Location",location);
                            userMap.put("Phone",phone);
                            userMap.put("Bloodgroup",bloodgroup);
                            userMap.put("DeviceToken",deviceToken);
                            userMap.put("image","default");
                            userMap.put("thumbnail_image","default");
                            fDb.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                                         finish();
                                         startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    }
                                }
                            });



                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Enter Valid Email & Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
