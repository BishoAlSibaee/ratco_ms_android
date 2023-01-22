package com.syrsoft.ratcoms.HRActivities;

public class RatingCriteria {
    private int id ;
    private String Arabic ;
    private String English ;

    public RatingCriteria(int id, String arabic, String english) {
        this.id = id;
        Arabic = arabic;
        English = english;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArabic(String arabic) {
        Arabic = arabic;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public int getId() {
        return id;
    }

    public String getArabic() {
        return Arabic;
    }

    public String getEnglish() {
        return English;
    }
}
