package com.syrsoft.ratcoms.Interfaces;

import com.syrsoft.ratcoms.AttendLeaveTime;

public interface AttendTimeCallback {

    void onSuccess(AttendLeaveTime al);
    void onFail(String error);
}
