package mx.com.cubosoft.sunshineapp_nueva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_settings)
        {
            Intent settings_intent = new Intent(this,SettingsActivity.class);
            startActivity(settings_intent);
            return true;
        }
        else if(id == R.id.map)
        {
            showMap();

        }

        return super.onOptionsItemSelected(item);
    }

    public void showMap() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String location = pref.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_defaut));
        String tittle = getString(R.string.chooser_tittle);
        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent intentMap = new Intent(Intent.ACTION_VIEW);

        Intent chooser = Intent.createChooser(intentMap,tittle);

        intentMap.setData(uri);

        if(intentMap.resolveActivity(getPackageManager()) != null)
        {
            startActivity(chooser);
        }
        else
        {
            Toast.makeText(MainActivity.this, "No se puede mostrar el mapa", Toast.LENGTH_SHORT).show();
        }

    }
}
