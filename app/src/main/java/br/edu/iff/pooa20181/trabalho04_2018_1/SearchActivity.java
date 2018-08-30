package br.edu.iff.pooa20181.trabalho04_2018_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.typeSpinner = findViewById(R.id.typeSpinner);

        String[] types = new String[]{"Carro","Moto","Caminhão"};
        ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, types);

        this.typeSpinner.setAdapter(adpt);

        this.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this,"Selecionou!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentArea, new CarroFragment())
                    .commit();
        }

    }
}
