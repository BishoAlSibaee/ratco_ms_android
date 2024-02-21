package com.syrsoft.ratcoms.Interfaces;

import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.HRActivities.Vacation;

public interface UserVacationTodayCallback {
    void onSuccess(boolean result, VACATION_CLASS vacation);
    void onFil(String error);
}
