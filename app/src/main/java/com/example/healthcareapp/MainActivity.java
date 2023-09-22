package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareapp.NHSData.NHSApi;
import com.example.healthcareapp.R.id;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ButtonAdapter.OnButtonClickListener {

    NHSApi nhsApi;
    public RecyclerView recyclerView;
    public ButtonAdapter adapter;
    public List<String> labels;
    public  String symptomspred="";
    private FloatingActionButton fabButton;
    private List<String> clickedButtonLabels;
    private ArrayAdapter<String> clickedListAdapter;
  public   String[] symptoms = {
            "Itching", "Skin Rash", "Nodal Skin Eruptions",
            "Continuous Sneezing", "Shivering", "Chills", "Joint Pain",
            "Stomach Pain", "Acidity", "Ulcers On Tongue", "Muscle Wasting",
            "Vomiting", "Burning Micturition", "Spotting Urination",
            "Fatigue", "Weight Gain", "Anxiety", "Cold Hands And Feets",
            "Mood Swings", "Weight Loss", "Restlessness", "Lethargy",
            "Patches In Throat", "Irregular Sugar Level", "Cough",
            "High Fever", "Sunken Eyes", "Breathlessness", "Sweating",
            "Dehydration", "Indigestion", "Headache", "Yellowish Skin",
            "Dark Urine", "Nausea", "Loss Of Appetite", "Pain Behind The Eyes",
            "Back Pain", "Constipation", "Abdominal Pain", "Diarrhoea",
            "Mild Fever", "Yellow Urine", "Yellowing Of Eyes",
            "Acute Liver Failure", "Fluid Overload", "Swelling Of Stomach",
            "Swelled Lymph Nodes", "Malaise", "Blurred And Distorted Vision",
            "Phlegm", "Throat Irritation", "Redness Of Eyes", "Sinus Pressure",
            "Runny Nose", "Congestion", "Chest Pain", "Weakness In Limbs",
            "Fast Heart Rate", "Pain During Bowel Movements",
            "Pain In Anal Region", "Bloody Stool", "Irritation In Anus",
            "Neck Pain", "Dizziness", "Cramps", "Bruising", "Obesity",
            "Swollen Legs", "Swollen Blood Vessels", "Puffy Face And Eyes",
            "Enlarged Thyroid", "Brittle Nails", "Swollen Extremities",
            "Excessive Hunger", "Extra Marital Contacts",
            "Drying And Tingling Lips", "Slurred Speech", "Knee Pain",
            "Hip Joint Pain", "Muscle Weakness", "Stiff Neck",
            "Swelling Joints", "Movement Stiffness", "Spinning Movements",
            "Loss Of Balance", "Unsteadiness", "Weakness Of One Body Side",
            "Loss Of Smell", "Bladder Discomfort", "Foul Smell Of Urine",
            "Continuous Feel Of Urine", "Passage Of Gases", "Internal Itching",
            "Toxic Look (Typhos)", "Depression", "Irritability", "Muscle Pain",
            "Altered Sensorium", "Red Spots Over Body", "Belly Pain",
            "Abnormal Menstruation", "Dischromic Patches",
            "Watering From Eyes", "Increased Appetite", "Polyuria",
            "Family History", "Mucoid Sputum", "Rusty Sputum",
            "Lack Of Concentration", "Visual Disturbances",
            "Receiving Blood Transfusion", "Receiving Unsterile Injections",
            "Coma", "Stomach Bleeding", "Distention Of Abdomen",
            "History Of Alcohol Consumption", "Fluid Overload.1",
            "Blood In Sputum", "Prominent Veins On Calf", "Palpitations",
            "Painful Walking", "Pus Filled Pimples", "Blackheads", "Scurring",
            "Skin Peeling", "Silver Like Dusting", "Small Dents In Nails",
            "Inflammatory Nails", "Blister", "Red Sore Around Nose",
            "Yellow Crust Ooze"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        nhsApi=new NHSApi(MainActivity.this);
//        nhsApi.getdataforSpecificationcondition("djb");
        recyclerView = findViewById(R.id.recyclerViewButtons);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Change the column count as needed
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        List<String> buttonLabels = generateButtonLabels(); // You can populate this list with your button labels
        adapter = new ButtonAdapter(buttonLabels,this::onButtonClick);
        recyclerView.setAdapter(adapter);

        clickedButtonLabels = new ArrayList<>();

        clickedListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clickedButtonLabels);
        ListView listViewClickedButtons = findViewById(R.id.buttonlist);
        listViewClickedButtons.setAdapter(clickedListAdapter);
        listViewClickedButtons.setOnItemClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
        });
        fabButton = findViewById(R.id.fabButton);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle FAB click event here

            }
        });
        final Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.fab);

        // Set animation listener to reset FAB after animation ends
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                fabButton.setScaleX(1.0f);
                fabButton.setScaleY(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Set click listener to play the animation when the FAB is clicked
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);
             //   Toast.makeText(MainActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
                for(int i=0;i<clickedButtonLabels.size();i++)
                {
                    symptomspred=symptomspred+clickedButtonLabels.get(i);

                        symptomspred=symptomspred+",";

                }
                if (symptomspred != null && symptomspred.length() > 0 && symptomspred.charAt(symptomspred.length() - 1) == ',') {
                    symptomspred = symptomspred.substring(0, symptomspred.length() - 1);
                }
               predict(symptomspred);
                symptomspred="";
             //   Toast.makeText(MainActivity.this, symptomspred, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Remove the item from the list and update the adapter
            clickedButtonLabels.remove(position);
            clickedListAdapter.notifyDataSetChanged();
   //         Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing if "No" is clicked
        });
        builder.show();
    }

    private List<String> generateButtonLabels() {
 labels = new ArrayList<>();
        // Populate this list with your button labels
        // For example:
      for(int i=0;i<symptoms.length;i++)
      {
          labels.add(symptoms[i]);
      }
        // ... and so on
        return labels;
    }

    public void predict(String sym)
    {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, " https://5f6f-34-83-12-234.ngrok-free.app", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // Now you can access the data from the JSON object
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String pred = jsonObject.getString("final_prediction");
                    Toast.makeText(MainActivity.this, pred, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,PredictedActivity.class);
                      intent.putExtra("disease",pred);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                   Toast.makeText(MainActivity.this, "Fail to get response = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ratik", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("symtoms", sym);

                return params;
            }
        };

        queue.add(request);

    }

    public void onButtonClick(String label) {
        if (!clickedButtonLabels.contains(label)) {
            clickedButtonLabels.add(label);
            clickedListAdapter.notifyDataSetChanged();
        //    Toast.makeText(this, "Button '" + label + "' clicked.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Symtom Already added", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
            // Show all button labels if the search query is empty
            adapter.filter(newText);

        return false;
    }
}