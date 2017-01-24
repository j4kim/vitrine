package com.vitrine.vitrine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Activité pour s'abonner / se désabonner
 */

public class VitrineInfoActivity extends AppCompatActivity {

    private Vitrine mVitrine;
    private Button btn_unsubscribe;
    private Button btn_subscribe;
    private Button btn_show;
    private ProgressDialog dialog;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine_info);

        // récupère l'utilisateur et la vitrine depuis l'intent qui a appellé cette activité
        Intent i = getIntent();
        mVitrine = i.getParcelableExtra("vitrine");
        User user = i.getParcelableExtra("user");

        // récupère les 3 boutons depuis le layout
        btn_show = (Button) findViewById(R.id.btn_show);
        btn_subscribe = (Button) findViewById(R.id.btn_subscribe);
        btn_unsubscribe = (Button) findViewById(R.id.btn_unsubscribe);

        // set le nom de la vitrine comme titre de l'activité
        setTitle(mVitrine.getName());

        // itinitialise l'objet Volley qui permet d'envoyer des requêtes http
        queue = Volley.newRequestQueue(this);

        // crée la string des paramètres GET et l'inclue à l'url
        final String params = String.format(
                "?token=%s&vitrine_id=%s",
                user.getToken(),
                mVitrine.getId());
        String is_subscribed_url = getString(R.string.is_subscribed) + params;

        // affiche le loader
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        // prépare la requête
        StringRequest is_subscribed_reqest = new StringRequest(
                Request.Method.GET,
                is_subscribed_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // le serveur retourne le nombre d'entrées dans la table subscribed
                        // qui correspondent au couple user/vitrine
                        if (response.equals("0")){
                            // l'utilisateur n'est pas abonné à la vitrine, on lui propose de s'abonner
                            btn_subscribe.setVisibility(View.VISIBLE);
                        }else if (response.equals("1")){
                            // l'utilisateur est abonné à la vitrine, on lui propose de se désabonner
                            btn_unsubscribe.setVisibility(View.VISIBLE);
                        }
                        // cache le loader
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VitrineInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // exécute la requête puis affiche l'un des deux boutons subscribe ou unsubscribe
        queue.add(is_subscribed_reqest);

        // lors du clic sur show vitrine, appelle l'activité VitrineActivity qui affiche les photos dans cette vitrine
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitrineInfoActivity.this, VitrineActivity.class);
                intent.putExtra("vitrine", mVitrine);
                startActivity(intent);
            }
        });

        // exécute la requête d'abonnement à la vitrine
        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.subscribe) + params;
                execRequest(url);
            }
        });

        // exécute la requête de désabonnement à la vitrine
        btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.unsubscribe) + params;
                execRequest(url);
            }
        });

    }

    // exécute une requête http get, puis quitte l'activité et affiche le résultat de la requête dans un Toast
    private void execRequest(String url) {
        StringRequest req = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(VitrineInfoActivity.this, response, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VitrineInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(req);
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
    }
}
