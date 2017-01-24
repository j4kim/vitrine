package com.vitrine.vitrine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Adapter pour les listes de vitrines
 */

public class VitrineAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Vitrine> mDataSource;

    public VitrineAdapter(Context context, ArrayList<Vitrine> data)
    {
        mContext = context;
        mDataSource = data;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.list_item_vitrine, viewGroup, false);

        TextView nameTextView = (TextView) rowView.findViewById(R.id.vitrineName);

        Vitrine v = (Vitrine) getItem(i);
        nameTextView.setText(v.getName());

        return rowView;
    }
}
