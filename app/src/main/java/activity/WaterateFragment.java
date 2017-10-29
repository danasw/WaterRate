package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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


public class WaterateFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    private ImageView emergambar;
    private TextView emer1, emer2;
    CircleProgressView mCircleView;
    private LineChartView lineChart;
    ArrayList pointValues = new ArrayList();
    private ArrayList<String> days = new ArrayList<>();
    int maxNumberOfPoints = 0;
    private FirebaseDatabase mDb;
    private TextView txtWater1, txtWater2, txtPh, txtSuhu, txtKonduk;
    private RectF drawRect = new RectF();
//    private ArrayList aL;

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
        mDb = FirebaseDatabase.getInstance();
        // ArrayList untuk hari dalam seminggu
        days.add("Senin");
        days.add("Selasa");
        days.add("Rabu");
        days.add("Kamis");
        days.add("Jumat");
        days.add("Sabtu");
        days.add("Minggu");
        //DEKLARASI BUTTON
        ImageView kranButton = (ImageView) rootView.findViewById(R.id.button_kran);
        ImageView airButton = (ImageView) rootView.findViewById(R.id.button_air);
        txtWater1 = (TextView) rootView.findViewById(R.id.konsumsi_sekarang);
        txtWater2 = (TextView) rootView.findViewById(R.id.konsumsi_maksimal);
        txtPh = (TextView) rootView.findViewById(R.id.output_suhu);
        txtSuhu = (TextView) rootView.findViewById(R.id.output_ph);
        txtKonduk = (TextView) rootView.findViewById(R.id.output_konduktivitas);
        //KLIK BUTTON
        kranButton.setOnClickListener(this);
        airButton.setOnClickListener(this);

        //akses Db
        mDb.getReference().child("/data/eKIJ7Kxw4oXUFiYhhObrAejQsC53/28~10~2017/").addValueEventListener(new ValueEventListener() {
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

        //DEKLARASI ALERT USAGE
        emergambar = (ImageView) rootView.findViewById(R.id.image_emergency);
        emer1 = (TextView)rootView.findViewById(R.id.text_emergency1);
        emer2 = (TextView)rootView.findViewById(R.id.text_emergency2);

        //CHART BUNDER MULAI DISINI
        mCircleView = (CircleProgressView)rootView.findViewById(R.id.circleView);
        mCircleView.setValueAnimated(50, 1500);      // 50 adalah valuenya
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                Log.d(TAG, "Progress Changed: " + value);
            }
        });

        mCircleView.setMaxValue(250); //value setting
//        mCircleView.setValue(0);
//        mCircleView.setValueAnimated(24);

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
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

//        ChartData.setAxisXBottom(Axis axisX);
//        ColumnChartData.setStacked(boolean isStacked);
//        Line.setStrokeWidth(int strokeWidthDp);

//        List<PointValue> values = new ArrayList<PointValue>();
//            values.add(new PointValue(0, 100));
//            values.add(new PointValue(1, 21));
//            values.add(new PointValue(2, 32));
//            values.add(new PointValue(3, 43));

        List axisXValues = new ArrayList<AxisValue>();
//            axisXValues.add(new AxisValue(0, "2011".toCharArray()));
//            axisXValues.add(new AxisValue(1, "2012".toCharArray()));
//            axisXValues.add(new AxisValue(2, "2013".toCharArray()));
//            axisXValues.add(new AxisValue(3, "2014".toCharArray()));
        DateTime now = new DateTime();
        for(int i=0;i<7;i++) {
            String date = now.plusDays(i-1).toString("dd/MM/yyyy");
            axisXValues.add(new AxisValue(i).setLabel(date));
        }

        Axis axisX = new Axis(axisXValues);
        axisX.setHasLines(true);
        axisX.setHasSeparationLine(false);
        axisX.setAutoGenerated(false);
        axisX.setHasTiltedLabels(true);
        axisX.setMaxLabelChars(4);

//        Axis axisX = new Axis(axisXValues).setName("Axis X");

        List<AxisValue> axisYValues = new ArrayList<AxisValue>();
//            axisYValues.add(new AxisValue(0).setLabel("0").setValue(0));
//            axisYValues.add(new AxisValue(1).setLabel("100").setValue(100));
//            axisYValues.add(new AxisValue(2).setLabel("200").setValue(200));
//            axisYValues.add(new AxisValue(3).setLabel("300").setValue(300));
        Axis axisY = new Axis(axisYValues).setHasLines(true);

        Line line = new Line().setColor(Color.DKGRAY).setCubic(false).setHasPoints(true)
                .setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

//        //keluar nilai pas dipencet
//        SelectedValue sv = new SelectedValue(0, 1, SelectedValue.SelectedValueType.NONE);
//        lineChart.selectValue(sv);
//
//        //deselect value by setting empty selected value, for example inside handler.postDelay() method
//        sv.clear();
//        lineChart.selectValue(sv);

        //set chart data to initialize viewport, otherwise it will be[0,0;0,0]
//        final Viewport v = new Viewport(lineChart.getMaximumViewport());
//        v.top =300; //example max value
//        v.bottom = 0;  //example min value
//        lineChart.setMaximumViewport(v);
//        lineChart.setCurrentViewport(v);

//        lineChart.setViewportCalculationEnabled(false);

        lineChart.setLineChartData(data);

        drawGraph();

        return rootView;
    }

    //method drawgraph
    private void drawGraph() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 7; ++i) {
                    float yValue = (float) (Math.random() * 150);
                    LineChartData data = lineChart.getLineChartData();
                    pointValues.add(new PointValue(i, yValue));
                    data.getLines().get(0).setValues(new ArrayList<>(pointValues));
                    lineChart.setLineChartData(data);
                    setViewport();
                    try {
                        Thread.sleep(35);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
    public void onClick(View view) {

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

                        emergambar.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorCerah), PorterDuff.Mode.MULTIPLY);
                        emer1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCerah));
                        emer2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCerah));
                        Toast.makeText(getContext(), "Anda sukses menggunakan cadangan air", Toast.LENGTH_SHORT).show();
//                        break;

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
//                        Toast.makeText(getContext(), "Anda", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

//             }
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