package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyleduo.switchbutton.SwitchButton;

import project.waterate.R;
import utils.SessionManager;

public class KranActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwitchButton kran1;
    private FirebaseDatabase mDb;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_kran);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        kran1 = (SwitchButton)findViewById(R.id.switch1);
        mDb = FirebaseDatabase.getInstance();
        sessionManager = new SessionManager(this);

        mDb.getReference("/profile/"+sessionManager.getUserID()+"/statuskran1/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kran1.setChecked((boolean) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        kran1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDb.getReference("/profile/"+sessionManager.getUserID()+"/").child("statuskran1").setValue(b);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Kran Air");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Back ke parent activity, diatur di android manifest
    }
}

