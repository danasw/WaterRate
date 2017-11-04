package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import project.waterate.R;
import utils.SessionManager;

public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    private TextView txtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        txtId = (TextView)findViewById(R.id.id_pengguna);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtId.setText(sessionManager.getEmailUser());

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        // display the first navigation drawer view on app launch
        displayView(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //refresh
        if(id == R.id.refresh_action){
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Intent update = new Intent(MainActivity.this, MainActivity.class);
        startActivity(update);
        finish();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        Intent intent;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new WaterateFragment();
                title = getString(R.string.title_waterate);
                break;
            case 1:
                intent = new Intent(MainActivity.this, TarifActivity.class);
                startActivity(intent);
                title = getString(R.string.title_tarif);
                break;
            case 2:
                intent = new Intent(MainActivity.this, TentangKamiActivity.class);
                startActivity(intent);
                title = getString(R.string.title_about);
                break;
            case 3:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("KONFIRMASI");

                // Setting Dialog Message
                alertDialog.setMessage("Anda yakin akan Log Out?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.seru2);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        mAuth.signOut();
                        Intent back = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(back);
                        finish();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Showing Alert Message
                alertDialog.show();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    //Notif
//    public void sendNotification() {
//
//        //Get an instance of NotificationManager//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        mBuilder.setContentIntent(pendingIntent);
//
//        mBuilder.setSmallIcon(R.drawable.seru);
//        mBuilder.setContentTitle("WateRATE Reminder");
//        mBuilder.setContentText("Anda sudah menggunakan 75% konsumsi maksimal!");
//
//        // Gets an instance of the NotificationManager service//
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //When you issue multiple notifications about the same type of event, it’s best practice for your app to try to update an existing notification with this new information, rather than immediately creating a new notification. If you want to update this notification at a later date, you need to assign it an ID. You can then use this ID whenever you issue a subsequent notification. If the previous notification is still visible, the system will update this existing notification, rather than create a new one. In this example, the notification’s ID is 001//
//        mNotificationManager.notify(001, mBuilder.build());
//    }
}