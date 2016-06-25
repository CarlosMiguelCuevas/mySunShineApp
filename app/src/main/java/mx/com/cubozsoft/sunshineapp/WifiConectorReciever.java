package mx.com.cubozsoft.sunshineapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import mx.com.cubozsoft.sunshineapp.listaPrincipal.ListOfWeather;
import mx.com.cubozsoft.sunshineapp.listaPrincipal.MainActivity;

/**
 * Created by carlos on 24/06/16.
 */
public class WifiConectorReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            boolean isWIFI = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

            if(isWIFI){
                Log.v("puto","conectado");
                Toast.makeText(context, "it is connected to wifi now, starting downoad", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
