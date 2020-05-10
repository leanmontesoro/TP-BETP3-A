package com.example.api_rest_call;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateCarActivity extends AppCompatActivity {

    AutoService conn;
    EditText marca;
    EditText modelo;
    Button crear;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
        //conn = (AutoService) getIntent().getSerializableExtra("connection");
        retrofit = new AdaptadorRetrofit().getAdaptador();
        conn = retrofit.create(AutoService.class);
        marca = findViewById(R.id.marcaValue);
        modelo = findViewById(R.id.modeloValue);
        crear = findViewById(R.id.createCar);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marca.getText().toString().equals("Ingresa una Marca") || marca.getText().toString().equals("") || modelo.getText().toString().equals("Ingresa un Modelo") || modelo.getText().toString().equals("")){
                    Toast.makeText(CreateCarActivity.this,"Completa ambos campos correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    crearAuto(marca.getText().toString(), modelo.getText().toString());
                }
            }
        });
    }

    public void crearAuto (String marca, String modelo) {

        Auto auto = new Auto("", marca, modelo);
        Call<Void> call = this.conn.agregarAuto(auto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(CreateCarActivity.this, "VEHICULO CREADO", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(CreateCarActivity.this, "Ocurrio un error al crear el vehiculo. Intenta nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreateCarActivity.this, "Ocurrio un error interno. Intenta nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}