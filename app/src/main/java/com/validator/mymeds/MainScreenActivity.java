package com.validator.mymeds;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.validator.mymeds.model.Medication;
import com.validator.mymeds.viewholder.MedViewHolder;

import java.util.Calendar;

public class MainScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private FirestoreRecyclerAdapter adapter;
    private Button addMedButton;
    private Toolbar my_toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu otherMenu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, otherMenu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(MainScreenActivity.this, AddAllergy.class);
        startActivity(intent);
        return true;
    }
    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        /*
         * The following lines set up the Medication view. Methods are explained below.
         */
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
        my_toolbar = findViewById(R.id.my_toolbar);
        my_toolbar.setBackgroundColor(Color.parseColor("#008577"));
        my_toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(my_toolbar);

        if(database.collection("profiles").document(user.getEmail()).collection("medications") != null){
            recyclerView = findViewById(R.id.medicationList);
            adapter = setUpAdapter(database,user);
            setUpRecyclerView(recyclerView,adapter);
        }

        addMedButton = findViewById(R.id.addMedButton);
        mNotificationHelper = new NotificationHelper(this);
        addMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this, AddMedView.class);
                startActivity(intent);
            }
        });
        MatchSystemandDosageTime(); //Sets an alarm for all user medications.
}

    /**
     * Method sets up a notification to send to user when their supply of a particular
     * medication is low.
     * @param med refers to specific medication which is low, as pulled from Firebase.
     */
    public void pushNotificationMedLow(Medication med) {
        int random = (int) (Math.random() * 1000);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification newNotification;
        NotificationChannel channel = new NotificationChannel("channel_id", "notification_channel", manager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        Notification.Builder builder = new Notification.Builder(MainScreenActivity.this,"channel_id");
        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(),0);
        builder.setAutoCancel(true);
        builder.setContentTitle("Medication Low!");
        builder.setContentText("You have only " + med.getRemainingDoses() + " doses of "
                + med.getName() + " remaining.");
        builder.setSmallIcon(R.drawable.ic_stat_onesignal_default);
        builder.setContentIntent(pending);
        builder.setOngoing(true);
        builder.setNumber(1);

        newNotification = builder.build();
        manager.notify(random, newNotification);
    }

    /**
     * The following method sets up the medication view by inputting information into a RecyclerView.
     * @param rv refers to the RecyclerView in the MainScreenActivity.
     * @param adapter refers to the customized adapter.
     */
    private void setUpRecyclerView(RecyclerView rv, FirestoreRecyclerAdapter adapter)
    {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }

    /**
     * This method sets up an adapter to update our recycler view.
     * @author Juliano Franz. Much of the following code was adapted from Assignment 3.
     * @param database refers to the Firebase from which information is being extracted.
     * @param user refers to the logged in user.
     * @return FirestoreRecyclerAdapter which can be passed into a RecyclerView.
     */
    private FirestoreRecyclerAdapter setUpAdapter(final FirebaseFirestore database, final FirebaseUser user) {
        Query query = database.collection("profiles").document(user.getEmail()).collection("medications");
        FirestoreRecyclerOptions<Medication> options = new FirestoreRecyclerOptions.Builder<Medication>()
                .setQuery(query,Medication.class).build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Medication, MedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MedViewHolder holder, int position, final Medication model) {
                holder.name.setText("Name: " + model.getName());
                holder.dosage.setText("Dosage: " + model.getDosage());
                holder.dosageTime.setText("Dosage Time: " + model.getDosageTime());
                holder.notesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainScreenActivity.this, NotesActivity.class);
                        intent.putExtra("Medication", model);
                        startActivity(intent);
                    }
                });
                holder.dosageAmountButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainScreenActivity.this, UpdateDosAmount.class);
                        intent.putExtra("Medication", model);
                        startActivity(intent);
                    }
                });
                holder.medTaken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.updateRemainingDosage();
                        if(model.medLow()) {
                            pushNotificationMedLow(model);
                        }
                        Toast.makeText(MainScreenActivity.this, model.getName() + " taken",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public MedViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.medviewformat,group,false);
                return new MedViewHolder(view);
            }
        };
        return adapter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Tells the adapter to start listening for changes in the database. */
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /* Tells adapter to stop listening when activity is not in use. */
        adapter.stopListening();
    }

    /**
     * Method loops through all medications a user has entered, and extracts the medication
     *  name and dosage time. Fields are passed into method described below.
     */
    public void MatchSystemandDosageTime()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
        final Query query = database.collection("profiles").document(user.getEmail()).collection("medications");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                {
                    Medication med = document.toObject(Medication.class);
                    String name = med.getName();
                    String dt = med.getDosageTime();
                    Getmednameandtriggeralarm(name,dt);
                }
            }

        });
    }

    /**
     * Method utilizes AlarmManager to set a system alarm for specified medication.
     * @param medicine refers to the name of a medication. The String is pulled from
     *                 'name' field in Firebase.
     * @param dosagetime refers to time when medication is to be administered. String is pulled
     *                   from 'dosageTime' field in Firebase.
     */
    public void Getmednameandtriggeralarm(String medicine,String dosagetime)
    {
        String [] parts = dosagetime.split(":");
        String parthr = parts[0];
        String partmin = parts[1];
        int hr = Integer.parseInt(parthr);
        int min = Integer.parseInt(partmin);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainScreenActivity.this, AlarmReceiver.class);
        alarmIntent.putExtra("Medicinename", medicine);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainScreenActivity.this, 1, alarmIntent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}

