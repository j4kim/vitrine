package com.vitrine.vitrine.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.vitrine.vitrine.ContributeActivity;
import com.vitrine.vitrine.NetworkTools;
import com.vitrine.vitrine.R;
import com.vitrine.vitrine.TabActivity;
import com.vitrine.vitrine.User;
import com.vitrine.vitrine.Vitrine;
import com.vitrine.vitrine.VitrineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ContributeFragment extends Fragment {

    private RetrieveVitrineTask mRetrieveTask;

    // UI references
    private View mProgressView;
    private ListView mContributeListView;
    private ArrayList<Vitrine> mVitrineList;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity fa = super.getActivity();
        LinearLayout llLayout = (LinearLayout) inflater.inflate(R.layout.fragment_contribute, container, false);

        user = ((TabActivity)getActivity()).getUser();

        mVitrineList = new ArrayList<>();

        mContributeListView = (ListView) llLayout.findViewById(R.id.contribute_listview);
        mProgressView = llLayout.findViewById(R.id.contribute_progress);

        VitrineAdapter adapter = new VitrineAdapter(fa, mVitrineList);
        mContributeListView.setAdapter(adapter);

        mContributeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ContributeActivity.class);
                intent.putExtra("vitrine", mVitrineList.get(i));
                startActivity(intent);
            }
        });

        showProgress(true);
        retrieveVitrines();

        return llLayout;
    }


    private void retrieveVitrines(){
        if (mRetrieveTask != null) {
            return;
        }

        mRetrieveTask = new RetrieveVitrineTask(user.getToken());
        mRetrieveTask.execute((Void) null);

    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mContributeListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mContributeListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContributeListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContributeListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    /**
     * Represents an asynchronous retrieving task for vitrines
     */
    public class RetrieveVitrineTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserToken;

        RetrieveVitrineTask(String userToken) {
            mUserToken = userToken;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //attempt retrieving a list of vitrines.
            boolean success = false;
            try {
                String response = NetworkTools.connect(getString(R.string.subscription_url) + "?token=" + mUserToken);

                JSONObject jsonObject = new JSONObject(response);
                JSONArray responses = jsonObject.getJSONArray("vitrines");
                for(int i = 0; i < responses.length()-1; i++)
                {
                    String str = responses.getJSONObject(i).toString();
                    Vitrine v = new Vitrine(str);
                    // Add picture path to vitrines
                    try {
                        String pictureList = NetworkTools.connect(getString(R.string.getpictures_url) + "?vitrine_id=" + v.getId());
                        JSONObject pictureObject = new JSONObject(pictureList);
                        JSONArray pictureArray = pictureObject.getJSONArray("pictures");
                        for (int j = 0; j < pictureArray.length() - 1; j++) {
                            v.addPicture(pictureArray.getJSONObject(i).getString("path"));
                        }

                        mVitrineList.add(v);
                    } catch (IOException |JSONException e)
                    {
                        // Error while retrieving pictures
                        e.printStackTrace();
                    }
                }
                success = true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRetrieveTask = null;
            showProgress(false);

            if (success) {
                showProgress(false);
            } else {
                //Probleme lors de la reception des vitrines

            }
        }

        @Override
        protected void onCancelled() {
            mRetrieveTask = null;
            showProgress(false);
        }
    }
}
