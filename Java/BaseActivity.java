package com.example.omkar.workout2;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Omkar on 6/10/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting

    public ProgressDialog mProgressDialog;

    public void showProgressDialog()
    {
        if(mProgressDialog==null)
        {
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog()
    {
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
