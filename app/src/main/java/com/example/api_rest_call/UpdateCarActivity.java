package com.example.api_rest_call;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateCarActivity extends AppCompatActivity {

    AutoService conn;
    String autoID;
    EditText marca;
    EditText modelo;
    Button update;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        retrofit = new AdaptadorRetrofit().getAdaptador();
        conn = retrofit.create(AutoService.class);
        marca = findViewById(R.id.newMarcaValue);
        modelo = findViewById(R.id.newModeloValue);
        update = findViewById(R.id.updateButton);

        if(getIntent().hasExtra("updateAuto")){
            Auto searchedAuto = (Auto) getIntent().getSerializableExtra("updateAuto");
            autoID = searchedAuto.getId();
            marca.append(searchedAuto.getMarca());
            modelo.append(searchedAuto.getModelo());
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marca.getText().toString().equals("") || modelo.getText().toString().equals("")){
                    Toast.makeText(UpdateCarActivity.this,"Completa ambos campos correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    modificarAuto(autoID, marca.getText().toString(), modelo.getText().toString());
                }
            }
        });

    }

    public void modificarAuto (String autoID, String marca, String modelo) {

        Auto auto = new Auto(autoID, marca, modelo);
        Call<Void> call = this.conn.actualizarAuto(autoID, auto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(UpdateCarActivity.this, "VEHICULO MODIFICADO", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UpdateCarActivity.this, "Ocurrio un error al modificar el vehiculo. Intenta nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateCarActivity.this, "Ocurrio un error interno. Intenta nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
