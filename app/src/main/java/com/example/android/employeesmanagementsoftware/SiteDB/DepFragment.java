package com.example.android.employeesmanagementsoftware.SiteDB;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementsoftware.SiteDB.SiteRowData.SiteItem;
import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.Contracts.SiteContract;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;

import java.util.ArrayList;
import java.util.List;


public class DepFragment extends Fragment {
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
    private  List<SiteItem> mValues;
    private MySiteRecyclerViewAdapter mAdapter;
    private static RecyclerView recyclerView;

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

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mDataBase = new EmployeesManagementDbHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mValues = new ArrayList<>();
            cursor = mDataBase.getAllDepartments();
            if (cursor.moveToFirst()){
                do{
                    String name,description;
                    Long id = Long.parseLong(cursor.getString(cursor.getColumnIndex(SiteContract.DepartmentEntry._ID)));
                    name = cursor.getString(cursor.getColumnIndex(SiteContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME));
                    description = cursor.getString(cursor.getColumnIndex(SiteContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION));
                    SiteItem dataProvider = new SiteItem(id,name,description);
                    mValues.add(dataProvider);
                }while (cursor.moveToNext());
            }
            cursor.close();
            mAdapter = new MySiteRecyclerViewAdapter(mValues, mListener);
            recyclerView.setAdapter(mAdapter);
            recyclerView.invalidate();
        }

        return view;
    }
     public void updateDepartmentList(EmployeesManagementDbHelper mDataBase){
         mValues = new ArrayList<>();
         cursor =  mDataBase.getAllDepartments();
        if (cursor.moveToFirst()) {
            do {
                String  name, description;
                Long id ;
                id = Long.parseLong(cursor.getString(cursor.getColumnIndex(SiteContract.DepartmentEntry._ID)));
                name = cursor.getString(cursor.getColumnIndex(SiteContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME));
                description = cursor.getString(cursor.getColumnIndex(SiteContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION));
                SiteItem dataProvider = new SiteItem(id, name, description);
                mValues.add(dataProvider);
            } while (cursor.moveToNext());
        }
            if (mAdapter == null) {
                mAdapter = new MySiteRecyclerViewAdapter(mValues,mListener);
                recyclerView.setAdapter(mAdapter);
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


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SiteItem item);
    }
}
