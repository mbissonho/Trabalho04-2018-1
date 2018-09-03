package br.edu.iff.pooa20181.trabalho04_2018_1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.edu.iff.pooa20181.trabalho04_2018_1.model.Ano;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Marca;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Model;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Modelo;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.ModeloAno;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Veiculo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarroFragment extends Fragment {

    private Button btnView;

    private Context ctx;
    private List<Marca> listMarca = null;
    private List<Modelo> listModelo = null;
    private List<Ano> listAno = null;

    private Veiculo veiculo;

    private Spinner brandSpinner;
    private Spinner modelSpinner;
    private Spinner yearSpinner;

    private Marca currentBrand = null;
    private Modelo currentModel = null;
    private Ano currentYear = null;

    private AlertDialog.Builder dialog;

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
                    CarroFragment.this.listMarca = response.body();

                    int size = CarroFragment.this.listMarca.size();
                    String[] brands  = new String[size];

                    for(int i = 0; i < size; i++){
                        brands[i] = CarroFragment.this.listMarca.get(i).getNome();
                    }

                    ArrayAdapter<String> adpt = new ArrayAdapter<String>(CarroFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, brands);

                    CarroFragment.this.brandSpinner.setAdapter(adpt);

                }else {
                    Toast.makeText(CarroFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
        this.btnView = view.findViewById(R.id.btnView);
        this.brandSpinner = view.findViewById(R.id.brandSpinner);
        this.modelSpinner = view.findViewById(R.id.modelSpinner);
        this.yearSpinner = view.findViewById(R.id.yearSpinner);

        this.btnView.setEnabled(false);
        this.brandSpinner.setEnabled(false);
        this.modelSpinner.setEnabled(false);
        this.yearSpinner.setEnabled(false);

        Toast.makeText(CarroFragment.this.ctx, "Aguarde...",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CarroFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
                            CarroFragment.this.listAno = response.body();
                            int size = CarroFragment.this.listAno.size();
                            String[] years  = new String[size];

                            for(int i = 0; i < size; i++){
                                years[i] = CarroFragment.this.listAno.get(i).getNome();
                            }
                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(CarroFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, years);
                            CarroFragment.this.yearSpinner.setAdapter(adpt);

                            CarroFragment.this.unlock();

                        }else {
                            Toast.makeText(CarroFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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

        this.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CarroFragment.this.ctx, "Veículo selecionado!",Toast.LENGTH_SHORT).show();

                CarroFragment.this.getCurrentAno();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);

                String marca, modelo, ano;
                marca = Integer.toString(CarroFragment.this.currentBrand.getCodigo());
                modelo = Integer.toString(CarroFragment.this.currentModel.getCodigo());
                ano = CarroFragment.this.currentYear.getCodigo();

                Call<Veiculo> veiculoCall = fipeUserAPI.getVeiculo("carros",marca,modelo,ano);

                veiculoCall.enqueue(new Callback<Veiculo>() {
                    @Override
                    public void onResponse(Call<Veiculo> call, Response<Veiculo> response) {
                        int code = response.code();
                        if(code == 200){
                            CarroFragment.this.veiculo = response.body();
                            Veiculo veiculo = CarroFragment.this.veiculo;

                            String message =
                            "Valor: "+veiculo.getValor()+"\n"+
                            "Marca: "+veiculo.getMarca()+"\n"+
                            "Modelo: "+veiculo.getModelo()+"\n"+
                            "AnoModelo: "+veiculo.getAnoModelo()+"\n"+
                            "Combustivel: "+veiculo.getCombustivel()+"\n"+
                            "CodigoFipe: "+veiculo.getCodigoFipe()+"\n"+
                            "MesReferencia: "+veiculo.getMesReferencia()+"\n"+
                            "TipoVeiculo: "+veiculo.getTipoVeiculo()+"\n"+
                            "SiglaCombustivel: "+veiculo.getSiglaCombustivel();

                            dialog = new AlertDialog.Builder(CarroFragment.this.ctx);
                            dialog.setTitle("Carro Pesquisado");
                            dialog.setMessage(message);
                            dialog.setNeutralButton("OK",null);
                            dialog.show();

                        }else {
                            Toast.makeText(CarroFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Veiculo> call, Throwable t) {
                        Log.d("FAILURE: ",t.getMessage());
                    }
                });
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

    private void getCurrentAno(){
        for(Ano year : this.listAno){
            if(this.yearSpinner.getSelectedItem().toString().equals(year.getNome().toString())){
                this.currentYear = year;
            }
        }
    }

    private void lock(){
        this.btnView.setEnabled(false);
        this.brandSpinner.setEnabled(false);
        this.modelSpinner.setEnabled(false);
        this.yearSpinner.setEnabled(false);
    }

    private void unlock(){
        this.btnView.setEnabled(true);
        this.brandSpinner.setEnabled(true);
        this.modelSpinner.setEnabled(true);
        this.yearSpinner.setEnabled(true);
    }


}
