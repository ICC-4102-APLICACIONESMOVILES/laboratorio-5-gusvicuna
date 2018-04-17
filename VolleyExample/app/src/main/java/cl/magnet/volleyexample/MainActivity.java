package cl.magnet.volleyexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {

    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkManager = NetworkManager.getInstance(this);

        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        TextView text_usuario= findViewById(R.id.text_user);
        Button boton_login=findViewById(R.id.button_login);
        Button boton_logout=findViewById(R.id.button_logout);
        String usuario_con=sharedPref.getString("Usuarios",null);

        Bundle extras=getIntent().getExtras();
        if (extras!=null) {
            String usuario = extras.getString("usuario");
            usuario_con = usuario;
            String password= extras.getString("password");

            try {
                networkManager.login(usuario, password, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        getForms();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        if (usuario_con!=null){
            boton_login.setVisibility(View.INVISIBLE);
            boton_logout.setVisibility(View.VISIBLE);

        }
        text_usuario.setText(usuario_con);

        editor.putString("Usuarios",usuario_con);
        editor.commit();
    }

    public void onLoginClick(View view){

        try {
            networkManager.login("ignacio@magnet.cl", "usuarioprueba", new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    getForms();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
                    System.out.println(error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getForms(){
        networkManager.getForms(new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                System.out.println(error);
            }
        });
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goLogout(View view){
        Button boton_login=findViewById(R.id.button_login);
        Button boton_logout=findViewById(R.id.button_logout);
        boton_login.setVisibility(View.VISIBLE);
        boton_logout.setVisibility(View.INVISIBLE);
    }
}
