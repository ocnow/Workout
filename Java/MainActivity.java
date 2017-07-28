package com.example.omkar.workout2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends BaseActivity {


    private static final String TAG="MAIN";

    private EditText mEmailField;
    private EditText mPassField;

    private Button mSignInButton;
    private Button mSignUpButton;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();

        mEmailField=(EditText)findViewById(R.id.email_text);
        mPassField=(EditText)findViewById(R.id.pass_text);

        mSignInButton=(Button)findViewById(R.id.signin_button);
        mSignUpButton=(Button)findViewById(R.id.signout_button);



    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void signUp(View v)
    {
        String email=mEmailField.getText().toString();
        String password=mPassField.getText().toString();
        Log.d(TAG,"SignUp:"+email);

        if(!validateForm())
        {
            return;
        }

        Toast.makeText(getApplicationContext(),"Creaing user",Toast.LENGTH_SHORT).show();

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d(TAG,"CreatedUserWithEmail:Success");
                    FirebaseUser user=mAuth.getCurrentUser();
                    //updateUI(user);
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),"Created user successfully",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.w(TAG,"CreateUserWithEmail:Failure");
                    FirebaseUser user=mAuth.getCurrentUser();
                    //updateUI(user);
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),"Created user failure",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signIn(View v)
    {
        final String email=mEmailField.getText().toString();
        String password=mPassField.getText().toString();
        Log.d(TAG,"signIn:"+email);
        if(!validateForm())
        {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"signInWithEmail:Success");
                    FirebaseUser user=mAuth.getCurrentUser();
                    Intent intent=new Intent(MainActivity.this,SelectActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.w(TAG,"signInWithEmail:Failure");
                    FirebaseUser user=mAuth.getCurrentUser();
                   // updateUI(user);
                    hideProgressDialog();

                }


            }
        });
    }

    private boolean validateForm(){
        boolean valid=true;

        String email=mEmailField.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            mEmailField.setError("Required:");
            valid=false;
        }else{
            mEmailField.setError(null);
        }

        String pass=mEmailField.getText().toString();
        if(TextUtils.isEmpty(pass))
        {
            mPassField.setError("Required:");
            valid=false;
        }else{
            mPassField.setError(null);
        }
        return valid;
    }

}
