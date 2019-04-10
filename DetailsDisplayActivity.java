package com.example.victo.logbook;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import io.objectbox.Box;
import io.objectbox.BoxStore;

import static com.example.victo.logbook.VisitorDetails_.visitorImageUri;
import static com.example.victo.logbook.VisitorsListFragment.EXTRA_VISITOR_ID;

public class DetailsDisplayActivity extends AppCompatActivity{

    @BindView(R.id.visitorNameEntry) EditText mVisitorName;
    @BindView(R.id.companyNameEntry) EditText mCompanyName;
    @BindView(R.id.entryTimeValue) TextView mEntryTime;
    @BindView(R.id.exitTimeValue) TextView mExitTime;
    @BindView(R.id.visitorIdEntry) TextView mVisitorIdEntry;
    @BindView(R.id.visitorLeavecheckBox) CheckBox mVisitorLeaveCheckBox;
    @BindView(R.id.visitorEntrySave) Button mSaveButton;
    @BindView(R.id.visitorLeaveCheck) TextView mVisitorLeaveString;
    @BindView (R.id.cameraButton) Button mCameraButton;
    @BindView(R.id.visitorImage) ImageView mVisitorImage;

    private Box <VisitorDetails> mVisitorDetailsBox;
    private VisitorDetails mVisitorDetails;
    private VisitorDetails mVisitorDetailsCheck;
    ThumbnailUtils thumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitors_entry_layout);
        ButterKnife.bind(this);

        mCameraButton.setVisibility(View.GONE);

        BoxStore boxStore = ((AppLogBook) getApplication()).getBoxStore();
        mVisitorDetailsBox = boxStore.boxFor(VisitorDetails.class);


        final int mVisitorId = getIntent().getIntExtra(EXTRA_VISITOR_ID, -1);
        if (mVisitorId == -1){
            Log.i("DetailsDisplayActivity","Visitor ID sent from Listfragment incorrect");
        }
        else{
            mVisitorDetails = mVisitorDetailsBox.query().equal(VisitorDetails_.visitorId, mVisitorId)
                                                                                .build().findFirst();
            if (mVisitorDetails == null){
                Log.i("DetailsDisplayActivity", "VisitorDetails not exisiting with given Visitor Id");
            }
            else{
                String visitorIdEntry = "TBV-" + mVisitorDetails.getVisitorId();
                mVisitorIdEntry.setText(visitorIdEntry.toString());
                mVisitorName.setText(mVisitorDetails.getFirstName().toString());
                mVisitorName.setEnabled(false);
                mCompanyName.setText(mVisitorDetails.getCompanyName().toString());
                mCompanyName.setEnabled(false);
                mEntryTime.setText(mVisitorDetails.getEntryTimeString().toString());
                //set visitor image
                if ((mVisitorDetails.getVisitorImageUri() != null)){
                    Uri visitorImageUri = Uri.parse(mVisitorDetails.getVisitorImageUri());
                    try{
                        Bitmap help1 = MediaStore.Images.Media.getBitmap(getContentResolver(), visitorImageUri);
                        mVisitorImage.setImageBitmap(thumbnail.extractThumbnail(help1, help1.getWidth(), help1.getHeight()));
                    } catch (IOException ex){
                        Log.i("VisitorsEntryFragment", "Exception in setting image");
                    }
                }

                else{
                    Log.i("VisitorsEntryFragment", "no image available for the selected visitor");
                }

//                if (visitorImageUri != null){
//                    try{
//                        Bitmap help1 = MediaStore.Images.Media.getBitmap(getContentResolver(), visitorImageUri);
//                        mVisitorImage.setImageBitmap(thumbnail.extractThumbnail(help1, help1.getWidth(), help1.getHeight()));
//                    } catch (IOException ex){
//                        Log.i("VisitorsEntryFragment", "Exception in setting image");
//                    }
//                }

                boolean hasVisitorLeft = mVisitorDetails.isHasVisitorLeft();
                if (!hasVisitorLeft){
                    mVisitorLeaveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxState) {
                            Log.i("DetailsDisplayActivity", "check box state: " + checkBoxState);
                            if (checkBoxState){
                                mVisitorDetails.setHasVisitorLeft((boolean) checkBoxState);
                                Date date = new Date();
//                                String dateString = ((AppLogBook) getActivity()).getTimefromDate(date);
                                String dateString = ((AppLogBook) getApplicationContext()).getTimefromDate(date);
                                mExitTime.setText(dateString);
                                mVisitorDetails.setExitTimeString(dateString);
//                                mVisitorDetailsBox.put(mVisitorDetails);
                            }
                        }
                    });
                }
                else{
                    mExitTime.setText(mVisitorDetails.getExitTimeString());
                    mVisitorLeaveString.setText("Visitor left.");
                    mSaveButton.setVisibility(View.GONE);
                    mVisitorLeaveCheckBox.setVisibility(View.GONE);
                }
                mSaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean hasVisitorLeft = mVisitorDetails.isHasVisitorLeft();
                        if (!hasVisitorLeft){
                            Toast.makeText(getApplicationContext(), "Please check the box if the visitor has left company",
                                                                                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mVisitorDetailsBox.put(mVisitorDetails);
                            disableSaveOnceVisitorLeave(mVisitorId);
                        }
                    }
                });


            }
        }

    }

    public void disableSaveOnceVisitorLeave(int mVisitorId){
        mVisitorDetailsCheck = mVisitorDetailsBox.query().equal(VisitorDetails_.visitorId, mVisitorId)
                .build().findFirst();
        boolean insertCheck = mVisitorDetailsCheck.isHasVisitorLeft();
        if (insertCheck){
            mSaveButton.setVisibility(View.GONE);
        }
        Log.i("DetailsDisplayActivity", "readback isHasVisitorLeft from VisitorDetails box at onSave: "
                + insertCheck);
    }
}
