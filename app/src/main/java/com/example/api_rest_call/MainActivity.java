package com.example.api_rest_call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    //btadd btdel btedit idauto

    EditText idgenerico;

    Button btadd,btdel,btedit,btbuscar;
    ListView list;
    ListAdapter adaptador;
    ArrayList<String> autos = new ArrayList<>();
    Retrofit retrofit;
    AutoService api;
    


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        TextView idgenerico =(EditText) findViewById(R.id.idgenerico);
        btadd = findViewById(R.id.btadd);
        btdel = findViewById(R.id.btdel);
        btedit = findViewById(R.id.btedit);
        btbuscar = findViewById(R.id.btnbuscar);
    
        retrofit = new AdaptadorRetrofit().getAdaptador();
        api = retrofit.create(AutoService.class);


        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autos);

        list = (ListView) findViewById(android.R.id.list);

        list.setAdapter(adaptador);

        this.getListadoVehiculos();

        btbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        };




    }

    public void getListadoVehiculos(){


         Call<List<Auto>> http_call = api.getAutos();

        //llamada asincrona
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
