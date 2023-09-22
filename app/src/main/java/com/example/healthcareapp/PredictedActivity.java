package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.DiseaseTreatment;

import java.util.List;

public class PredictedActivity extends AppCompatActivity implements CSVParserTask.OnCSVDataParsedListener {

    TextView preddisease,diseasedesccription,prevemeasure,extracomments;
    String diseasename="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predicted);
        preddisease=findViewById(R.id.predictieddisease);
        diseasedesccription=findViewById(R.id.predictieddiseaseDescription1);
        prevemeasure=findViewById(R.id.preventivemeasures);
        extracomments=findViewById(R.id.extracomments);
        diseasename=getIntent().getStringExtra("disease");
        CSVParserTask csvParserTask = new CSVParserTask(PredictedActivity.this, (CSVParserTask.OnCSVDataParsedListener) this);
        csvParserTask.execute();


    }

    public void onCSVDataParsed(List<DiseaseTreatment> data) {
        // Data parsed from CSV, now insert into the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        // dbHelper.insertData(data);

//     String diseasedata[]=   dbHelper.getdata("Drug Reaction");
        DiseaseTreatment diseaseTreatment = new DiseaseTreatment();
        diseaseTreatment = dbHelper.getdiseaseByName(diseasename)
        ;
        preddisease.setText("Predicted Disease:" + diseaseTreatment.getDiseaseName());
        diseasedesccription.setText(diseaseTreatment.getDescription());

        String desc[] = diseaseTreatment.getTreatment().replaceAll("[^\\x20-\\x7e]", "").split(";");
        if(!diseaseTreatment.getTreatment().isEmpty()) {
            for (int i = 0; i < desc.length; i++) {
                int j = i + 1;

                prevemeasure.append(j + ". " + desc[i].substring(0, 1).toUpperCase() + desc[i].substring(1) + "\n");
                prevemeasure.append("\n");
            }
        }
        if (!diseaseTreatment.getExtraComments().isEmpty()) {
            String comm[] = diseaseTreatment.getExtraComments().replaceAll("[^\\x20-\\x7e]", "").split(";");
            int count = 1;

            for (int i = 0; i < comm.length; i++) {

//            if(comm[i].charAt(comm[i].length()-1)==':')
//            {
//                extracomments.append(comm[i].substring(0, 1).toUpperCase()+comm[i].substring(1)+"\n");
//            }
//            else
//            {
                extracomments.append(count + ". " + comm[i].substring(0, 1).toUpperCase() + comm[i].substring(1) + "\n");
                count++;
//            }

                extracomments.append("\n");
            }

            // prevemeasure.setText("Treatment:"+diseaseTreatment.getTreatment());

            //  Toast.makeText(this,diseaseTreatment.getTreatment().toString(),Toast.LENGTH_LONG).show();
        }
    }
}