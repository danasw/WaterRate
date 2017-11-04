package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import project.waterate.R;
import service.NotificationIntentService;
import utils.SessionManager;


public class WaterateFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    private ImageView emergambar;
    private TextView emer1, emer2;
    CircleProgressView mCircleView;
    private LineChartView lineChart;
    ArrayList pointValues = new ArrayList();
    int maxNumberOfPoints = 0, jumlahOrang, konsumInt, jumlahOrangEmer;
    float paramNotif;
    private FirebaseDatabase mDb;
    private TextView txtWater1, txtWater2, txtPh, txtSuhu, txtKonduk;
    private SessionManager sessionManager;
    private ImageView kranButton, airButton;
    android.content.res.Resources res;
    private boolean tambah;
    private int draw = 0;
    private DateTime now;

    public WaterateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waterate, container, false);
        Intent intent = new Intent(getContext(), NotificationIntentService.class);
        getContext().startService(intent);
        res = getResources();

        mDb = FirebaseDatabase.getInstance();
        sessionManager = new SessionManager(getContext());
        jumlahOrang = sessionManager.getSyarat();

        //DEKLARASI BUTTON
        kranButton = (ImageView) rootView.findViewById(R.id.button_kran);
        airButton = (ImageView) rootView.findViewById(R.id.button_air);
        txtWater1 = (TextView) rootView.findViewById(R.id.konsumsi_sekarang);
        txtWater2 = (TextView) rootView.findViewById(R.id.konsumsi_maksimal);
        txtPh = (TextView) rootView.findViewById(R.id.output_ph);
        txtSuhu = (TextView) rootView.findViewById(R.id.output_suhu);
        txtKonduk = (TextView) rootView.findViewById(R.id.output_konduktivitas);
        emergambar = (ImageView) rootView.findViewById(R.id.image_emergency);
        emer1 = (TextView)rootView.findViewById(R.id.text_emergency1);
        emer2 = (TextView)rootView.findViewById(R.id.text_emergency2);

        //KLIK BUTTON
        kranButton.setOnClickListener(this);
        airButton.setOnClickListener(this);
        now = new DateTime();
        String hariini = now.toString("d~M~yyyy");
        Log.d(TAG, hariini);
        //akses Db
        mDb.getReference().child("/data/"+sessionManager.getUserID()+"/"+hariini+"/").addValueEventListener(new ValueEventListener() {
            //ngakses child
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtWater1.setText(String.valueOf(dataSnapshot.child("debit").getValue()));
                txtPh.setText(String.valueOf(dataSnapshot.child("ph").getValue()));
                txtSuhu.setText(String.valueOf(dataSnapshot.child("suhu").getValue()));
                txtKonduk.setText(String.valueOf(dataSnapshot.child("konduktivitas").getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDb.getReference().child("/data/"+sessionManager.getUserID()+"/"+hariini+"/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                konsumInt = Integer.parseInt(dataSnapshot.child("debit").getValue().toString());
                mCircleView.setValueAnimated(konsumInt, 1500);
                paramNotif = (float)konsumInt/(jumlahOrang);
                if (paramNotif == 0.75){
                    NotificationIntentService.processStartNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDb.getReference().child("/profile/"+sessionManager.getUserID()+"/syarat/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sessionManager.setUserSyarat(Integer.parseInt(dataSnapshot.getValue().toString()));
                jumlahOrang = sessionManager.getSyarat();
                jumlahOrangEmer = jumlahOrang+1;
                if (tambah){
                    txtWater2.setText(String.valueOf(jumlahOrangEmer)); //set limit debit air
                    mCircleView.setMaxValue(jumlahOrangEmer); //value setting
                    mCircleView.setValueAnimated(konsumInt, 1500);
                } else {
                    txtWater2.setText(String.valueOf(jumlahOrang)); //set limit debit air
                    mCircleView.setMaxValue(jumlahOrang); //value setting
                    mCircleView.setValueAnimated(konsumInt, 1500);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDb.getReference("/profile/"+sessionManager.getUserID()+"/tambahair/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tambah = (boolean) dataSnapshot.getValue();
//                int tambah = Integer.parseInt(dataSnapshot.getValue().toString());
//                tambah == 1
                if (tambah) {
//                    emergambar.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorCerah));
//                    emer1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCerah));
//                    emer2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCerah));
                    emergambar.setColorFilter(res.getColor(R.color.colorCerah));
//                    emergambar.setColorFilter(getContext().getResources().getColor(R.color.colorCerah));
                    emer1.setTextColor(res.getColor(R.color.colorCerah));
                    emer2.setTextColor(res.getColor(R.color.colorCerah));
//                    airButton.setColorFilter(getContext().getResources().getColor(R.color.Hitam));
                    txtWater2.setText(String.valueOf(jumlahOrangEmer)); //set limit debit air
                    mCircleView.setMaxValue(jumlahOrangEmer); //value setting
                    airButton.setImageAlpha(50);
                    airButton.setEnabled(false);
                } else {
//                    emergambar.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorMuda));
//                    emer1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMuda));
//                    emer2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMuda));
                    emergambar.setColorFilter(res.getColor(R.color.colorMuda));
                    emer1.setTextColor(res.getColor(R.color.colorMuda));
                    emer2.setTextColor(res.getColor(R.color.colorMuda));
//                    airButton.setBackgroundResource(R.drawable.pinggirbunder);
//                    airButton.setImageResource(R.drawable.air);
                    txtWater2.setText(String.valueOf(jumlahOrang)); //set limit debit air
                    mCircleView.setMaxValue(jumlahOrang); //value setting
                    airButton.setImageAlpha(255);
                    airButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //CHART BUNDER MULAI DISINI
        mCircleView = (CircleProgressView)rootView.findViewById(R.id.circleView);
//        mCircleView.setValueAnimated(0, 1500);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                Log.d(TAG, "Progress Changed: " + value);
            }
        });

        mCircleView.setOnTouchListener(new View.OnTouchListener() { //disable progress bar in touch
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mCircleView.setBarColor(getResources().getColor(R.color.colorBunder1), getResources()
                .getColor(R.color.colorBunder2), getResources().getColor(R.color.colorBunder3),
                getResources().getColor(R.color.colorBunder4)); //WARNA BUNDERAN

        //LINE CHART MULAI DISINI
        lineChart =(LineChartView)rootView.findViewById(R.id.chart_riwayat);

        //gabisa di zoom/geser/scroll
        lineChart.setInteractive(false);
        lineChart.setZoomEnabled(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setZoomLevel(0,0,4);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        List axisXValues = new ArrayList<AxisValue>();

        for(int i=0;i<7;i++) {
            String date = now.plusDays(i-7).toString("dd/MM/yyyy");
            axisXValues.add(new AxisValue(i).setLabel(date));
        }

        Axis axisX = new Axis(axisXValues);
        axisX.setHasLines(true);
        axisX.setHasSeparationLine(false);
        axisX.setAutoGenerated(false);
        axisX.setHasTiltedLabels(true);
        axisX.setTypeface(Typeface.DEFAULT_BOLD);
        axisX.setMaxLabelChars(10);

        List<AxisValue> axisYValues = new ArrayList<AxisValue>();

        Axis axisY = new Axis(axisYValues).setHasLines(true);

        Line line = new Line().setColor(Color.argb(255, 138, 173, 186)).setCubic(false).setHasPoints(true)
                .setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.getAxisXBottom().setTextSize(8);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        lineChart.setLineChartData(data);

        drawGraph();

        return rootView;
    }

    //method drawgraph
    private void drawGraph() {
        mDb.getReference("/data/"+sessionManager.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (draw < 7) {
                        Log.d(TAG, s
                                .child(now.plusDays(draw-7).toString("d~M~yyyy"))
                                .child("debit")
                                .getValue().toString());
                        float yValue = Float.parseFloat(String.valueOf(dataSnapshot
                                .child(now.plusDays(draw-7).toString("d~M~yyyy"))
                                .child("debit")
                                .getValue()));
                        LineChartData data = lineChart.getLineChartData();
                        pointValues.add(new PointValue(draw, yValue));
                        data.getLines().get(0).setValues(new ArrayList<PointValue>(pointValues));
                        lineChart.setLineChartData(data);
                        setViewport();
                    }
                    draw++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setViewport() {
        int size = pointValues.size();
        if (size > maxNumberOfPoints) {
            final Viewport viewport = new Viewport(lineChart.getMaximumViewport());
            viewport.left = size - 50;
            lineChart.setCurrentViewport(viewport);
        }
    }


    @Override
    public void onClick(final View view) {

        Fragment fragment = null;
        switch (view.getId()) {

            //BUTTON KRAN DIPENCET PINDAH ACTIVITY
            case R.id.button_kran:
                Intent intent = new Intent(getContext(), KranActivity.class);
                startActivity(intent);
                break;

            //BUTTON AIR DIPENCET METU ALERT USAGE
            case R.id.button_air:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                // Setting Dialog Title
                alertDialog.setTitle("KONFIRMASI");

                // Setting Dialog Message
                alertDialog.setMessage("Anda yakin akan menggunakan cadangan air?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.seru2);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

//                        emergambar.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorCerah), PorterDuff.Mode.MULTIPLY);
//                        emer1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCerah));
//                        emer2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCerah));
                        emergambar.setColorFilter(getContext().getResources().getColor(R.color.colorCerah));
                        emer1.setTextColor(getContext().getResources().getColor(R.color.colorCerah));
                        emer2.setTextColor(getContext().getResources().getColor(R.color.colorCerah));
                        Toast.makeText(getContext(), "Anda sukses menggunakan cadangan air", Toast.LENGTH_SHORT).show();
                        mDb.getReference("/profile/"+sessionManager.getUserID()+"/").child("tambahair").setValue(true);
                        airButton.setEnabled(false);
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
//                        Toast.makeText(getContext(), "Anda", Toast.LENGTH_SHORT).show();
                        mDb.getReference("/profile/"+sessionManager.getUserID()+"/").child("tambahair").setValue(false);
                    }
                });

                // Showing Alert Message
                alertDialog.show();

        }

    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container_body, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}