package mx.com.cubozsoft.sunshineapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState == null)
        {
            Bundle forecasBundle = getIntent().getExtras();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detailRoot,DetailFragment.newInstance(forecasBundle),"detailforecast")
                    .commit();
        }


    }
}
