package br.edu.iff.pooa20181.trabalho04_2018_1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.edu.iff.pooa20181.trabalho04_2018_1.model.Ano;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Marca;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Model;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Modelo;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.ModeloAno;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarroFragment extends Fragment {

    private Context ctx;
    private List<Marca> listMarca = null;
    private List<Modelo> listModelo = null;
    private List<Ano> listAno = null;

    private Spinner brandSpinner;
    private Spinner modelSpinner;
    private Spinner yearSpinner;

    private Marca currentBrand = null;
    private Modelo currentModel = null;
    private Ano currentAno = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carro, container, false);
        this.bind(view);

        FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);
        Call<List<Marca>> listMarcaCall = fipeUserAPI.getMarcas("carros");
        listMarcaCall.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {
                int code = response.code();
                if(code == 200){
                    Toast.makeText(CarroFragment.this.ctx, "Funcionou!",Toast.LENGTH_SHORT).show();

                    CarroFragment.this.listMarca = response.body();

                    int size = CarroFragment.this.listMarca.size();
                    String[] brands  = new String[size];

                    for(int i = 0; i < size; i++){
                        brands[i] = CarroFragment.this.listMarca.get(i).getNome();
                    }

                    ArrayAdapter<String> adpt = new ArrayAdapter<String>(CarroFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, brands);

                    CarroFragment.this.brandSpinner.setAdapter(adpt);

                }else {
                    Toast.makeText(CarroFragment.this.ctx, "Deu Ruim!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable t) {

            }


        });

        this.setListeners();

        return view;
    }

    private void bind(View view){
        this.ctx = getContext();
        this.brandSpinner = view.findViewById(R.id.brandSpinner);
        this.modelSpinner = view.findViewById(R.id.modelSpinner);
        this.yearSpinner = view.findViewById(R.id.yearSpinner);
    }

    private void setListeners(){
        this.brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(CarroFragment.this.ctx, "Marca selecionada!",Toast.LENGTH_SHORT).show();

                CarroFragment.this.getCurrentMarca();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);
                Call<ModeloAno> listModeloCall = fipeUserAPI.getModelos("carros", Integer.toString(CarroFragment.this.currentBrand.getCodigo()));
                listModeloCall.enqueue(new Callback<ModeloAno>() {
                    @Override
                    public void onResponse(Call<ModeloAno> call, Response<ModeloAno> response) {
                        int code = response.code();
                        if(code == 200){
                            Toast.makeText(CarroFragment.this.ctx, "200!",Toast.LENGTH_SHORT).show();

                            ModeloAno modeloAno = response.body();

                            CarroFragment.this.listModelo = modeloAno.getModelos();
                            int size = CarroFragment.this.listModelo.size();
                            String[] models  = new String[size];

                            for(int i = 0; i < size; i++){
                                models[i] = CarroFragment.this.listModelo.get(i).getNome();
                            }

                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(CarroFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, models);

                            CarroFragment.this.modelSpinner.setAdapter(adpt);

                        }else {
                            Toast.makeText(CarroFragment.this.ctx, "Deu Ruim!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModeloAno> call, Throwable t) {
                        Log.d("FAILURE: ",t.getMessage());
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        this.modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CarroFragment.this.ctx, "Modelo selecionado!",Toast.LENGTH_SHORT).show();

                CarroFragment.this.getCurrentModelo();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);

                String marca, modelo;
                marca = Integer.toString(CarroFragment.this.currentBrand.getCodigo());
                modelo = Integer.toString(CarroFragment.this.currentModel.getCodigo());

                Call<List<Ano>> listAnoCall = fipeUserAPI.getAnos("carros",marca,modelo);

                listAnoCall.enqueue(new Callback<List<Ano>>() {
                    @Override
                    public void onResponse(Call<List<Ano>> call, Response<List<Ano>> response) {
                        int code = response.code();
                        if(code == 200){
                            Toast.makeText(CarroFragment.this.ctx, "200!",Toast.LENGTH_SHORT).show();

                            CarroFragment.this.listAno = response.body();

                            int size = CarroFragment.this.listAno.size();
                            String[] years  = new String[size];

                            for(int i = 0; i < size; i++){
                                years[i] = CarroFragment.this.listAno.get(i).getNome();
                            }

                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(CarroFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, years);

                            CarroFragment.this.yearSpinner.setAdapter(adpt);

                        }else {
                            Toast.makeText(CarroFragment.this.ctx, "Deu Ruim!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Ano>> call, Throwable t) {
                        Log.d("FAILURE: ",t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCurrentMarca(){
        for(Marca brand : this.listMarca){
            if(this.brandSpinner.getSelectedItem().toString().equals(brand.getNome().toString())){
                this.currentBrand = brand;
            }
        }
    }

    private void getCurrentModelo(){
        for(Modelo model : this.listModelo){
            if(this.modelSpinner.getSelectedItem().toString().equals(model.getNome().toString())){
                this.currentModel = model;
            }
        }
    }


}
