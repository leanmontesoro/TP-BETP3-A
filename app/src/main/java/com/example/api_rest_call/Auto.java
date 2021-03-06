package com.example.api_rest_call;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Auto implements Serializable {
    private String id;
    private String marca;
    private String modelo;

    public Auto(String id, String marca, String modelo) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getMarca() {

        return marca;
    }

    public void setMarca(String marca) {

        this.marca = marca;
    }

    public String getModelo() {

        return modelo;
    }

    public void setModelo(String modelo) {

        this.modelo = modelo;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}