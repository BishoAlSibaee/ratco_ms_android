package com.syrsoft.ratcoms;

public class Permission {

//    static String[] PermissionEnglishName = new String[] {"Manage Authorized JobTitles","Manage Orders Authorities","Exit Request","Vacations","Back To Work","Resignation"
//    ,"Advance Payment","Request Custody","Request Vacation Salary","Manage Employees Contracts","Manage Employees Passports","Manage Employees IDs","Send New Ad"
//    ,"Send New Ad With Image","Salary Report","My Orders","My Approvals","Check Employees Attendance","Rating Employees","Add Client Visit Report","My Visit Reports","My Staff Visit Reports"
//    ,"Add Client","My Clients","Add Project Contract","My Project Contracts","Add Maintenance Order","My Maintenance Orders","Add Site Visit Order","My Site Visit Orders","Site Visit Orders"
//    ,"Add Local Purchase Order"} ;
//    static String[] PermissionArabicName = new String[] {"ضبط المسميات الوظيفية ذات الصلاحية","ضبط صلاحيات طلبات الموارد البشرية","طلب خروج","اجازات","العودة للعمل","استقالة"
//            ,"سلفة","طلب عهدة","طلب راتب اجازة","ضبط عقود الموظفين","ضبط جوازات الموظفين","ضبط اقامات الموظفين","ارسال اعلان نصي"
//            ,"ارسال اعلان مع صورة","تقارير الرواتب","طلباتي","موافقاتي","مراجعة دوام الموظفين","تقييم الموظفين","اضافة تقرير زيارة عميل","تقارير زيارتي للعملاء","تقارير زيارة العملاء لفريقي"
//            ,"اضافة عميل","عملائي","اضافة عقد مشروع","عقودي","اضافة طلب صيانة","طلباتي للصيانة","اضافة طلب زيارة موقع","طلباتي لزيارة موقع","طلبات زيارة موقع"
//            ,"اضافة طلب شراء محلي"} ;
    private int id ;
    private String PermissionEnName ;
    private String PermissionArName ;
    private String Department ;
    private int Value ;
    private Boolean Result ;

    public Permission(int id, String permissionEnName, String permissionArName, String department) {
        this.id = id;
        PermissionEnName = permissionEnName;
        PermissionArName = permissionArName;
        Department = department;
    }

    public int getId() {
        return id;
    }

    public String getPermissionEnName() {
        return PermissionEnName;
    }

    public String getPermissionArName() {
        return PermissionArName;
    }

    public String getDepartment() {
        return Department;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setPermissionEnName(String permissionEnName) {
        PermissionEnName = permissionEnName;
    }

    public void setPermissionArName(String permissionArName) {
        PermissionArName = permissionArName;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setValue(int value) {
        Value = value;
    }

    public void setResult() {

        if (this.Value == 1) {
            Result = true ;
        }
        else if (this.Value == 0) {
            Result = false ;
        }
    }

    public int getValue() {
        return Value;
    }

    public Boolean getResult() {
        return Result;
    }
}
