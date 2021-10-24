package com.blizzard.app.findmyblood;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter{
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                RequestFragment reqFrag = new RequestFragment();
                return reqFrag;
            case 1:
                AccountFragment accFrag = new AccountFragment();
                return accFrag;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "FIND BLOOD";
            case 1:
                return "BLOOD REQUEST";
            default:
                return null;
        }
    }
}
