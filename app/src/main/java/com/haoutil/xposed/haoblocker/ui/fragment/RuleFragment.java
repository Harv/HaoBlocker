package com.haoutil.xposed.haoblocker.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.haoutil.xposed.haoblocker.R;
import com.haoutil.xposed.haoblocker.model.entity.Rule;
import com.haoutil.xposed.haoblocker.presenter.RulePresenter;
import com.haoutil.xposed.haoblocker.presenter.RulePresenterImpl;
import com.haoutil.xposed.haoblocker.ui.RuleView;
import com.haoutil.xposed.haoblocker.ui.activity.RuleActivity;
import com.haoutil.xposed.haoblocker.ui.activity.SettingsActivity;
import com.haoutil.xposed.haoblocker.ui.adapter.BaseRecycleAdapter;
import com.haoutil.xposed.haoblocker.util.BlockerManager;

public class RuleFragment extends BaseFragment implements RuleView, BaseRecycleAdapter.OnItemClick, View.OnClickListener, DialogInterface.OnClickListener, SettingsActivity.OnAddListener, SettingsActivity.OnMenuItemClickListener {
    private RulePresenter mRulePresenter;
    private RecyclerView rv_rule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRulePresenter = new RulePresenterImpl(this);
        mRulePresenter.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            rv_rule = (RecyclerView) view.findViewById(R.id.rv_rule);
            rv_rule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRulePresenter.setListItems(BlockerManager.TYPE_ALL);
        }
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mRulePresenter.setMenuItems(menu);
    }

    // click on list item
    @Override
    public void onClick(int position) {
        mRulePresenter.modifyRule(position);
    }

    // long click on list item
    @Override
    public void onLongClick(int position) {
        mRulePresenter.deleteRuleConfirm(position);
    }

    @Override
    public void onClick(View v) {
        mRulePresenter.restoreRule();
    }

    // click on dialog button
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mRulePresenter.deleteRule();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mRulePresenter.deleteRuleCancel();
                break;
        }
    }

    // click on Snackbar button
    @Override
    public void onAdd() {
        mRulePresenter.addRule();
    }

    // click on menu item
    @Override
    public void onFilter(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_all:
                mRulePresenter.setListItems(BlockerManager.TYPE_ALL);
                break;
            case R.id.filter_call:
                mRulePresenter.setListItems(BlockerManager.TYPE_CALL);
                break;
            case R.id.filter_sms:
                mRulePresenter.setListItems(BlockerManager.TYPE_SMS);
                break;
            case R.id.filter_except:
                mRulePresenter.setListItems(BlockerManager.TYPE_EXCEPT);
                break;
        }
    }

    @Override
    public void onExport(MenuItem item) {
        mRulePresenter.exportRules();
    }

    @Override
    public void onImport(MenuItem item) {
        mRulePresenter.importRules();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Rule rule = (Rule) bundle.get("rule");
            int position = bundle.getInt("position");
            mRulePresenter.addOrUpdateRuleSuccess(position, rule);
        }
    }

    // toggle FloatingActionButton
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mRulePresenter != null) {
            mRulePresenter.toggleAddButton(isVisibleToUser);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_rule;
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void setRuleAdapter(RecyclerView.Adapter adapter) {
        rv_rule.setAdapter(adapter);
    }

    @Override
    public void setMenuItems(Menu menu) {
        menu.findItem(R.id.filter).setVisible(true);
        menu.findItem(R.id.export).setVisible(true);
        menu.findItem(R.id.import0).setVisible(true);
        ((SettingsActivity) getActivity()).setOnMenuItemClickListener(this);
    }

    @Override
    public BaseRecycleAdapter.OnItemClick getOnItemClick() {
        return this;
    }

    @Override
    public void addRule() {
        Intent intent = new Intent(getActivity(), RuleActivity.class);
        intent.putExtra("operation", "add");
        startActivityForResult(intent, 0);
    }

    @Override
    public void modifyRule(int position, Rule rule) {
        Intent intent = new Intent(getActivity(), RuleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("operation", "modify");
        bundle.putInt("position", position);
        bundle.putSerializable("rule", rule);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    public void toggleAddButton(boolean visible) {
        ((SettingsActivity) getActivity()).setOnAddListener(visible ? this : null);
    }

    @Override
    public void showTip(int resId) {
        ((SettingsActivity) getActivity()).showTip(resId, null);
    }

    @Override
    public void showTipInThread(int resId) {
        ((SettingsActivity) getActivity()).showTipInThread(resId);
    }

    @Override
    public void confirm() {
        confirm(this, this);
    }
}
