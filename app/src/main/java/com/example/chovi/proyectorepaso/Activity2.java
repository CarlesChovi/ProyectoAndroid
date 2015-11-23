package com.example.chovi.proyectorepaso;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


/**
 * Created by Chovi on 4/11/15.
 */
public class Activity2 extends Activity {

    private Spinner sp_P;
    private Spinner sp_C;
    private Context context;
    private Button continuar;
    private EditText nom;
    private Switch notificacion;
    private static final int ALERTA_ID = 1;
    private String provincia;
    private String ciudad;
    private int contador;

    final int SUBACTIVITY_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);

        sp_P = (Spinner) findViewById(R.id.provincias);
        sp_C = (Spinner) findViewById(R.id.pueblos);
        context = this;
        continuar = (Button) findViewById(R.id.btn_continuar);
        nom = (EditText) findViewById(R.id.editText);
        notificacion = (Switch) findViewById(R.id.noti);

        final ArrayAdapter<CharSequence> provincias = ArrayAdapter.createFromResource(this, R.array.provincias, android.R.layout.simple_spinner_item);
        provincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_P.setAdapter(provincias);

        // Recollim els paràmatres que venen de l'Activity principal (si en ve algun)
        Bundle b = getIntent().getExtras();
        if (b != null) {
            contador = b.getInt("Contador");

        }


        sp_P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TypedArray loc = getResources().obtainTypedArray(R.array.array_provincia_a_localidades);
                String localidades[] = getResources().getStringArray(loc.getResourceId(position, 0));

                ArrayAdapter ciudades = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, localidades);
                ciudades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_C.setAdapter(ciudades);
                provincia = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_C.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ciudad = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (dadesCorrectes()) {
                                                 //Cridaré al meu Subactivity
                                                 Intent intent = new Intent(getApplicationContext(), MainActivityReset.class);
                                                 //Cree un objecte Bundle per a enviar els paràmetres
                                                 Bundle b = new Bundle();
                                                 b.putString("Nom", nom.getText().toString());  // Afegim el paràmetre NOM
                                                 b.putString("Notificacion", String.valueOf(notificacion.onCheckIsTextEditor()));
                                                 b.putString("Provincia", String.valueOf(provincia));
                                                 b.putString("Ciudad", String.valueOf(ciudad));
                                                 b.putInt("Contador", Integer.valueOf(contador));
                                                 startActivity(intent);

                                                 if (notificacion.isChecked()) {
                                                     b.putString("Notificacion", "Enviar");
                                                 } else {
                                                     b.putString("Notificacion", "No enviar");
                                                 }
                                                 intent.putExtras(b);  //Afegisc l'objecte Bundle a l'Intent
                                                 setResult(RESULT_OK);
                                                 startActivity(intent); // Cride al subactivity, amb l'Intent (que conté el Bundle)
                                                 finish();
                                             } else {
                                                 Toast.makeText(getApplicationContext(), "Revisa les dades. Ompli-les TOTES", Toast.LENGTH_LONG).show();
                                             }
                                         }
                                     }
        );
    }

    public boolean dadesCorrectes() {
        if (nom.getText().length() <= 0) {
            return false;
        }
        return true;
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
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(nom.getText().toString())
                .setContentText("Notificación 1")
                .setTicker("Alerta!");

        Intent notificacion = new Intent(this, MainActivityReset.class);

        PendingIntent conIntent = PendingIntent.getActivity(Activity2.this, 0, notificacion, 0);

        mBuilder.setContentIntent(conIntent);

        NotificationManager noti=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        noti.notify(ALERTA_ID, mBuilder.build());
    }
}
