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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarroFragment extends Fragment {

    private Context ctx;
    private List<Marca> listMarca = null;
    private List<Modelo> listModelo = null;

    private Spinner brandSpinner;
    private Spinner modelSpinner;

    private Marca currentBrand = null;
    private Modelo currentModel;

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
        
    }

    private void setListeners(){
        this.brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(CarroFragment.this.ctx, "Marca selecionada!",Toast.LENGTH_SHORT).show();

                CarroFragment.this.getCurrentMarca();

                if(CarroFragment.this.currentBrand != null){
                    Toast.makeText(CarroFragment.this.ctx, "NAO NULO: "+Integer.toString(CarroFragment.this.currentBrand.getCodigo()),Toast.LENGTH_SHORT).show();
                }

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
    }

    private void getCurrentMarca(){
        for(Marca brand : this.listMarca){
            if(this.brandSpinner.getSelectedItem().toString().equals(brand.getNome().toString())){
                this.currentBrand = brand;
                Log.d("CURRENT","ESCOLHEU!!");
            }
        }
    }

    private void fillSpinner(Spinner spinner, List<Object> list){

    }

}
