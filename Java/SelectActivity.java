package com.example.omkar.workout2;

//Importing required packages
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.omkar.workout2.Workout.Body;
import com.example.omkar.workout2.Workout.Workout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;

public class SelectActivity extends AppCompatActivity {

    //Spinners for selecting weight,height
    private Spinner wtSpinner;
    private Spinner htftSpinner;
    private Spinner htinSpinner;


    //Authentication and database reference
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    //Store selected weight and height
    private int wtSelected,htftSelected,htinSelected;

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //Initializing spinner items
        wtSpinner=(Spinner)findViewById(R.id.wtspinner);
        htftSpinner=(Spinner)findViewById(R.id.htftspinner);
        htinSpinner=(Spinner)findViewById(R.id.htinspinner);

        //Initializing database and authentication references
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();



        //Setting weight spinner with selectable weights
        String array[]={"30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80"};
        ArrayAdapter<CharSequence> wtadapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,array);
        wtadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wtSpinner.setAdapter(wtadapter);

        //Setting height's spinners with selectable heights
        String htftArray[]={"5","6","7"};
        ArrayAdapter<CharSequence> htftAdapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,htftArray);
        htftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        htftSpinner.setAdapter(htftAdapter);

        String htinArray[]={"0","1","2","3","4","5","6","7","8","9","10","11"};
        ArrayAdapter<CharSequence> htinAdapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,htinArray);
        htinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        htinSpinner.setAdapter(htinAdapter);


        //On select listners for each spinner
        wtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wtSelected=Integer.parseInt(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wtSelected=40;
                parent.setSelection(0);
            }
        });

        htftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                htftSelected=Integer.parseInt(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                htftSelected=5;
                parent.setSelection(0);
            }
        });

        htinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                htinSelected=Integer.parseInt(parent.getSelectedItem().toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                htinSelected=0;
                parent.setSelection(0);
            }
        });
    }

    //Function gets invoked when clicked workout
    public void workout(View view)
    {
        Intent intent = new Intent(SelectActivity.this, WorkoutActivity.class);
        startActivity(intent);
    }

    //Function to update weight and height
    public void updateWeightAndHeight(View view)
    {
            float ht=(float)(0.3048 * htftSelected + 0.0254 * htinSelected);
            int wt = wtSelected;
            Body body=new Body(wt,ht);

            String uid=mAuth.getCurrentUser().getUid();
            mDatabase.child(uid).child("Body").setValue(body).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SelectActivity.this,"Weight and height successfully updated",Toast.LENGTH_SHORT).show();
                }
            });


    }

    //Function get invoked when clicked getAverage button
    public void getAverage(View view)
    {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int sum[]=new int[3];
                
                //Getting total number of users
                int cnt=(int)dataSnapshot.getChildrenCount();
                
                //Looping for each user
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    
                    //Getting weight and height of current or authenticated user
                    Long wt1=(Long)dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Body").child("weight").getValue();
                    int wt=wt1.intValue();
                    Double ht1=(Double) dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Body").child("height").getValue();
                    float ht=ht1.floatValue();
                    
                    //Creating body object for looped user
                    Body body=new Body(wt,ht);

                    //Checking if looped user is authenticated or current user
                    String cur_uid=postSnapshot.getKey().trim();
                    String auth_uid=mAuth.getCurrentUser().getUid().trim();

                    if(!cur_uid.equals(auth_uid))
                    {
                        //Getting weight and height of looped user
                        wt1 = (Long) postSnapshot.child("Body").child("weight").getValue();
                        wt=wt1.intValue();
                        ht1 = (Double) postSnapshot.child("Body").child("height").getValue();
                        ht=ht1.floatValue();
                        
                        //Check to see if looped user is within range
                        if (body.isInside(wt,ht)) {
                            //If user in range add his/her workout values to sum array
                            sum[0]=+((Long)postSnapshot.child("pushups").getValue()).intValue();
                            sum[1]=+((Long)postSnapshot.child("situps").getValue()).intValue();
                            sum[2]=+((Long)postSnapshot.child("reps").getValue()).intValue();
                        }
                    }

                }

                //Creating string for displaying the average
                StringBuilder builder=new StringBuilder();
                builder.append("Pushups ");
                builder.append(Math.round(sum[0]/(float)(cnt-1)));
                builder.append("Situps ");
                builder.append(Math.round(sum[1]/(float)(cnt-1)));
                builder.append("Reps ");
                builder.append(Math.round(sum[2]/(float)(cnt-1)));


                //Displaying alert dialog
                AlertDialog.Builder builder1=new AlertDialog.Builder(SelectActivity.this);
                builder1.setMessage(builder.toString());
                builder1.setTitle("Info: ");
                builder1.setCancelable(true);
                builder1.show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
