package com.blizzard.app.findmyblood;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    private Context c;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context c) {
        this.c = c;
    }

    public int[] images={
            R.drawable.blood_logo_1,
            R.drawable.blood_logo_2,
            R.drawable.blood_logo_3
    };

    public String[] headings ={
            "Donate Blood","Search Donor","Explore Updates Around You"
    };

    public String[] description={
            "Mother’s tears cannot save her child’s life but your blood can. \n",
                    "Find Blood Donors near your Location or you city. \n" ,
            "Stay Connected for blood donation updates. \n"

    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater)c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImage=(ImageView)v.findViewById(R.id.slide_image);
        TextView slideHeading=(TextView)v.findViewById(R.id.slide_heading);
        TextView slideDescription=(TextView)v.findViewById(R.id.slide_description);

        slideImage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(description[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}




