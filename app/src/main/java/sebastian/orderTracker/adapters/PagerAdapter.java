package sebastian.orderTracker.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import sebastian.orderTracker.TabClientList;
import sebastian.orderTracker.TabProductList;

/**
 * Created by Senastian on 30/03/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        //String[] daysArray = context.getResources().getStringArray(R.array.days);
        Fragment tab;
        if(position >= 0 && position < 7) {
            tab = new TabClientList();
            Bundle b = new Bundle();
            b.putInt("day", position);
            tab.setArguments(b);
        } else {
           tab = new TabProductList();
        }
        return tab;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
