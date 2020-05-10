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
    Button btadd, btdel, btedit, btbuscar;
    ListView list;
    ListAdapter adaptador;
    ArrayList<String> autos = new ArrayList<>();
    Retrofit retrofit;
    AutoService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idgenerico = findViewById(R.id.idgenerico);
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

        btdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idgenerico.getText().toString().equals("ID") || idgenerico.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Inserte un ID a eliminar",Toast.LENGTH_SHORT).show();
                } else {
                    eliminarAuto(api, idgenerico.getText().toString());
                }
            }
        });

        btbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idgenerico.getText().toString().equals("ID") || idgenerico.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Inserte un ID a buscar",Toast.LENGTH_SHORT).show();
                } else {
                    buscarAuto(api, idgenerico.getText().toString());
                }
            }
        });

        btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idgenerico.getText().toString().equals("ID") || idgenerico.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Inserte un ID a editar",Toast.LENGTH_SHORT).show();
                } else {
                    editarAuto(api, idgenerico.getText().toString());
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
                    Toast.makeText(MainActivity.this, "Ocurrio un error interno. Por favor, intenta nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Auto> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ocurrio un error interno. Por favor, intenta nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editarAuto(final AutoService api, String idAuto) {

        autos.clear();
        Call<Auto> call = api.getAuto(idAuto);

        call.enqueue(new Callback<Auto>() {
            @Override
            public void onResponse(Call<Auto> call, Response<Auto> response) {
                if(response.code() == 200){
                    if (response.body().getId() == null) {
                        Toast.makeText(MainActivity.this, "El ID del vehiculo ingresado no existe", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent updateCarIntent = new Intent(getApplicationContext(), UpdateCarActivity.class);
                        updateCarIntent.putExtra("updateAuto", (Serializable) response.body());
                        startActivity(updateCarIntent);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Ocurrio un error interno. Por favor, intenta nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Auto> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ocurrio un error interno. Por favor, intenta nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void eliminarAuto(final AutoService api, String idAuto) {

        autos.clear();
        Call<Void> call = api.eliminarAuto(idAuto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "El vehiculo se elimino correctamente", Toast.LENGTH_SHORT).show();
                    getListadoVehiculos();
                }
                else {
                    Toast.makeText(MainActivity.this, "No se pudo eliminar el vehiculo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ocurrio un error interno. Por favor, intenta nuevamente", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this,"Ocurrio un error interno. Por favor, intenta nuevamente", Toast.LENGTH_LONG);
            }
        });

    }
}
