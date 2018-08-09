package com.sensoapps.asenousy.thinkmargin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_SMALL) {
            getSupportActionBar().hide();
        }

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1951501873982763~9479178133");

        final AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bugReport:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Feedback").setMessage("for any issues or suggestions please contact me");

                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hanounasoft@hotmail.com"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "thinkMargin");
                        startActivity(Intent.createChooser(emailIntent, "Send E-Mail"));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}