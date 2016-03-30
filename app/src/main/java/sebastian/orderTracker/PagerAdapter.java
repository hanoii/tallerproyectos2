package sebastian.orderTracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Senastian on 30/03/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabClientList tab1 = new TabClientList();
                return tab1;
            case 1:
                TabProductList tab2 = new TabProductList();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
