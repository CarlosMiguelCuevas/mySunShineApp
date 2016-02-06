package mx.com.cubosoft.sunshineapp_nueva;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {



        Toast.makeText(context, "CAMBIO LA CONEXION", Toast.LENGTH_LONG).show();
    }
}
