package com.vitrine.vitrine.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vitrine.vitrine.R;
import com.vitrine.vitrine.TabActivity;
import com.vitrine.vitrine.User;
import com.vitrine.vitrine.Vitrine;
import com.vitrine.vitrine.VitrineActivity;
import com.vitrine.vitrine.VitrineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubscribedVitrinesFragment extends Fragment {

    // UI references
    private ListView mSubscribedListView;
    private ArrayList<Vitrine> mVitrineList;
    private VitrineAdapter mVitrineAdapter;
    private View mProgressView;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity fa = super.getActivity();
        LinearLayout llLayout = (LinearLayout) inflater.inflate(R.layout.fragment_subscribed_vitrines, container, false);

        user = ((TabActivity)getActivity()).getUser();

        mVitrineList = new ArrayList<>();

        mSubscribedListView = (ListView) llLayout.findViewById(R.id.subscribed_listview);
        mProgressView = llLayout.findViewById(R.id.subs_progress);
        mProgressView.setVisibility(View.VISIBLE);

        mVitrineAdapter  = new VitrineAdapter(fa, mVitrineList);
        mSubscribedListView.setAdapter(mVitrineAdapter);

        mSubscribedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), VitrineActivity.class);
                intent.putExtra("vitrine", mVitrineList.get(i));
                startActivity(intent);
            }
        });

        retrieveSubscribedVitrines();

        return llLayout;
    }

    private void retrieveSubscribedVitrines(){

        String url = getString(R.string.subscription_url) + "?token=" + user.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray responses = jsonObject.getJSONArray("vitrines");
                    for (int i = 0; i < responses.length(); i++) {
                        String str = responses.getJSONObject(i).toString();
                        Vitrine v = new Vitrine(str);

                        // Add picture path to vitrines
                        mVitrineList.add(v);
                    }
                    mProgressView.setVisibility(View.GONE);
                    mVitrineAdapter.notifyDataSetChanged();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY_ERROR", error.getMessage());
            }
        });

        ((TabActivity)getActivity()).getQueue().add(stringRequest);
    }
}
