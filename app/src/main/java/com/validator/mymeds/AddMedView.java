package com.validator.mymeds;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.validator.mymeds.model.Medication;
import com.validator.mymeds.model.Profile;

import android.widget.TimePicker;
import android.widget.Toast;
import java.time.LocalTime;

/**
 * Class allows all user medications to be displayed on a single page, with scrolling enabled.
 */
public class AddMedView extends AppCompatActivity {
    String missingFields = "Missing fields: ";
    TextView requiredFields;
    EditText medName, dosageAmount, timeDosage, expirationDate, doseRemain, description, notes;
    Button addMedButton, cancel;

    private Profile profile;
    private TimePicker timePicker;

    private FirebaseFirestore database;
    private FirebaseAuth authUser;
    private FirebaseUser user;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med_view);

        authUser = FirebaseAuth.getInstance();
        user = authUser.getCurrentUser();
        medName = findViewById(R.id.medName);
        dosageAmount = findViewById(R.id.dosageAmount);
        timeDosage = findViewById(R.id.timeDosage);
        expirationDate = findViewById(R.id.expirationDate);
        doseRemain = findViewById(R.id.doseRemain);
        description = findViewById(R.id.description);
        notes = findViewById(R.id.notes);
        addMedButton = findViewById(R.id.addMedButton);
        requiredFields = findViewById(R.id.requiredFields);
        cancel = findViewById(R.id.cancelButton);
        database = FirebaseFirestore.getInstance();

        /*
         * Creates a TimePickerDialog when a user is adding medication information.
         * This ensures ease of use, as well as proper formatting of time.
         */
        timeDosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddMedView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        timeDosage.setText(hourOfDay + ":" + minutes);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        /*
         * The following function extracts user entered fields on the page,
         * and uses them to create a medication object which is stored in Firebase as a
         * sub-collection of the current user.
         * It also prevents submission if certain required fields are missing.
         */
        addMedButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(medName.getText().toString().length() == 0){
                    missingFields += "Medication Name ";
                }
                if(dosageAmount.getText().toString().length() == 0){
                    missingFields += "Dosage Amount ";
                }
                if(description.getText().toString().length() == 0){
                    missingFields += "Description ";
                }
                if(!missingFields.equals("Missing fields: ")){
                    requiredFields.setText(missingFields);
                }
                else{
                    Medication medication = new Medication(medName.getText().toString(),
                            dosageAmount.getText().toString(),
                            description.getText().toString());
                    medication.setDosageTime(timeDosage.getText().toString());

                    medication.setExpirationDate(expirationDate.getText().toString());
                    medication.setNotes(notes.getText().toString());
                    medication.setRemainingDoses(doseRemain.getText().toString());

                    DocumentReference ref = database.collection("profiles").
                            document(user.getEmail()).collection("medications").
                            document(medName.getText().toString());
                    ref.set(medication);

                    Toast.makeText(AddMedView.this, "Medication Added", Toast.LENGTH_SHORT).show();
                    requiredFields.setText("Required Fields: Name, Dosage, Description");

                   fileList();
                   finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedView.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}
