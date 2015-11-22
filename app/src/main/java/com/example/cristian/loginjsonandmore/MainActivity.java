package com.example.cristian.loginjsonandmore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cristian.loginjsonandmore.DataBaseHelper.DBHelper;
import com.example.cristian.loginjsonandmore.ViewModels.UserViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private EditText etUserName, etPassword;
    DBHelper helper;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Controles
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading, Please wait...");

        //Eventos
        btnLogin.setOnClickListener(this);

        //Otros metodos
        helper = new DBHelper(this);

    }

    public class JsonLogin extends AsyncTask<String,String,List<UserViewModel>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<UserViewModel> doInBackground(String... params) {

            HttpURLConnection connection = null;

            try {
                //Coneccion
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                //Extraer archivo plano
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                //Leer archivo plano
                String line = "";
                StringBuffer stringBuffer = new StringBuffer();
                while((line = reader.readLine()) != null){
                    stringBuffer.append(line);
                }
                //Convirtiendo archivo plano a JSON
                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("user");

                //Extrayendo datos del Documento JSON
                List<UserViewModel> modelList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    UserViewModel model = new UserViewModel();

                    model.setId(finalObject.getInt("id"));
                    model.setName(finalObject.getString("name"));
                    model.setPassword(finalObject.getString("password"));
                    model.setAlias(finalObject.getString("aias"));
                    model.setLatitud((float) finalObject.getDouble("latitud"));
                    model.setLongitud((float) finalObject.getDouble("longitud"));

                    //Agregando datos a la lista del modelo
                    modelList.add(model);
                }

                return modelList;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<UserViewModel> userViewModels) {
            super.onPostExecute(userViewModels);
            try {
                helper.addUser(userViewModels);
            } catch (Exception e){
                e.printStackTrace();
            }
            dialog.dismiss();
            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
            intent.putExtra("nombre",etUserName.getText().toString());
            startActivity(intent);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String userName,passUser;
                userName = etUserName.getText().toString();
                passUser = etPassword.getText().toString();
                if (userName.equals("") || passUser.equals("")){
                    Toast.makeText(this,"Los campos son requeridos",Toast.LENGTH_LONG).show();
                }else{
                    Boolean validUser = false;
                    try {
                        validUser = helper.validUser(userName,passUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (validUser){
                        Intent intent = new Intent(this,MenuActivity.class);
                        intent.putExtra("nombre",userName);
                        startActivity(intent);
                        finish();
                    }else{
                        new JsonLogin().execute("http://www.mocky.io/v2/5650cd44110000303aac9c8f");
                    }
                }
                break;
        }
    }

}
