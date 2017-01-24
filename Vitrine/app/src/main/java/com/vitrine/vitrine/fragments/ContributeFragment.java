package com.vitrine.vitrine.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.vitrine.vitrine.ContributeActivity;
import com.vitrine.vitrine.CreateActivity;
import com.vitrine.vitrine.R;
import com.vitrine.vitrine.TabActivity;
import com.vitrine.vitrine.User;
import com.vitrine.vitrine.Vitrine;
import com.vitrine.vitrine.VitrineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Fragment pour choisir a quelle vitrine contribuer
 */

public class ContributeFragment extends Fragment {
    // UI references
    private ProgressDialog dialog;
    private ListView mContributeListView;
    private VitrineAdapter mVitrineAdapter;
    private ArrayList<Vitrine> mVitrineList;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity fa = super.getActivity();
        LinearLayout llLayout = (LinearLayout) inflater.inflate(R.layout.fragment_contribute, container, false);

        user = ((TabActivity)getActivity()).getUser();

        mVitrineList = new ArrayList<>();

        mContributeListView = (ListView) llLayout.findViewById(R.id.contributeListView);

        Button btnNewVitrine = (Button) llLayout.findViewById(R.id.btnNewVitrine);

        mVitrineAdapter = new VitrineAdapter(fa, mVitrineList);
        mContributeListView.setAdapter(mVitrineAdapter);

        mContributeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ContributeActivity.class);
                intent.putExtra("vitrine", mVitrineList.get(i));
                startActivity(intent);
            }
        });

        btnNewVitrine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
        });

        dialog = ProgressDialog.show(fa, "",
                "Loading. Please wait...", true);
        retrieveVitrines();


        return llLayout;
    }



    private void retrieveVitrines(){

        LatLng latLng = ((TabActivity)getActivity()).getLocation();
        String lat = String.valueOf(latLng.latitude);
        String lon = String.valueOf(latLng.longitude);

        String url = getString(R.string.get_vitrines_here_url) + "?lat=" + lat + "&long=" + lon;
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
                    dialog.dismiss();
                    mVitrineAdapter.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
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
