package com.haoutil.xposed.haoblocker.model;

import com.haoutil.xposed.haoblocker.model.entity.Call;

import java.util.List;

public interface CallModel {
    void readAllCall();

    List<Call> getCalls(long id);

    long saveCall(Call call);

    void deleteCall(Call call);

    long restoreCall(Call call);
}
