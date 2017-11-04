package service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import activity.MainActivity;
import project.waterate.R;
import utils.SessionManager;

/**
 * Created by hp on 11/2/2017.
 */

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    private FirebaseDatabase mDb;
    private SessionManager sessionManager;
    private int jumlahOrang;
    private static Context context;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        mDb = FirebaseDatabase.getInstance();
        sessionManager = new SessionManager(this);
        context = this;
        jumlahOrang = sessionManager.getSyarat();
        DateTime now = new DateTime();
        String hariini = now.toString("d~M~yyyy");
        mDb.getReference().child("/data/" + sessionManager.getUserID() + "/"+hariini+"/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int konsumInt = Integer.parseInt(dataSnapshot.child("debit").getValue().toString());
                float paramNotif = (float) konsumInt / (jumlahOrang * 1000);
                if (paramNotif == 0.75) {
                    processStartNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Scheduled Notification")
                .setAutoCancel(true)
                .setContentTitle("WateRATE Reminder")
                .setContentText("Anda sudah menggunakan 75% konsumsi maksimal!")
                .setSmallIcon(R.drawable.seru);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
