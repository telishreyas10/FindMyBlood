package com.blizzard.app.findmyblood;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button nextBtn,backBtn;
    private int current_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mSlideViewPager=(ViewPager)findViewById(R.id.slideViewPager);
        mDotsLayout=(LinearLayout)findViewById(R.id.dotsLayout);

        nextBtn=(Button)findViewById(R.id.next_button);
        backBtn=(Button)findViewById(R.id.back_button);

        sliderAdapter=new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDots(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(current_page+1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(current_page-1);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent i=new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void addDots(int position){
        mDots=new TextView[3];
        mDotsLayout.removeAllViews();

        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(45);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotsLayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            current_page=position;
            if(position==0){
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("");
            }
            else if(position==mDots.length-1) {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Finish");
                backBtn.setText("Back");
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(WelcomeActivity.this,StartActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
            else{
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
