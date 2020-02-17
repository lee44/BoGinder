package com.apps.jlee.boginder.Activities;

import android.os.Bundle;

import com.apps.jlee.boginder.Fragments.ProfilePreviewFragment;
import com.apps.jlee.boginder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilePreviewActivity extends AppCompatActivity
{
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_preview);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ProfilePreviewFragment profilePreviewFragment = new ProfilePreviewFragment(this);

        if(getIntent().getExtras() != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("user_id", getIntent().getExtras().getString("user_id"));
            profilePreviewFragment.setArguments(bundle);
        }

        setFragment(profilePreviewFragment);
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profile_preview_main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
