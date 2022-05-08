package com.example.android.employeesmanagementsoftware.SiteDB;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.SiteDB.SiteRowData.SiteItem;
import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.Contracts.SiteContract;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.example.android.employeesmanagementsoftware.taskDB.TasksAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DepFragment extends Fragment implements SelectSiteListener{
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private  EmployeesManagementDbHelper mDataBase;
    private  Cursor cursor;
    private Context context;
    private  ArrayList<SiteItem> mValues;
    private MySiteRecyclerViewAdapter mAdapter;
    private static RecyclerView recyclerView;
    DatabaseReference dbref;

    public DepFragment() {
    }

    public static DepFragment newInstance(int columnCount) {
        DepFragment fragment = new DepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValues = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_site_lists, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("site");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            SiteItem site = new SiteItem();

                            site.setId(snapshot.child("id").getValue().toString());
                            site.setName(snapshot.child("name").getValue().toString());
                            site.setDetails(snapshot.child("description").getValue().toString());
//                        site.setId(snapshot.child("id").getValue().toString());
//                        site.setName(snapshot.child("name").getValue().toString());
//                        site.setDetails(snapshot.child("description").getValue().toString());

                            mValues.add(site);
                        }
                        catch (Exception e) {
                            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    RecyclerView recyclerView =  view.findViewById(R.id.site_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mAdapter = new MySiteRecyclerViewAdapter(mValues, getActivity(), DepFragment.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    //        return view;
     public void updateDepartmentList(EmployeesManagementDbHelper mDataBase){
         mValues = new ArrayList<>();


            if (mAdapter == null) {
            } else {
                mAdapter.notifyDataSetChanged();
            }
            cursor.close();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClicked(SiteItem item) {
        Toast.makeText(getActivity(), "Sitename - "+item.getName(), Toast.LENGTH_SHORT).show();         //

        Intent intent = new Intent(DepFragment.this.getActivity(), SiteActivity.class);
        intent.putExtra("departmentId", item.getId());
        startActivity(intent);
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SiteItem item);
    }
}
