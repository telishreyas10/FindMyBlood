package com.blizzard.app.findmyblood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn1;
    private Button btn2;
    private TextView heading;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn1=findViewById(R.id.button2);
        btn2=findViewById(R.id.button3);
        heading=findViewById(R.id.start_heading);
        heading.setText("Login & Register");
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btn1){
            i=new Intent(StartActivity.this,LoginActivity.class);
            startActivity(i);
            finish();

        }
        else if(view==btn2){
            i=new Intent(StartActivity.this,RegisterActivity.class);
            startActivity(i);
        }
    }
}
