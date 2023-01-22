package com.syrsoft.ratcoms.HRActivities;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class JobTitle {
    public int id ;
    public String JobTitle ;
    public int Department ;
    public String ArabicName ;

    public JobTitle(int id, String jobTitle, int department, String arabicName) {
        this.id = id;
        JobTitle = jobTitle;
        Department = department;
        ArabicName = arabicName;
    }

    public static JobTitle searchJobTitleById(List<JobTitle> list , int id) {
        JobTitle j = null ;

        for (int i =0;i<list.size();i++) {
            if (list.get(i).id == id) {
                j = list.get(i) ;
            }
        }
        return j ;
    }

    public static USER getBonusApproveUser(String jt , USER u) {
        USER res = null ;
        if (jt.equals("Direct Manager")) {
            res = USER.searchUserByJobNumber(MyApp.EMPS,u.DirectManager);
        }
        else if (jt.equals("Department Manager")) {
            res = USER.searchUserByJobNumber(MyApp.EMPS,u.DepartmentManager);
        }
        else {
            USER.searchUserByJobtitle(MyApp.EMPS,jt);
        }
        return res ;
    }
}
