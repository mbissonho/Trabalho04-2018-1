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

public class CaminhaoFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_caminhao, container, false);
        this.bind(view);

        FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);
        Call<List<Marca>> listMarcaCall = fipeUserAPI.getMarcas("caminhoes");
        listMarcaCall.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {
                int code = response.code();
                if(code == 200){
                    CaminhaoFragment.this.listMarca = response.body();

                    int size = CaminhaoFragment.this.listMarca.size();
                    String[] brands  = new String[size];

                    for(int i = 0; i < size; i++){
                        brands[i] = CaminhaoFragment.this.listMarca.get(i).getNome();
                    }

                    ArrayAdapter<String> adpt = new ArrayAdapter<String>(CaminhaoFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, brands);

                    CaminhaoFragment.this.brandSpinner.setAdapter(adpt);

                }else {
                    Toast.makeText(CaminhaoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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

        Toast.makeText(CaminhaoFragment.this.ctx, "Aguarde...",Toast.LENGTH_SHORT).show();
    }

    private void setListeners(){
        this.brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(CaminhaoFragment.this.ctx, "Marca selecionada!",Toast.LENGTH_SHORT).show();

                CaminhaoFragment.this.getCurrentMarca();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);
                Call<ModeloAno> listModeloCall = fipeUserAPI.getModelos("caminhoes", Integer.toString(CaminhaoFragment.this.currentBrand.getCodigo()));
                listModeloCall.enqueue(new Callback<ModeloAno>() {
                    @Override
                    public void onResponse(Call<ModeloAno> call, Response<ModeloAno> response) {
                        int code = response.code();
                        if(code == 200){
                            ModeloAno modeloAno = response.body();
                            CaminhaoFragment.this.listModelo = modeloAno.getModelos();
                            int size = CaminhaoFragment.this.listModelo.size();
                            String[] models  = new String[size];
                            for(int i = 0; i < size; i++){
                                models[i] = CaminhaoFragment.this.listModelo.get(i).getNome();
                            }
                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(CaminhaoFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, models);
                            CaminhaoFragment.this.modelSpinner.setAdapter(adpt);
                        }else {
                            Toast.makeText(CaminhaoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CaminhaoFragment.this.ctx, "Modelo selecionado!",Toast.LENGTH_SHORT).show();

                CaminhaoFragment.this.getCurrentModelo();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);

                String marca, modelo;
                marca = Integer.toString(CaminhaoFragment.this.currentBrand.getCodigo());
                modelo = Integer.toString(CaminhaoFragment.this.currentModel.getCodigo());

                Call<List<Ano>> listAnoCall = fipeUserAPI.getAnos("caminhoes",marca,modelo);

                listAnoCall.enqueue(new Callback<List<Ano>>() {
                    @Override
                    public void onResponse(Call<List<Ano>> call, Response<List<Ano>> response) {
                        int code = response.code();
                        if(code == 200){
                            CaminhaoFragment.this.listAno = response.body();
                            int size = CaminhaoFragment.this.listAno.size();
                            String[] years  = new String[size];

                            for(int i = 0; i < size; i++){
                                years[i] = CaminhaoFragment.this.listAno.get(i).getNome();
                            }
                            ArrayAdapter<String> adpt = new ArrayAdapter<String>(CaminhaoFragment.this.ctx, R.layout.support_simple_spinner_dropdown_item, years);
                            CaminhaoFragment.this.yearSpinner.setAdapter(adpt);

                            CaminhaoFragment.this.unlock();

                        }else {
                            Toast.makeText(CaminhaoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CaminhaoFragment.this.ctx, "Veículo selecionado!",Toast.LENGTH_SHORT).show();

                CaminhaoFragment.this.getCurrentAno();

                FIPEUserAPI fipeUserAPI = FIPEUserAPI.retrofit.create(FIPEUserAPI.class);

                String marca, modelo, ano;
                marca = Integer.toString(CaminhaoFragment.this.currentBrand.getCodigo());
                modelo = Integer.toString(CaminhaoFragment.this.currentModel.getCodigo());
                ano = CaminhaoFragment.this.currentYear.getCodigo();

                Call<Veiculo> veiculoCall = fipeUserAPI.getVeiculo("caminhoes",marca,modelo,ano);

                veiculoCall.enqueue(new Callback<Veiculo>() {
                    @Override
                    public void onResponse(Call<Veiculo> call, Response<Veiculo> response) {
                        int code = response.code();
                        if(code == 200){
                            CaminhaoFragment.this.veiculo = response.body();
                            Veiculo veiculo = CaminhaoFragment.this.veiculo;

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

                            dialog = new AlertDialog.Builder(CaminhaoFragment.this.ctx);
                            dialog.setTitle("Caminhão Pesquisado");
                            dialog.setMessage(message);
                            dialog.setNeutralButton("OK",null);
                            dialog.show();

                        }else {
                            Toast.makeText(CaminhaoFragment.this.ctx, "Erro. Não foi possível obter as informações!",Toast.LENGTH_SHORT).show();
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
