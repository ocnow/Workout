package com.example.omkar.workout2;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkoutActivity extends AppCompatActivity {

    Button startButton,stopButton;
    private Handler handler;
    long timeInMilliseconds = 0L;
    long starttime=0L;
    TextView timerValue;

    Spinner spinner;
    Button buttons[]=new Button[3];
    public static int setSize[]={0,0,0};
    public static int sets[]={0,0,0};
    public static int sum[]={0,0,0};
    boolean fir[]={true,true,true};

    int cur;

    TextView setsDoneTextView;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;



    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds=SystemClock.uptimeMillis()-starttime;
            int sec=(int)timeInMilliseconds/1000;
            int min=sec/60;
            sec=sec%60;
            int milisec=(int)timeInMilliseconds%1000;


            timerValue=(TextView)findViewById(R.id.timerValue);
            timerValue.setText(Integer.toString(min)+":"+String.format("%02d",sec)+":"+String.format("%02d",milisec));
            handler.postDelayed(runnable,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        handler=new Handler();
        startButton=(Button)findViewById(R.id.startButton);
        stopButton=(Button)findViewById(R.id.endButton);
        timerValue=(TextView)findViewById(R.id.timerValue);

        spinner=(Spinner)findViewById(R.id.spinner);

        buttons[0]=(Button)findViewById(R.id.pushupButton);
        buttons[2]=(Button)findViewById(R.id.repButton);
        buttons[1]=(Button)findViewById(R.id.situpButton);

        cur=0;

        setsDoneTextView=(TextView)findViewById(R.id.setsDoneTextview);

        buttons[0].setBackgroundColor(0xffff0000);

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons[2].setBackgroundColor(0xffff0000);
                buttons[0].setBackgroundColor(0xff444444);
                buttons[1].setBackgroundColor(0xff444444);
                cur=2;
                checkIfFirst();
            }
        });


        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons[1].setBackgroundColor(0xffff0000);
                buttons[0].setBackgroundColor(0xff444444);
                buttons[2].setBackgroundColor(0xff444444);
                cur=1;
                checkIfFirst();
            }
        });

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons[0].setBackgroundColor(0xffff0000);
                buttons[1].setBackgroundColor(0xff444444);
                buttons[2].setBackgroundColor(0xff444444);
                cur=0;
                checkIfFirst();
            }
        });

        String setSizes[]=new String[56];
        for(int i=4;i<60;i++)
        {
            setSizes[i-4]=Integer.toString(i);
        }
        ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,setSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSize[cur]=Integer.parseInt((String)parent.getSelectedItem());
                fir[cur]=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fir[cur]=true;
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                starttime= SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                if(!fir[cur]) {
                    sets[cur]++;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerValue.setText("00:00:00");
                handler.removeCallbacks(runnable);

                if(!fir[cur]) {
                    sum[cur] += setSize[cur];
                    setsDoneTextView.setText(Integer.toString(sets[cur]));
                    updateDatabase();
                }
            }
        });

    }

    private void updateDatabase() {
        String uid=mAuth.getCurrentUser().getUid();
        String names[]={"pushups","situps","reps"};
        mDatabase.child(uid).child(names[cur]).setValue(sum[cur]);
    }

    private void checkIfFirst()
    {
        if(fir[cur]){
            setsDoneTextView.setText("0");
            spinner.setSelection(0);
            AlertDialog.Builder builder=new AlertDialog.Builder(WorkoutActivity.this);
            builder.setMessage("Enter the set size firstly.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setTitle("WARNING");
            builder.show();

        }
    }

}