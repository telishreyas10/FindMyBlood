package com.blizzard.app.findmyblood;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();



        viewPager=findViewById(R.id.main_tabpager);
        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
      viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout=findViewById(R.id.main_tabs);
       tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fbAuth.getCurrentUser();
        if(currentUser==null){
            updateUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.main_item_logout){
            fbAuth.getInstance().signOut();
            updateUI();
         }
         else if (item.getItemId()==R.id.main_item_account){
             Intent i=new Intent(MainActivity.this,ProfileActivity.class);
             startActivity(i);
         }
            return true;
    }

    public void updateUI(){
        Intent i=new Intent(MainActivity.this,StartActivity.class);
        startActivity(i);
        finish();
    }
}
