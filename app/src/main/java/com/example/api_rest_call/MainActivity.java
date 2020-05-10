package com.example.api_rest_call;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText idgenerico;
    Button btadd,btdel,btedit,btbuscar;
    ListView list;
    ListAdapter adaptador;
    ArrayList<String> autos = new ArrayList<>();
    Retrofit retrofit;
    AutoService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Traigo los botones del xml*/
        final TextView idgenerico =(EditText) findViewById(R.id.idgenerico);
        btadd = findViewById(R.id.btadd);
        btdel = findViewById(R.id.btdel);
        btedit = findViewById(R.id.btedit);
        btbuscar = findViewById(R.id.btnbuscar);
        retrofit = new AdaptadorRetrofit().getAdaptador();
        api = retrofit.create(AutoService.class);
        this.getListadoVehiculos();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autos);
        list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adaptador);

        this.getListadoVehiculos();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.getItemAtPosition(position);

            }
        });


        btdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this,idgenerico.getText().toString(),Toast.LENGTH_SHORT).show();
                if(idgenerico.getText().toString().equals("ID") || idgenerico.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Inserte un ID a eliminar",Toast.LENGTH_SHORT).show();
                } else {

                    //api.eliminarAuto("0a7fe5fc-d8b4-4c41-9f66-053dbc64152b");
                    eliminarAuto(api,"0a7fe5fc-d8b4-4c41-9f66-053dbc64152b");
                }
            }
        });

        btbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idgenerico.getText().toString().equals("ID") || idgenerico.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Inserte un ID",Toast.LENGTH_SHORT).show();
                } else {
                    idgenerico.setText("");
                    buscarAuto(api,"0a7fe5fc-d8b4-4c41-9f66-053dbc64152b");
                }
            }
        });

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createCarIntent = new Intent(getApplicationContext(), CreateCarActivity.class);
                //createCarIntent.putExtra("connection", (Parcelable) api);
                startActivity(createCarIntent);
            }
        });

    }

    public void buscarAuto(final AutoService api, String idAuto) {
        autos.clear();
        Call<Auto> call = api.getAuto(idAuto);

        call.enqueue(new Callback<Auto>() {
            @Override
            public void onResponse(Call<Auto> call, Response<Auto> response) {

                if(response.code() == 200){
                    Intent searchedCarIntent = new Intent(getApplicationContext(), SearchedCarActivity.class);
                    searchedCarIntent.putExtra("searchedAuto", (Serializable) response.body());
                    startActivity(searchedCarIntent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Auto> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falló conexión con API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void eliminarAuto(final AutoService api, final String idAuto) {
        autos.clear();
        Call<Auto> call = api.getAuto(idAuto);
        //Call<Auto> call = api.eliminarAuto(idAuto);


        //Toast.makeText(MainActivity.this,"Entre al metodo",Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<Auto>() {
            @Override
            public void onResponse(Call<Auto> call, Response<Auto> response) {


                switch (response.code()) {
                    case 200:
                        autos.remove(idAuto);
                        Toast.makeText(MainActivity.this, "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                        // idgenerico.setText("ID");
                        getListadoVehiculos();
                        break;
                    case 204:
                        Toast.makeText(MainActivity.this, "No se elimino el registro", Toast.LENGTH_SHORT).show();
                      //  idgenerico.setText("");
                        break;

                }
            }

            @Override
            public void onFailure(Call<Auto> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falló conexión con API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListadoVehiculos(){

       // Defnimos la interfaz para que utilice la base retrofit de mi aplicacion ()
        AutoService autoService = retrofit.create(AutoService.class);


        Call<List<Auto>> http_call = autoService.getAutos();

        http_call.enqueue(new Callback<List<Auto>>() {
            @Override
            public void onResponse(Call<List<Auto>> call, Response<List<Auto>> response) {
                // Si el servidor responde correctamente puedo hacer uso de la respuesta esperada:
                autos.clear();

                for (Auto auto: response.body()){
                    autos.add(auto.getMarca() + " - " + auto.getModelo());
                }

                // Aviso al base adapter que cambio mi set de datos.
                // Renderizacion general de mi ListView
                ((BaseAdapter) adaptador).notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Auto>> call, Throwable t) {
                // SI el servidor o la llamada no puede ejecutarse, muestro un mensaje de eror:
                Toast.makeText(MainActivity.this,"Hubo un error con la llamada a la API", Toast.LENGTH_LONG);

            }
        });

    }
}
