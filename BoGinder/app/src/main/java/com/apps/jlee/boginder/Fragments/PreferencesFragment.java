package com.apps.jlee.boginder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apps.jlee.boginder.R;
import com.edmodo.rangebar.RangeBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreferencesFragment extends Fragment
{
    @BindView(R.id.age_rangeBar)
    RangeBar ageBar;
    @BindView(R.id.height_rangeBar)
    RangeBar heightBar;
    @BindView(R.id.distance_seekBar)
    SeekBar distanceBar;
    @BindView(R.id.age_range_tv)
    TextView age_range_tv;
    @BindView(R.id.height_range_tv)
    TextView height_range_tv;
    @BindView(R.id.distance_range_tv)
    TextView distance_range_tv;
    @BindView(R.id.gender_radio_group)
    RadioGroup radioGroup;

    private DatabaseReference databaseReference;
    final int startingHeight = 48;
    private int startingInches, endingInches, startingFeet, endingFeet;
    private String user_id;

    public PreferencesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);

        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users/"+user_id);

        ButterKnife.bind(this,view);

        ageBar.setTickCount(33);
        ageBar.setTickHeight(0);
        ageBar.setBarWeight(6);

        heightBar.setTickCount(37);
        heightBar.setTickHeight(0);
        heightBar.setBarWeight(6);

        ageBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener()
        {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1)
            {
                age_range_tv.setText((i+18)+"-"+(i1+18));
            }
        });

        heightBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener()
        {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1)
            {
                startingInches = startingHeight + i;
                endingInches = startingHeight + i1;

                startingFeet = startingInches/12;
                endingFeet = endingInches/12;

                startingInches = startingInches % 12;
                endingInches = endingInches % 12;

                String height = startingFeet+"'"+startingInches+'"'+"-"+endingFeet+"'"+endingInches+'"';
                height_range_tv.setText(height);
            }
        });

        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                distance_range_tv.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {}
        });

        return view;
    }

    private void getUserInfo()
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();

//                    if(map.get("Gender") != null)
//                        ((RadioButton)radioGroup.findViewById(map.get("Gender").toString().equals("Male") ? R.id.male_radio_button : R.id.female_radio_button)).setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){}
        });
    }

}
