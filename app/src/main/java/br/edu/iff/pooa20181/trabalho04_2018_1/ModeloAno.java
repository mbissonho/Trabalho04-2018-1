package br.edu.iff.pooa20181.trabalho04_2018_1;

import java.util.List;

public class ModeloAno {

    private List<Modelo> modelos;
    private List<Ano> anos;

    public ModeloAno(){}

    public List<Modelo> getModelos() {
        return modelos;
    }

    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }

    public List<Ano> getAnos() {
        return anos;
    }

    public void setAnos(List<Ano> anos) {
        this.anos = anos;
    }
}
