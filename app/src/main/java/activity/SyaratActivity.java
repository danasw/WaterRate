package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

import project.waterate.R;
import utils.SessionManager;

public class SyaratActivity extends AppCompatActivity {

    private Button buttonSyarat;
    private int min, max;
    private String emptytext;
    private EditText edtSyarat;
    private FirebaseDatabase mDb;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syarat);
        session = new SessionManager(this);
        if (session.isSyaratExist()) {
            startActivity(new Intent(SyaratActivity.this, MainActivity.class));
            finish();
        }
        mDb = FirebaseDatabase.getInstance();
        final EditText inputkeluarga = (EditText)findViewById(R.id.input_keluarga);
        buttonSyarat = (Button)findViewById(R.id.button_inputkeluarga);
        buttonSyarat.setEnabled(false);

        buttonSyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int syarat = Integer.parseInt(inputkeluarga.getText().toString());
                mDb.getReference().child("/profile/"+session.getUserID()+"/syarat/")
                        .setValue(syarat);
                session.setUserSyarat(syarat);
                startActivity(new Intent(SyaratActivity.this, MainActivity.class));
                finish();

            }
        });

        //gabisa input 0 di awal, nilai yg diterima 1-100
        inputkeluarga.setFilters(new InputFilter[]{new InputFilterMinMax("1","100")});

        //kosong > button disable
        inputkeluarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    buttonSyarat.setEnabled(false);
                } else {
                    buttonSyarat.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
// emptytext = inputkeluarga.getText().toString();
// if (emptytext.matches("")){
//
// Toast.makeText(this,"Mohon isi data dengan benar!", Toast.LENGTH_SHORT).show();
// return;
//
// }

    }

}