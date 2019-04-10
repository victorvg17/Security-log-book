package com.example.victo.logbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class VisitorsSummaryFragment extends Fragment implements FragmentLifeCycle {


    private Box<VisitorDetails> mVisitorDetails;
    private int mTotalVisitors = 0;
    private int mVisitorsRemaining = 0;
    private int mVisitorsLeft = 0;

    @BindView(R.id.totalNumberVisitors) TextView textTotalVisitors;
    @BindView(R.id.totalVisitorsRemaining) TextView textTotalVisitorsRemaining;
    private String totalVisitors;
    private String totalVisitorsRemaining;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BoxStore mBoxStore = ((AppLogBook) getActivity().getApplication()).getBoxStore();
        mVisitorDetails = mBoxStore.boxFor(VisitorDetails.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.day_summary_layout, container, false);
//        TextView textTotalVisitors = (TextView) v.findViewById(R.id.totalNumberVisitors);
//        TextView textTotalVisitorsRemaining = (TextView) v.findViewById(R.id.totalVisitorsRemaining);
        syncNumberOfVisitors();

        ButterKnife.bind(this, v);
        totalVisitors = " " + mTotalVisitors;
        totalVisitorsRemaining = " " + mVisitorsRemaining;
        Log.i("VisitorsSummaryFragment", "Visitors total: " + mTotalVisitors +
                " Visitors remaining: " + mVisitorsRemaining);

        if ((totalVisitors != null) & (totalVisitorsRemaining != null)){
            textTotalVisitors.setText(totalVisitors);
            textTotalVisitorsRemaining.setText(totalVisitorsRemaining);
        }

        else{
            Log.i("VisitorsSummaryFragment", "String totalVisitors/ totalVisitorsRemaining is null");
        }
        return v;
    }

    public void syncNumberOfVisitors(){
        List <VisitorDetails> itemsVisitors = mVisitorDetails.getAll();
        mTotalVisitors = itemsVisitors.size();
        for (VisitorDetails visitorDetails : itemsVisitors){
            if (visitorDetails.isHasVisitorLeft()){
                mVisitorsLeft++;
            }
        }
         mVisitorsRemaining = mTotalVisitors - mVisitorsLeft;
        mVisitorsLeft = 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("VisitorsSummaryFragment", "onStart");
        syncNumberOfVisitors();
    }

    @Override
    public void onBackground() {

    }

    @Override
    public void onDisplay() {
        Log.i("VisitorsSummaryFragment", "onDisplay");
        syncNumberOfVisitors();
    }
}
