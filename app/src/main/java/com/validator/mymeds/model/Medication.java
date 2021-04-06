package com.validator.mymeds.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.Serializable;

/**
 * Following class is used to store users' medication information.
 * Medication objects are stored in a user's sub-collection in Firebase.
 */
public class Medication implements Serializable {

    //Attributes
    public String id;
    private String name;
    private String remainingDoses;
    private String dosage;
    private String dosageTime;
    private String expirationDate;
    private String description;
    private String notes;

    //Constructors
    public Medication(){

    }

    public Medication(String name, String dosage, String description){
        this.name = name;
        this.dosage = dosage;
        this.description = description;
    }

    //Get Methods
    public String getName() {
        return name;
    }
    public String getRemainingDoses(){
        return remainingDoses;
    }
    public String getDosage(){
        return dosage;
    }
    public String getExpirationDate(){
        return expirationDate;
    }
    public String getDosageTime(){
        return dosageTime;
    }
    public String getDescription(){
        return description;
    }
    public String getNotes(){return notes;}


    //Set Methods
    public void setRemainingDoses(String remainingDoses){
        this.remainingDoses = remainingDoses;
    }
    public void setExpirationDate(String expirationDate){
        this.expirationDate = expirationDate;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    public void setName(String n) {
        name = n;
    }


    /**
     * Method sets the dosage time for a particular medication.
     * @param time refers to time of day, as entered with the TimePickerDialog in the AddMedView.
     */
    public void setDosageTime(String time){
        String [] token = time.split(":");
        if(token[0].length() == 1){
            time = "0" + time;
        }
        if(token[1].length() == 1){
            time += "0";
        }
        dosageTime = time;
    }

    /**
     * Because all Medication information within Firebase is stored as strings,
     * this method is used to ensure that computations are performed only on Integers.
     * @param str refers to any string being tested.
     * @return true if a String can be converted to an Integer, false if not.
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method uses isNumeric() function to determine whether a string can be converted to an int.
     * It also checks if the converted int is greater than 0 (because cannot have negative doses).
     * @return the numeric value of the String decremented by 1, as a string; or null.
     */
    public String decrementDose() {
        if (isNumeric(remainingDoses) && (Integer.parseInt(remainingDoses) > 0)) {
            return (Integer.parseInt(remainingDoses) - Integer.parseInt(dosage)) + "";
        }
        else return null;
    }

    public boolean medLow() {
        if (!isNumeric(dosage)) {
            return false;
        }
        return (Integer.parseInt(remainingDoses) < (5* Integer.parseInt(dosage)));
    }

    /**
     * Uses above two methods to extract, alter, and update the "remaining doses" field in Firebase.
     */
    public void updateRemainingDosage() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final DocumentReference ref = database.collection("profiles").document(user.getEmail())
                .collection("medications").document(name);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Medication med = documentSnapshot.toObject(Medication.class);
                String newDose = decrementDose();
                if (newDose != null) {
                    ref.update("remainingDoses", ("" + newDose));
                }
            }
        });
    }
}


