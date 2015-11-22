package com.example.cristian.loginjsonandmore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cristian.loginjsonandmore.DataBaseHelper.DBHelper;
import com.example.cristian.loginjsonandmore.ViewModels.UserViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    TextView tvWelcome,tvAlias;
    DBHelper helper;

    MapView mapView;
    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        helper = new DBHelper(this);
        tvWelcome = (TextView) findViewById(R.id.tvBienvenido);
        tvAlias = (TextView) findViewById(R.id.tvAlias);

        mapView = (MapView) findViewById(R.id.mapa);
        mapView.onCreate(savedInstanceState);
        mapView.setClickable(true);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("nombre");
        welcome(data);
    }

    private void welcome(String data) {
        List<UserViewModel> modelList = helper.getUser(data);
        StringBuffer stringBuffer1 = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i = 0; i< modelList.size(); i++){
            stringBuffer1.append(modelList.get(i).getName()+ " " + modelList.get(i).getLatitud());
            stringBuffer2.append(modelList.get(i).getAlias()+ " " + modelList.get(i).getLongitud());
        }
        tvWelcome.setText(stringBuffer1.toString());
        tvAlias.setText(stringBuffer2.toString());

    }

    private void mostarmapa() {
        try{
            MapsInitializer.initialize(getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }

        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(4.797979,-7.4545))
                .title("Site")
                .snippet("Street");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(markerOptions);
    }

}
