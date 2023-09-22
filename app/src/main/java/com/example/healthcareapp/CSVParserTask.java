package com.example.healthcareapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.healthcareapp.Model.DiseaseTreatment;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVParserTask extends AsyncTask<Void, Void, List<DiseaseTreatment>> {

    private Context context;
    private OnCSVDataParsedListener listener;

    public CSVParserTask(Context context, OnCSVDataParsedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<DiseaseTreatment> doInBackground(Void... voids) {
        List<DiseaseTreatment> data = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("diseasetreatments.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            // Skip the header row if exists
            String[] nextLine;
            reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                String diseaseName = nextLine[0];
                String treatment = nextLine[1];
                String extraComments = nextLine[3];
                String description = nextLine[2];

                DiseaseTreatment diseaseTreatment = new DiseaseTreatment();
                diseaseTreatment.setDiseaseName(diseaseName);
                diseaseTreatment.setTreatment(treatment);
                diseaseTreatment.setExtraComments(extraComments);
                diseaseTreatment.setDescription(description);

                data.add(diseaseTreatment);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(List<DiseaseTreatment> data) {
        if (listener != null) {
            listener.onCSVDataParsed(data);
        }
    }

    public interface OnCSVDataParsedListener {
        void onCSVDataParsed(List<DiseaseTreatment> data);
    }
}
