package br.edu.iff.pooa20181.trabalho04_2018_1;

import java.util.List;

import br.edu.iff.pooa20181.trabalho04_2018_1.model.Ano;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Marca;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Model;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.ModeloAno;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Veiculo;
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

    @GET("{tipo}/marcas/{codigo}/modelos/{codmodelo}/anos")
    Call<List<Ano>> getAnos(@Path("tipo") String tipo, @Path("codigo") String codigo, @Path("codmodelo") String codmodelo);

    @GET("{tipo}/marcas/{codigo}/modelos/{codmodelo}/anos/{ano}")
    Call<Veiculo> getVeiculo(@Path("tipo") String tipo, @Path("codigo") String codigo, @Path("codmodelo") String codmodelo, @Path("ano") String ano);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://parallelum.com.br/fipe/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
