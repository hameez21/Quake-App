package com.example.android.quakereport;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EarthquakeDetails extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.design_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        String title = getIntent().getStringExtra("title");
        CollapsingToolbarLayout CT = findViewById(R.id.collapseToolbar);
        CT.setExpandedTitleTextAppearance(R.style.title);

        //TextView tvTitle = findViewById(R.id.title);
        //tvTitle.setText(title);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final ProgressBar magBar = findViewById(R.id.magBar);
        progressAnim(magBar, 8);

    }

    public void onMapReady(GoogleMap map) {
        double lat = getIntent().getDoubleExtra("lat", 0);
        double lng = getIntent().getDoubleExtra("lng", 0);
        LatLng s = new LatLng(lat, lng);

        map.addMarker(new MarkerOptions().position(s));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(s, 7));

    }


    public void progressAnim(final ProgressBar progressBar, int progress) {
        final ObjectAnimator progressAnim = ObjectAnimator.ofInt(progressBar, "progress", 0, progress * 10);
        progressAnim.setDuration(5000);
        progressAnim.setInterpolator(new AnticipateOvershootInterpolator());
        progressAnim.start();
        progressAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                int color = (int) animation.getAnimatedValue();
                switch (color / 10) {
                    case 1:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude1), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 2:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude2), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 3:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude3), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 4:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude4), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 5:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude5), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 6:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude6), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 7:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude7), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 8:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude8), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 9:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude9), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    case 10:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude10plus), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;
                    default:
                        progressDrawable.setColorFilter(getResources().getColor(R.color.magnitude3), PorterDuff.Mode.SRC_IN);
                        progressBar.setProgressDrawable(progressDrawable);
                        break;

                }
            }
        });
    }
}

