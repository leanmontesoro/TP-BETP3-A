package com.example.api_rest_call;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AutoService {
    /*
    CREATE: https://us-central1-be-tp3-a.cloudfunctions.net/app/api/create
    UPDATE: https://us-central1-be-tp3-a.cloudfunctions.net/app/api/update/{id}
    DELETE: https://us-central1-be-tp3-a.cloudfunctions.net/app/api/delete/{id}
    READ: https://us-central1-be-tp3-a.cloudfunctions.net/app/api/read/{id}
    READ ALL: https://us-central1-be-tp3-a.cloudfunctions.net/app/api/read/
    */

    /* Definicion de rutas*/
    String READ_ALL= "app/api/read";
    String CREATE= "app/api/create";
    String READ = "app/api/read/{id}";
    String UPDATE= "app/api/update/{id}";
    String DEL= "app/api/delete/{id}";


    /**
     * Metodo abstracto para utilizar HTTP.GET
     * @return
     */
    @GET(READ_ALL)
    Call<List<Auto>> getAutos();

    @GET(READ)
    Call<Auto> getAuto(
            @Path("id") String id
    );

    @POST(CREATE)
    Call<Void> agregarAuto(
            @Body Auto auto
    );

    @PUT(UPDATE)
    Call<Void> actualizarAuto(
            @Path("id") String id,
            @Body Auto auto
    );

    @DELETE(DEL)
    Call<Void> eliminarAuto(
            @Path("id") String id
    );
}