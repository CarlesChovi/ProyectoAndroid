package com.example.chovi.proyectorepaso;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

    String nom;
    String provincia;
    String ciudad;
    String notificacion;

    Button pulsar;
    TextView contador;
    int cont;
    private static final int ALERTA_ID=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contador = (TextView) findViewById(R.id.id_contador);
        pulsar = (Button) findViewById(R.id.id_pulsar);

        // Recollim els paràmatres que venen de l'Activity principal (si en ve algun)
        Bundle b = getIntent().getExtras();
        if (b != null) {
            nom = b.getString("Nom");
            provincia = b.getString("Provincia");
            ciudad = b.getString("Ciudad");
            notificacion = b.getString("Notificacion");
            cont = b.getInt("Contador");


            if (notificacion.compareTo("Enviar") == 0) {

                Notification.Builder notificacio=new Notification.Builder(this);
                notificacio.setContentTitle("Notificación");
                notificacio.setSmallIcon(android.R.drawable.ic_dialog_alert);
                notificacio.setContentText("Información");
                //Definim l'objecte InboxStyle
                Notification.InboxStyle iS = new Notification.InboxStyle();
                //Li afegim les línies de text que mostrarà quan s'expandisca
                iS.addLine("Su nombre es: "+nom);
                iS.addLine("y vive en: " + ciudad + "(" + provincia + ")");
                //Afegim l'InboxStyle a la notificació
                notificacio.setStyle(iS);



                Intent notificacion = new Intent(this, MainActivity.class);
                PendingIntent conIntent = PendingIntent.getActivity(this, 0, notificacion, PendingIntent.FLAG_UPDATE_CURRENT);
                notificacio.addAction(R.drawable.reload, "Resetear", conIntent);

                Intent notificacion2 = new Intent();
                PendingIntent conIntent2 = PendingIntent.getActivity(this, 0, notificacion2, PendingIntent.FLAG_UPDATE_CURRENT);
                notificacio.addAction(R.drawable.keyboard53, "Continuar", conIntent2);

                NotificationManager noti=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                noti.notify(1, notificacio.build());


                noti.notify(ALERTA_ID, notificacio.build());


            } else {
                Toast.makeText(getApplicationContext(), "Su nombre es: "+nom+" y vive en: "+ciudad+"("+provincia+")", Toast.LENGTH_LONG).show();
            }
        }

        pulsar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cree un objecte Bundle per a enviar els paràmetres
                cont++;

                Bundle b = new Bundle();
                b.putInt("Contador", Integer.valueOf(cont));


                contador.setText("" + cont);
                notificacionOnClick();
                if (cont == 5) {
                    Intent intent = new Intent(getApplicationContext(), Activity2.class);
                    intent.putExtra("Bundle", b);
                    startActivityForResult(intent, 1);
                }

            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void notificacionOnClick(){
        Notification.Builder mBuilder=new Notification.Builder(this);
        mBuilder.setContentTitle("Notificación");
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        mBuilder.setContentText("Has hecho : "+cont+" pulsaciones");

        Intent notificacion = new Intent(this, MainActivity.class);

        PendingIntent conIntent = PendingIntent.getActivity(MainActivity.this, 0, notificacion, 0);

        mBuilder.setContentIntent(conIntent);

        NotificationManager noti=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        noti.notify(ALERTA_ID, mBuilder.build());
    }
    public static class switchButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Here", "I am here");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Bundle b = data.getExtras();
                    int cont = b.getInt("Contador");
                    contador.setText(""+cont);
                }else{
                    Toast.makeText(getApplicationContext(), "Error en el Activity2", Toast.LENGTH_SHORT);
                }
        }
    }
}
