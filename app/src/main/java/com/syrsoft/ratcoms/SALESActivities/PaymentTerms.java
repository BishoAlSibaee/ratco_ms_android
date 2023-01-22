package com.syrsoft.ratcoms.SALESActivities;

public class PaymentTerms {
    public String Percent ;
    public String Condition ;

    public PaymentTerms(String percent, String condition) {
        Percent = percent;
        Condition = condition;
    }

    public void setPercent(String percent) {
        Percent = percent;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getPercent() {
        return Percent;
    }

    public String getCondition() {
        return Condition;
    }
}
