package com.example.victo.logbook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 700;
    BoxStore mBoxstore;
    LogBookId mLogBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

//        (AppLogBook)
        final Box<LogBookId> logBookIdBox;
//        final Box <LogBookId> logBookIdBox= ((AppLogBook) getApplication()).getBoxStore().boxFor(LogBookId.class);
        BoxStore boxStore= ((AppLogBook) getApplication()).getBoxStore();
        logBookIdBox = boxStore.boxFor(LogBookId.class);
        EditText mCompanyId = (EditText) findViewById(R.id.id_editText);
        final TextView mCompanyIdText = (TextView) findViewById(R.id.company_id_textview);
        Button mSaveButton = (Button) findViewById(R.id.save_id_button);

        mLogBookId = logBookIdBox.get(1);
        if (mLogBookId == null){
            mLogBookId = new LogBookId();
            mCompanyId.setVisibility(View.VISIBLE);
            mCompanyIdText.setVisibility(View.VISIBLE);
            mSaveButton.setVisibility(View.VISIBLE);

//            final String companyIdString = mCompanyIdText.getText().toString();
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                final String companyIdString = mCompanyIdText.getText().toString();
                @Override
                public void onClick(View view) {
                    if (companyIdString.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter valid company Id", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        mLogBookId.setCompanyName(companyIdString);
                        mLogBookId.setStartingTime(System.currentTimeMillis());
                        logBookIdBox.put(mLogBookId);
                        Intent mainIntent = new Intent(SplashActivity.this, LogBookActivity.class);
                        startActivity(mainIntent);
                    }
                }
            });

        } else{
            mCompanyId.setVisibility(View.INVISIBLE);
            mCompanyIdText.setVisibility(View.INVISIBLE);
            mSaveButton.setVisibility(View.INVISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent;
                    mainIntent = new Intent(SplashActivity.this, LogBookActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}
