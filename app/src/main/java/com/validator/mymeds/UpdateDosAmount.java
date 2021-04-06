package com.validator.mymeds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.validator.mymeds.model.Medication;

/**
 * Class allows users update their remaining doses manually, and save the changes in Firebase.
 * Lets users refill their remaining dose if they have refilled prescription.
 * Lets users edit their remaining dosage if they accidentally press the 'Take Med' button.
 */
public class UpdateDosAmount extends AppCompatActivity {
    private Intent intent;
    private Medication medication;
    private TextView nameEdit;
    private TextView amountEdit;
    private Button updateDoseAmount;
    private FirebaseFirestore database;
    private FirebaseAuth authUser;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dos_amount);

        intent = getIntent();
        medication = (Medication)intent.getSerializableExtra("Medication");
        updateDoseAmount = findViewById(R.id.updateDoseAmount);
        amountEdit = findViewById(R.id.amountEdit);
        nameEdit = findViewById(R.id.nameEdit);

        nameEdit.setText(medication.getName());
        amountEdit.setText(medication.getRemainingDoses());

        authUser = FirebaseAuth.getInstance();
        user = authUser.getCurrentUser();
        database = FirebaseFirestore.getInstance();

        updateDoseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = database.collection("profiles")
                        .document(user.getEmail()).collection("medications").document(medication.getName());
                ref.update("remainingDoses", amountEdit.getText().toString());
                fileList();
                finish();
            }
        });
    }
}
