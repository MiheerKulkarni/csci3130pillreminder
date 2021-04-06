package com.validator.mymeds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.validator.mymeds.model.Profile;

/**
 * Class defines activity which gives users the ability to associate allergies with their profile.
 */
public class AddAllergy extends AppCompatActivity {

    private FirebaseFirestore database;
    private FirebaseAuth authUser;
    private FirebaseUser user;
    private Button addAllergyButton;
    private TextView addNewAllergy;
    private Profile profile;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergy);

        authUser = FirebaseAuth.getInstance();
        user = authUser.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        addAllergyButton = findViewById(R.id.addAllergyButton);
        listView = findViewById(R.id.listView);
        addNewAllergy = findViewById(R.id.addNewAllergy);

        final DocumentReference ref = database.collection("profiles").
                document(user.getEmail());

        /* Sets up view showing all listed allergies */
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profile = documentSnapshot.toObject(Profile.class);

                ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.allergy_layout, R.id.textView3,
                        profile.allergies);

                listView.setAdapter(adapter);
            }
        });

        /* Method sets any newly entered allergies */
        addAllergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.allergies.add(addNewAllergy.getText().toString());
                ref.set(profile);
                addNewAllergy.setText("");
            }
        });
    }
}
