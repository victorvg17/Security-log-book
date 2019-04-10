package com.example.victo.logbook;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class VisitorsListFragment extends Fragment implements FragmentLifeCycle {

    private Box<VisitorDetails> mVisitorDetails;
    private boolean mVisitorsEntered = false;
    private RecyclerView visitorsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private VisitorDetails mCurrVisitor;
    public static final String EXTRA_VISITOR_ID = "visitor_id_selected_visitor";
//    public List <VisitorDetails> visitorDetailsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("VisitorsListFragment", "OnCreate");
        super.onCreate(savedInstanceState);

        BoxStore boxStore = ((AppLogBook) getActivity().getApplication()).getBoxStore();
        mVisitorDetails = boxStore.boxFor(VisitorDetails.class);
        mCurrVisitor = mVisitorDetails.query().orderDesc(VisitorDetails_.visitorId).build().findFirst();
        Log.i("VisitorsListFragment", "Latest visitor Id: " + mCurrVisitor.getVisitorId());
        if (mVisitorDetails.get(1) != null){
            mVisitorsEntered = true;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visitors_list_layout, container, false);

        visitorsRecyclerView = (RecyclerView) v.findViewById(R.id.visitors_recycler_view);
        visitorsRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        visitorsRecyclerView.setLayoutManager(layoutManager);

//        Drawable mDivider = getResources().getDrawable(R.drawable.drawable_divider);
        Drawable mDivider = ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.drawable_divider);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(visitorsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(mDivider);
        visitorsRecyclerView.addItemDecoration(itemDecoration);

        List <VisitorDetails> visitorDetailsList= mVisitorDetails.getAll();
//        visitorDetailsList= mVisitorDetails.getAll();

        if (visitorDetailsList == null){
            visitorDetailsList = new ArrayList<VisitorDetails>();
        }
        mAdapter = new VisitorsListAdapter(visitorDetailsList);
        visitorsRecyclerView.setAdapter(mAdapter);

        return v;
        }

    public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorsListViewHolder>{
        List <VisitorDetails> visitorDetailsList;
        private long currVisitorId;


        public VisitorsListAdapter(List <VisitorDetails> items){
            if (mVisitorsEntered){
                currVisitorId = mCurrVisitor.getVisitorId();
            }

            if (items.size() < 10){
                visitorDetailsList = new ArrayList<>(items);
                for (int i = 0; i < 10; i++){
                    visitorDetailsList.add(new VisitorDetails(i, "Rajan", "CBI"));
                }
            }
            else {
                visitorDetailsList = items;
            }
        }

        public class VisitorsListViewHolder extends RecyclerView.ViewHolder{

            VisitorDetails mVisitorDetails;

            @BindView(R.id.visitorIdListValue) TextView mVisitorIdList;
            @BindView(R.id.visitorCompanyListValue) TextView mVisitorCompanyNameList;
            @BindView(R.id.visitorNameListValue) TextView mVisitorNameList;

            public VisitorsListViewHolder(LayoutInflater inflater, ViewGroup parent){
                super(inflater.inflate(R.layout.visitors_list_entity, parent, false));
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mVisitorsEntered){
//                            Toast.makeText(getActivity(), "entity details will be up soon", Toast.LENGTH_SHORT).show();
                            long mVisitorId = mVisitorDetails.getVisitorId();
                            Log.i("VisitorsListFragment", "visitor ID of the selected  position: " + mVisitorId);
                            Intent detailsDisplayIntent = new Intent(getActivity().getApplicationContext(), DetailsDisplayActivity.class);
                            detailsDisplayIntent.putExtra(EXTRA_VISITOR_ID, (int) mVisitorId);
                            startActivity(detailsDisplayIntent);
                        }
                        else{
                            Log.i("VisitorsListFragment", "OnClick Listener Recycler view entry, mVisitorsEntered: " + mVisitorsEntered);
                        }
                    }
                });
            }

            public void bind(VisitorDetails visitorDetails, boolean hasVisitorLeft){
                mVisitorDetails = visitorDetails;
                String visitorId = "TBV-" + visitorDetails.getVisitorId();
                if (hasVisitorLeft){
                    itemView.setBackgroundColor(getResources().getColor(R.color.visitorLeftColor));
                }
                else{
                    if ((visitorDetails.getCompanyName().toString() == "CBI") & (visitorDetails.getFirstName().toString() == "Rajan")){
                        itemView.setBackgroundColor(getResources().getColor(R.color.visitorNotActiveColor));
                    }
                    else{
                        itemView.setBackgroundColor(getResources().getColor(R.color.visitorActiveColor));
                    }
                }

                mVisitorIdList.setText(visitorId);
                mVisitorNameList.setText(visitorDetails.getFirstName().toString());
                mVisitorCompanyNameList.setText(visitorDetails.getCompanyName().toString());
            }

        }

        @Override
        public void onBindViewHolder(@NonNull VisitorsListViewHolder visitorsListViewHolder, int position) {
            boolean hasVisitorLeft = visitorDetailsList.get(position).isHasVisitorLeft();
            visitorsListViewHolder.bind(visitorDetailsList.get(position), hasVisitorLeft);
        }

        @NonNull
        @Override
        public VisitorsListAdapter.VisitorsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater =LayoutInflater.from(getActivity());
            VisitorsListViewHolder holder = new VisitorsListViewHolder(layoutInflater, viewGroup);

            return holder;
        }

        @Override
        public int getItemCount() {
            return visitorDetailsList.size();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("VisitorsListFragment", "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
//        visitorDetailsList= mVisitorDetails.getAll();
        Log.i("VisitorsListFragment", "onResume()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackground() {
        Log.i("VisitorsListFragment", "onBackground()");
//        visitorDetailsList= mVisitorDetails.getAll();
    }

    @Override
    public void onDisplay() {
        Log.i("VisitorsListFragment", "OnDisplay");
        if (!mVisitorsEntered){
            if (mVisitorDetails.get(1) != null){
                mVisitorsEntered = true;
            }
        }
    }
}
