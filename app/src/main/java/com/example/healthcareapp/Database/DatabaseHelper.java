package com.example.healthcareapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.healthcareapp.Model.DiseaseTreatment;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "disease_treatment_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "diseasetreatment";
    private static final String COLUMN_DISEASE_NAME = "diseasename";
    private static final String COLUMN_TREATMENT = "treatment";
    private static final String COLUMN_EXTRA_COMMENTS = "extracomments";
    private static final String COLUMN_DESCRIPTION = "description";

    public String message = "";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_DISEASE_NAME + " TEXT,"
                + COLUMN_TREATMENT + " TEXT,"
                + COLUMN_EXTRA_COMMENTS + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(List<DiseaseTreatment> data) {

        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            for (DiseaseTreatment item : data) {
                values.put(COLUMN_DISEASE_NAME, item.getDiseaseName());
                values.put(COLUMN_TREATMENT, item.getTreatment());
                values.put(COLUMN_EXTRA_COMMENTS, item.getExtraComments());
                values.put(COLUMN_DESCRIPTION, item.getDescription());
                db.insert(TABLE_NAME, null, values);
            }


    }
    @SuppressLint("Range")
    public DiseaseTreatment getdiseaseByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_DISEASE_NAME, COLUMN_TREATMENT, COLUMN_EXTRA_COMMENTS,COLUMN_DESCRIPTION},
                COLUMN_DISEASE_NAME + "=?", new String[]{name},
                null, null, null, null);

        DiseaseTreatment diseaseTreatment = null;
        if (cursor != null && cursor.moveToFirst()) {
            diseaseTreatment = new DiseaseTreatment();
            diseaseTreatment.setTreatment(cursor.getString(cursor.getColumnIndex(COLUMN_TREATMENT)));
            diseaseTreatment.setDiseaseName(cursor.getString(cursor.getColumnIndex(COLUMN_DISEASE_NAME)));
            diseaseTreatment.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            diseaseTreatment.setExtraComments(cursor.getString(cursor.getColumnIndex(COLUMN_EXTRA_COMMENTS)));
            cursor.close();
        }


        return diseaseTreatment;
    }

//    public  String[] getdata(String diseaseName)
//    {
//        String[] data= new String[3];
//
//        Cursor cursor = db.query(
//                TABLE_NAME,
//                null,
//                diseaseName, // Replace "column_name" with the actual column name for your identifier
//                new String[]{diseaseName},
//                null,
//                null,
//                null
//        );
//        if (cursor.moveToFirst()) {
//            // Replace "column_name_1", "column_name_2", etc., with actual column names you want to retrieve data for.
//            int columnIndex1 = cursor.getColumnIndex(COLUMN_DESCRIPTION);
//            int columnIndex2 = cursor.getColumnIndex(COLUMN_EXTRA_COMMENTS);
//
//            // Check if the column exists in the cursor before retrieving the data
//            if (columnIndex1 >= 0) {
//                data[0] = cursor.getString(columnIndex1);
//                // Use dataColumn1 as needed
//            } else {
//                // Handle the case where column_name_1 is not found in the cursor
//            }
//
//            if (columnIndex2 >= 0) {
//                data[1] = cursor.getString(columnIndex2);
//                // Use dataColumn2 as needed
//            } else {
//                // Handle the case where column_name_2 is not found in the cursor
//            }
//        }
//
//        cursor.close();
//        return  data;
//    }

}

