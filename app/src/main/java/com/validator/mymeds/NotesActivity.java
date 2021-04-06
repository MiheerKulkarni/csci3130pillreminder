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
 * Class sets up activity which allows users to associate notes with specified medications.
 */
public class NotesActivity extends AppCompatActivity {
    private Intent intent;
    private Medication medication;
    private Button updateNotesButton;
    private TextView notesEdit, nameEdit;
    private FirebaseFirestore database;
    private FirebaseAuth authUser;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes2);

        intent = getIntent();
        medication = (Medication)intent.getSerializableExtra("Medication");
        nameEdit = findViewById(R.id.nameEdit);
        notesEdit = findViewById(R.id.medNameEdit);
        updateNotesButton = findViewById(R.id.updateDoseAmount);

        nameEdit.setText(medication.getName());
        notesEdit.setText(medication.getNotes());

        authUser = FirebaseAuth.getInstance();
        user = authUser.getCurrentUser();
        database = FirebaseFirestore.getInstance();

        updateNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = database.collection("profiles").document(user.getEmail()).collection("medications").document(medication.getName());
                ref.update("notes", notesEdit.getText().toString());
                fileList();
                finish();
            }
        });
    }
}
