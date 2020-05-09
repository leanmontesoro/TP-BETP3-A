package com.example.api_rest_call;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SearchedCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_car);

        if(getIntent().hasExtra("searchedAuto")){
            TextView searchedModelo = (TextView) findViewById(R.id.searchedMarcaTxt);
            TextView searchedMarca = (TextView) findViewById(R.id.searchedModeloTxt);
            Auto searchedAuto = (Auto) getIntent().getSerializableExtra("searchedAuto");
            searchedModelo.append(searchedAuto.getModelo());
            searchedMarca.append(searchedAuto.getMarca());
        }
    }
}
