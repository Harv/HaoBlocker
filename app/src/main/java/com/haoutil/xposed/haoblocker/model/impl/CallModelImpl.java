package com.haoutil.xposed.haoblocker.model.impl;

import android.content.Context;

import com.haoutil.xposed.haoblocker.AppContext;
import com.haoutil.xposed.haoblocker.model.CallModel;
import com.haoutil.xposed.haoblocker.model.entity.Call;
import com.haoutil.xposed.haoblocker.util.BlockerManager;

import java.util.List;

public class CallModelImpl implements CallModel {
    private BlockerManager mBlockerManager;

    public CallModelImpl() {
        Context context = AppContext.getsInstance().getApplicationContext();
        mBlockerManager = new BlockerManager(context);
    }

    @Override
    public void readAllCall() {
        mBlockerManager.readAllCall();
    }

    @Override
    public List<Call> getCalls(long id) {
        return mBlockerManager.getCalls(id);
    }

    @Override
    public long saveCall(Call call) {
        return mBlockerManager.hasCall(call) ? -1 : mBlockerManager.saveCall(call);
    }

    @Override
    public void deleteCall(Call call) {
        mBlockerManager.deleteCall(call);
    }

    @Override
    public long restoreCall(Call call) {
        return mBlockerManager.restoreCall(call);
    }
}
