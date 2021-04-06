package com.validator.mymeds.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.validator.mymeds.R;

/**
 * Customized ViewHolder for medications.
 */

public class MedViewHolder extends RecyclerView.ViewHolder  {

    public TextView name;
    public TextView dosage;
    public TextView dosageTime;
    public Button notesButton;
    public Button dosageAmountButton;
    public Button medTaken;


    public MedViewHolder(View view)
    {
        super(view);
        name = view.findViewById(R.id.medviewname);
        dosage = view.findViewById(R.id.medviewdosage);
        dosageTime = view.findViewById(R.id.medviewdosagetime);
        notesButton = view.findViewById(R.id.notesButton);
        dosageAmountButton = view.findViewById(R.id.dosageAmountButton);
        medTaken = view.findViewById(R.id.medTaken);

    }
}
