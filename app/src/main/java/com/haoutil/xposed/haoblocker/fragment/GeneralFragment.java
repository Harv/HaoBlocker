package com.haoutil.xposed.haoblocker.fragment;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.haoutil.xposed.haoblocker.R;
import com.haoutil.xposed.haoblocker.util.SettingsHelper;

public class GeneralFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private SettingsHelper settingsHelper;

    private Switch sw_enable;
    private Switch sw_sms_enable;
    private Switch sw_call_enable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsHelper = new SettingsHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);

        try {
            String versionName = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;

            TextView tv_about = (TextView) view.findViewById(R.id.tv_about);
            tv_about.setText(tv_about.getText() + " v" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }

        sw_enable = (Switch) view.findViewById(R.id.sw_enable);
        sw_enable.setChecked(settingsHelper.isEnable());
        sw_enable.setOnCheckedChangeListener(this);

        sw_sms_enable = (Switch) view.findViewById(R.id.sw_sms_enable);
        sw_sms_enable.setEnabled(sw_enable.isChecked());
        sw_sms_enable.setChecked(settingsHelper.isEnableSMS());
        sw_sms_enable.setOnCheckedChangeListener(this);

        sw_call_enable = (Switch) view.findViewById(R.id.sw_call_enable);
        sw_call_enable.setEnabled(sw_enable.isChecked());
        sw_call_enable.setChecked(settingsHelper.isEnableCall());
        sw_call_enable.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean b) {
        switch (v.getId()) {
            case R.id.sw_enable:
                sw_sms_enable.setEnabled(b);
                sw_call_enable.setEnabled(b);

                settingsHelper.setEnable(b);
                break;
            case R.id.sw_sms_enable:
                settingsHelper.setEnableSMS(b);
                break;
            case R.id.sw_call_enable:
                settingsHelper.setEnableCall(b);
                break;
        }
    }
}
