package com.haoutil.xposed.haoblocker.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.haoutil.xposed.haoblocker.R;
import com.haoutil.xposed.haoblocker.adapter.CallAdaptor;
import com.haoutil.xposed.haoblocker.event.CallUpdateEvent;
import com.haoutil.xposed.haoblocker.model.Call;
import com.haoutil.xposed.haoblocker.util.DbManager;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class CallFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private DbManager dbManager;

    private CallAdaptor adapter;

    @InjectView(R.id.cb_check_all)
    CheckBox cb_check_all;
    @InjectView(R.id.srl_rules)
    SwipeRefreshLayout srl_rules;
    @InjectView(R.id.lv_rules)
    ListView lv_rules;

    private boolean showDiscardAction = false;

    private MenuItem action_discard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dbManager = new DbManager(getActivity());
        dbManager.readAllCall();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_actions, menu);

        action_discard = menu.findItem(R.id.action_discard);
        action_discard.setVisible(showDiscardAction);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discard:
                this.confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbManager.deleteCall(adapter.getCheckedCalls());

                        adapter.clearChecked();
                        adapter.notifyDataSetChanged();
                    }
                }, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = getView();

        srl_rules.setOnRefreshListener(this);
        setColorSchemeResources(srl_rules);

        adapter = new CallAdaptor(getActivity().getLayoutInflater(), dbManager.getCalls(-1));
        lv_rules.setAdapter(adapter);

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_call;
    }

    @OnClick(R.id.cb_check_all)
    public void onClick(View view) {
        switch (view.getId()) {
            // do not use setOnCheckedChangeListener, because it will trigger checkAll method of RuleAdapter
            case R.id.cb_check_all:
                boolean b = ((CheckBox) view).isChecked();

                adapter.checkAll(b);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @OnItemClick(R.id.lv_rules)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Call call = (Call) adapter.getItem(position);
//
//        Intent intent = new Intent(getActivity(), RuleActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("sms", sms);
//        intent.putExtras(bundle);
//
//        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        srl_rules.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                List<Call> list = dbManager.getCalls(adapter.getCount() > 0 ? ((Call) adapter.getItem(0)).getId() : -1);
                if (list != null && list.size() > 0) {
                    for (int i = list.size() - 1; i >= 0; i--) {
                        adapter.addItem(list.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }

                srl_rules.setRefreshing(false);
                srl_rules.setEnabled(true);
            }
        }, 1500);
    }

    @Override
    public void onResetActionBarButtons(boolean isMenuOpen) {
        if (action_discard == null) {
            return;
        }

        if (isMenuOpen) {
            action_discard.setVisible(false);
        } else {
            action_discard.setVisible(showDiscardAction);
        }
    }

    public void onEvent(CallUpdateEvent event) {
        switch (event.getWhat()) {
            case 0:
                showDiscardAction = false;
                getActivity().invalidateOptionsMenu();
                break;
            case 1:
                showDiscardAction = true;
                getActivity().invalidateOptionsMenu();
                break;
            case 2:
                cb_check_all.setChecked(false);
                break;
            case 3:
                cb_check_all.setChecked(true);
                break;
        }
    }
}
