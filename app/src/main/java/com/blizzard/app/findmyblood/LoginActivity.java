package com.blizzard.app.findmyblood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText login_email;
    private EditText login_password;
    private Button login_button;
    private FirebaseAuth fbAuth;
    private DatabaseReference userRef;
    private TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        heading=(TextView)findViewById(R.id.login_heading);
        heading.setText("Login");

        fbAuth=FirebaseAuth.getInstance();
        login_email=(EditText)findViewById(R.id.login_email);
        login_password=(EditText)findViewById(R.id.login_password);
        login_button=(Button)findViewById(R.id.login_button);

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==login_button){
            String email=login_email.getText().toString().trim();
            String password=login_password.getText().toString().trim();

            if(email.isEmpty()){
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.isEmpty()){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
                return;
            }

            fbAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String currentuser=fbAuth.getCurrentUser().getUid();
                                String deviceToken= FirebaseInstanceId.getInstance().getToken();

                                userRef.child(currentuser).child("DeviceToken").setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                    }
                                });

                            }
                            else{
                                Toast.makeText(LoginActivity.this,"Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
