package com.example.android.employeesmanagementsoftware.SiteDB;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementsoftware.SiteDB.DepFragment.OnListFragmentInteractionListener;
import com.example.android.employeesmanagementsoftware.SiteDB.SiteRowData.SiteItem;
import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.taskDB.SelectTaskListener;

import java.util.List;

public class MySiteRecyclerViewAdapter extends RecyclerView.Adapter<MySiteRecyclerViewAdapter.ViewHolder> {

    private final List<SiteItem> mValues;
    private final Context mListener;
    private SelectSiteListener listener;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public MySiteRecyclerViewAdapter(List<SiteItem> items, Context listener, SelectSiteListener listener2) {
        mValues = items;
        mListener = listener;
        this.listener = listener2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_site, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).name);
        holder.mdescriptionView.setText(mValues.get(position).details);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
                Context context = v.getContext();
                Intent intent = new Intent(context, SiteActivity.class);
                intent.putExtra("departmentId",mValues.get(position).id); // set id to Department activity
                context.startActivity(intent);

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(mValues.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mdescriptionView;
        public CardView cardView;
        public SiteItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.department_name);
            mdescriptionView = (TextView) view.findViewById(R.id.department_description);
            cardView = view.findViewById(R.id.card_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mdescriptionView.getText() + "'";
        }
    }
}
