package com.example.victo.logbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;

import static android.app.Activity.RESULT_OK;

public class VisitorsEntryFragment extends Fragment implements FragmentLifeCycle {

    @BindView(R.id.visitorNameEntry) EditText mVisitorNameEntry;
    @BindView(R.id.companyNameEntry) EditText mCompanyNameEntry;
    @BindView(R.id.visitorImage) ImageView mVisitorImage;
    @BindView(R.id.visitorIdEntry) TextView mVisitorId;
    @BindView(R.id.entryTimeValue) TextView mEntryTimeValue;
    @BindView(R.id.exitTimeValue) TextView mExitTimeValue;
    @BindView(R.id.visitorEntrySave) Button mVisitorEntrySave;
    @BindView(R.id.cameraButton) Button mCameraButton;

    private Box<VisitorDetails> mVisitorDetailsBox;
    private static int CAMERA_PIC_REQUEST = 1024;
//    private String visitorName;
//    private String visitorCompanyName;
    VisitorDetails mVisitorDetails;
    private String  visitorId;
    private long numberVisitorId;


    private String mCurrentPhotoPath;
    private static final String EXTRA_PHOTO_PATH = "current_photo_path";
    private Uri photoUri = null;
    ThumbnailUtils thumbnail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BoxStore boxStore = ((AppLogBook) getActivity().getApplication()).getBoxStore();
        mVisitorDetailsBox = boxStore.boxFor(VisitorDetails.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visitors_entry_layout, container, false);
        ButterKnife.bind(this,v);

        //time displayed once the visitor leaves the company.
        mExitTimeValue.setVisibility(View.INVISIBLE);

        Date date = new Date();
        String dateString = ((AppLogBook) getActivity().getApplication()).getTimefromDate(date);
        updateViews(dateString);
        Log.i("VisitorsEntryFragment", "Current time: " + dateString);

        mVisitorEntrySave.setOnClickListener(new View.OnClickListener() {
            String visitorName;
            String visitorCompanyName;
            long mEntryTime;

            @Override
            public void onClick(View view) {
                visitorName = mVisitorNameEntry.getText().toString();
                visitorCompanyName = mCompanyNameEntry.getText().toString();

                if ((visitorName == null)||(visitorCompanyName == null)){
                    Toast.makeText(getContext().getApplicationContext(), "Please enter visitor and company name", Toast.LENGTH_SHORT);
                }
                else{
                    mEntryTime = System.currentTimeMillis();
                    mVisitorDetails = new VisitorDetails();
                    mVisitorDetails.setCompanyName(visitorCompanyName);
                    mVisitorDetails.setFirstName(visitorName);
                    mVisitorDetails.setEntryTime(mEntryTime);

                    Date date = new Date();
                    String dateString = ((AppLogBook) getActivity().getApplication()).getTimefromDate(date);
                    mVisitorDetails.setEntryTimeString(dateString);
                    if (mVisitorDetailsBox.get(1) == null){
                        numberVisitorId = 1;
                    }
                    else{
                        List<VisitorDetails> visitorDetailsList= mVisitorDetailsBox.getAll();
                        //increment the count by 1, considering the current visitor
                        numberVisitorId = visitorDetailsList.size() + 1;
                    }
                    mVisitorDetails.setVisitorId(numberVisitorId);
                    //Set image uri to Visitor Details object
                    if (photoUri != null){
                        mVisitorDetails.setVisitorImageUri(photoUri.toString());
                        Log.i("VisitorsEntryFragment", "save onClick uri visitor image: " + mVisitorDetails.getVisitorImageUri().toString());
                    }
                    mVisitorDetailsBox.put(mVisitorDetails);

                    updateViews(dateString);
                }
                //clear the entries in name field once stored
                mVisitorNameEntry.getText().clear();
                mCompanyNameEntry.getText().clear();
            }
        });

        /*
            setting up on click listener for camera button
         */
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    try{
                        photoFile = createImageFile();
                    } catch (IOException ex){
                        Log.i("VisitorsEntryFragment", "Exception in creating file");
                    }
                }

                if (photoFile != null){
                    photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.example.android.fileprovider",
                            photoFile);
                    Log.i("VisitorsENtryFragment", "Yo, this is uri: " + photoUri);
                    Log.i("VisitorsENtryFragment", "Yo, this is currentPhotopath: " + mCurrentPhotoPath);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    }
            }
        });

        return v;
    }

    public void updateViews(String dateString){
        int numberVisitorId;
        String visitorId;
        if (mVisitorDetailsBox.get(1) == null){
            numberVisitorId = 1;
        }
        else{
            List<VisitorDetails> visitorDetailsList= mVisitorDetailsBox.getAll();
            //increment the count by 1, considering the current visitor
            numberVisitorId = visitorDetailsList.size() + 1;
        }

        visitorId = "TBV-" + numberVisitorId;
        mVisitorId.setText(visitorId);
        mEntryTimeValue.setText(dateString);
    }

    private File createImageFile() throws IOException{
        //Create an image file name
        String mTimeStamp = new SimpleDateFormat("hh:mm:ss a").format(new Date());
        String imageFilename = "JPEG_" + mTimeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("VisitorsEntryFragment", "File storage dir: "+ storageDir);
        File image = File.createTempFile(
                imageFilename, /*prefix*/
                ".jpg", /*suffix*/
                storageDir /*file path*/
        );
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if ((resultCode == RESULT_OK)){
//            if ((requestCode == CAMERA_PIC_REQUEST) & (data != null)){
            if (requestCode == CAMERA_PIC_REQUEST){
                try{
                    Bitmap help1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    mVisitorImage.setImageBitmap(thumbnail.extractThumbnail(help1, help1.getWidth(), help1.getHeight()));
                } catch (IOException ex){
                    Log.i("VisitorsEntryFragment", "Exception in setting image");
                }
            }
        }

        else{
            Log.i("VisitorsEntryFragment", "looks like Intent data is null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("VisitorsEntryFragment", "onStart()");
//        Date date = new Date();
//        String dateString = ((AppLogBook) getActivity().getApplication()).getTimefromDate(date);
//        updateViews(dateString);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("VisitorsEntryFragment", "onResume()");
        Date date = new Date();
        String dateString = ((AppLogBook) getActivity().getApplication()).getTimefromDate(date);
        updateViews(dateString);
    }

    @Override
    public void onBackground() {

    }

    @Override
    public void onDisplay() {
        Log.i("VisitorsEntryFragment", "onResume()");
        Date date = new Date();
        String dateString = ((AppLogBook) getActivity().getApplication()).getTimefromDate(date);
        updateViews(dateString);
    }

}
