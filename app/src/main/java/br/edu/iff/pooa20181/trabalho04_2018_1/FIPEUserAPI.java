package br.edu.iff.pooa20181.trabalho04_2018_1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FIPEUserAPI {

    @GET("{tipo}/marcas")
    Call<List<Marca>> getMarcas(@Path("tipo") String tipo);

    @GET("{tipo}/marcas/{codigo}/modelos")
    Call<ModeloAno> getModelos(@Path("tipo") String tipo, @Path("codigo") String codigo);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://parallelum.com.br/fipe/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
