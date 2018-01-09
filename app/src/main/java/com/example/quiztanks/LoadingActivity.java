package com.example.quiztanks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoadingActivity extends AppCompatActivity {

    public static final String SELECTED_TYPE = "selected_type";

    String[] fileNamesLightTanks;
    String[] fileNamesMediumTanks;
    String[] fileNameHeavyTanks;
    String[] fileNamePTSAU;
    String[] fileNameSAU;
    int checkTest = 0;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    Intent intent;
    TextView textView;
    boolean[] selectedType = new boolean[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent = new Intent(LoadingActivity.this, MainActivity.class);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        fileNamesLightTanks = getArrayFileNamesOfType("LightTanks", getResources().getStringArray(R.array.light_tanks).length);
        fileNamesMediumTanks = getArrayFileNamesOfType("MediumTanks", getResources().getStringArray(R.array.medium_tanks).length);
        fileNameHeavyTanks = getArrayFileNamesOfType("HeavyTanks", getResources().getStringArray(R.array.heavy_tanks).length);
        fileNamePTSAU = getArrayFileNamesOfType("PTSAU", getResources().getStringArray(R.array.pt_sau).length);
        fileNameSAU = getArrayFileNamesOfType("SAU", getResources().getStringArray(R.array.sau).length);
    }

    public String[] getArrayFileNamesOfType (final String type, int resourcesStringArrayLength) {
        final String[] fileNames = new String[resourcesStringArrayLength];
        for (int i = 0; i < resourcesStringArrayLength; i++) {
            final int finalI = i;
            databaseReference.child(type).child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fileNames[finalI] = dataSnapshot.getValue(String.class);
                    checkTest++;
                    progressBar.setProgress(checkTest);
                    textView.setText(getString(R.string.loading_text));
                    if (checkTest == 100) {
                        intent.putExtra("light_tanks_array", fileNamesLightTanks);
                        intent.putExtra("medium_tanks_array", fileNamesMediumTanks);
                        intent.putExtra("heavy_tanks_array", fileNameHeavyTanks);
                        intent.putExtra("pt_sau_array", fileNamePTSAU);
                        intent.putExtra("sau_array", fileNameSAU);
                        for (int i = 0; i < 5; i++){
                            selectedType[i] = true;
                        }
                        intent.putExtra(SELECTED_TYPE, selectedType);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        return fileNames;
    }
}
