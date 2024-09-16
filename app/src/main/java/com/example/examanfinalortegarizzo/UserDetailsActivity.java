package com.example.examanfinalortegarizzo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class UserDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private User user;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            ((TextView) findViewById(R.id.nameTextView)).setText(user.name.first + " " + user.name.last);
            ((TextView) findViewById(R.id.emailTextView)).setText(user.email);
            ((TextView) findViewById(R.id.phoneTextView)).setText(user.phone);
            ((TextView) findViewById(R.id.addressTextView)).setText(
                    user.location.street.number + " " + user.location.street.name + ", " +
                            user.location.city + ", " + user.location.state + ", " + user.location.country);

            Glide.with(this)
                    .load(user.picture.large)
                    .circleCrop()
                    .into((ImageView) findViewById(R.id.userImageView));

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapFragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            fetchCountryFlag(user.location.country);
        } else {
            Toast.makeText(this, "Error al cargar los datos del usuario.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (user != null && user.location != null && user.location.coordinates != null) {
            try {
                LatLng location = new LatLng(
                        Double.parseDouble(user.location.coordinates.latitude),
                        Double.parseDouble(user.location.coordinates.longitude)
                );
                map.addMarker(new MarkerOptions().position(location).title(user.location.city));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Error al cargar la ubicación del usuario.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se pudo cargar la ubicación del usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCountryFlag(String country) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/v3.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CountryApi api = retrofit.create(CountryApi.class);

        api.getCountryInfo(country).enqueue(new Callback<List<CountryInfo>>() {
            @Override
            public void onResponse(Call<List<CountryInfo>> call, Response<List<CountryInfo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    String flagUrl = response.body().get(0).flags.png;
                    Glide.with(UserDetailsActivity.this)
                            .load(flagUrl)
                            .into((ImageView) findViewById(R.id.flagImageView));
                } else {
                    Toast.makeText(UserDetailsActivity.this, "No se pudo cargar la bandera del país.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CountryInfo>> call, Throwable t) {
                Toast.makeText(UserDetailsActivity.this, "Error al cargar la bandera del país.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

interface CountryApi {
    @GET("name/{country}")
    Call<List<CountryInfo>> getCountryInfo(@Path("country") String country);
}

class CountryInfo {
    Flags flags;
}

class Flags {
    String png;
}