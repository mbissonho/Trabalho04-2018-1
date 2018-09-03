package br.edu.iff.pooa20181.trabalho04_2018_1;

import android.app.AlertDialog;
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
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Modelo;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.ModeloAno;
import br.edu.iff.pooa20181.trabalho04_2018_1.model.Veiculo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotoFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_moto, container, false);

        this.bind(view);

        FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);
        Call<List<Marca>> listMarcaCall = fipeUserAPI.getMarcas("motos");
        listMarcaCall.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {
                int code = response.code();
                if(code == 200){
                    MotoFragment.this.listMarca = response.body();

                    int size = MotoFragment.this.listMarca.size();
                    String[] brands  = new String[size];

                    for(int i = 0; i < size; i++){
                        brands[i] = MotoFragment.this.listMarca.get(i).getNome();
                    }

                    ArrayAdapter<String> adpt = new ArrayAdapter<String>(MotoFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, brands);

                    MotoFragment.this.brandSpinner.setAdapter(adpt);

                }else {
                    Toast.makeText(MotoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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

        Toast.makeText(MotoFragment.this.ctx, "Aguarde...",Toast.LENGTH_SHORT).show();
    }

    private void setListeners(){
        this.brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MotoFragment.this.ctx, "Marca selecionada!",Toast.LENGTH_SHORT).show();

                MotoFragment.this.getCurrentMarca();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);
                Call<ModeloAno> listModeloCall = fipeUserAPI.getModelos("motos", Integer.toString(MotoFragment.this.currentBrand.getCodigo()));
                listModeloCall.enqueue(new Callback<ModeloAno>() {
                    @Override
                    public void onResponse(Call<ModeloAno> call, Response<ModeloAno> response) {
                        int code = response.code();
                        if(code == 200){
                            ModeloAno modeloAno = response.body();
                            MotoFragment.this.listModelo = modeloAno.getModelos();
                            int size = MotoFragment.this.listModelo.size();
                            String[] models  = new String[size];
                            for(int i = 0; i < size; i++){
                                models[i] = MotoFragment.this.listModelo.get(i).getNome();
                            }
                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(MotoFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, models);
                            MotoFragment.this.modelSpinner.setAdapter(adpt);
                        }else {
                            Toast.makeText(MotoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MotoFragment.this.ctx, "Modelo selecionado!",Toast.LENGTH_SHORT).show();

                MotoFragment.this.getCurrentModelo();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);

                String marca, modelo;
                marca = Integer.toString(MotoFragment.this.currentBrand.getCodigo());
                modelo = Integer.toString(MotoFragment.this.currentModel.getCodigo());

                Call<List<Ano>> listAnoCall = fipeUserAPI.getAnos("motos",marca,modelo);

                listAnoCall.enqueue(new Callback<List<Ano>>() {
                    @Override
                    public void onResponse(Call<List<Ano>> call, Response<List<Ano>> response) {
                        int code = response.code();
                        if(code == 200){
                            MotoFragment.this.listAno = response.body();
                            int size = MotoFragment.this.listAno.size();
                            String[] years  = new String[size];

                            for(int i = 0; i < size; i++){
                                years[i] = MotoFragment.this.listAno.get(i).getNome();
                            }
                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(MotoFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, years);
                            MotoFragment.this.yearSpinner.setAdapter(adpt);

                            MotoFragment.this.unlock();

                        }else {
                            Toast.makeText(MotoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MotoFragment.this.ctx, "Veículo selecionado!",Toast.LENGTH_SHORT).show();

                MotoFragment.this.getCurrentAno();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);

                String marca, modelo, ano;
                marca = Integer.toString(MotoFragment.this.currentBrand.getCodigo());
                modelo = Integer.toString(MotoFragment.this.currentModel.getCodigo());
                ano = MotoFragment.this.currentYear.getCodigo();

                Call<Veiculo> veiculoCall = fipeUserAPI.getVeiculo("motos",marca,modelo,ano);

                veiculoCall.enqueue(new Callback<Veiculo>() {
                    @Override
                    public void onResponse(Call<Veiculo> call, Response<Veiculo> response) {
                        int code = response.code();
                        if(code == 200){
                            MotoFragment.this.veiculo = response.body();
                            Veiculo veiculo = MotoFragment.this.veiculo;

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

                            dialog = new AlertDialog.Builder(MotoFragment.this.ctx);
                            dialog.setTitle("Motos Pesquisada");
                            dialog.setMessage(message);
                            dialog.setNeutralButton("OK",null);
                            dialog.show();

                        }else {
                            Toast.makeText(MotoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
