package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import project.waterate.R;

public class TarifActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tarif);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        final ImageView imagetarif = (ImageView) findViewById(R.id.image_tarif);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    imagetarif.setImageResource(R.drawable.data1);
                } else {
                    imagetarif.setImageResource(R.drawable.data2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList <String>();
        categories.add("PDAM Tirtamarta");
        categories.add("PDAM Tirta Satria");
//        categories.add("PDAM Waduk Sermo");
//        categories.add("PDAM Kesugihan");
//        categories.add("PDAM Tirta Wijaya");
//        categories.add("PDAM Baturaden");

        // Creating adapter for spinner
        ArrayAdapter <String> dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Tarif Air PDAM");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Back ke parent activity, diatur di android manifest
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


}
