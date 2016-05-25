package sebastian.orderTracker.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import sebastian.orderTracker.Global;
import sebastian.orderTracker.QuickstartPreferences;
import sebastian.orderTracker.RegistrationIntentService;
import sebastian.orderTracker.adapters.PagerAdapter;
import sebastian.orderTracker.R;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setTabs(tabLayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Seteo Viewpager para poder switchear los fragmentos

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(7);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // Esto es el menú lateral
        //mDrawerList = (ListView)findViewById(R.id.navList);
        //addDrawerItems();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TextView txtVendedor = (TextView) findViewById(R.id.txtNombreVendedor);
        txtVendedor.setText(((Global) getBaseContext().getApplicationContext()).getUsername());
        return true;
    }

    private void setTabs(TabLayout tabLayout){
        String[] days = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        for(int i = 0; i < 7; ++i) {
            tabLayout.addTab(tabLayout.newTab().setText(days[i]));
        }
        tabLayout.addTab(tabLayout.newTab().setText(R.string.products_tab));
    }

    private ListView mDrawerList;


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Hacer algo con cada botón del menú
        switch (item.getItemId()) {
            case R.id.nav_fuera_ruta: {
                Intent intent = new Intent(getBaseContext().getApplicationContext(), OffRouteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().getApplicationContext().startActivity(intent);
                return true;
            }
            case R.id.nav_resumen: {
                Intent intent = new Intent(getBaseContext().getApplicationContext(), Summary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().getApplicationContext().startActivity(intent);
                return true;
            }
            case R.id.nav_order_history: {
                Intent intent = new Intent(getBaseContext().getApplicationContext(), OrderHistory.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().getApplicationContext().startActivity(intent);
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
}
