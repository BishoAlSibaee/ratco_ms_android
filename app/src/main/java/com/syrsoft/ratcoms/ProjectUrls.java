package com.syrsoft.ratcoms;

public class ProjectUrls {

    public static String MainUrl = "https://ratco-solutions.com/RatcoManagementSystem/";
    public static String MainUrlNew = "https://ratco-solutions.com/RatcoManagementSystem/NewOptions/";

    // User Urls

    public static String getIsEmployeeAttendToday = MainUrlNew + "getIfEmployeeAttendToday.php";
    public static String getIsEmployeeAttendYesterday = MainUrlNew + "getIfEmployeeAttendYesterday.php";
    public static String getIsEmployeeAttendNow = MainUrlNew + "getIsEmployeeAttendNow.php";
    public static String getIsEmployeeHasVacationToday = MainUrlNew + "getIsEmployeeHasVacationToday.php";
    public static String getIsEmployeeHasVacationYesterday = MainUrlNew + "getIsEmployeeHasVacationYesterday.php";
    public static String setMyVacationStatusUrl = MyApp.MainUrl + "setIamInVacation.php" ;
    public static String getMyVacationsUrl = MyApp.MainUrl + "getMyVacations.php" ;
    public static String getTasksUrl = MyApp.MainUrl+"getDailyJobs.php";
    public static String setTokenUrl = MyApp.MainUrl+"setUserToken.php";
    public static String registerWorkTimeUrl = MyApp.MainUrl+"attendRecordInsert.php";
    public static String checkRatingAvailability = MyApp.MainUrl+"checkAvailableRatings.php";
    public static String getClientUrl = MyApp.MainUrl+"getClient.php";
    public static String getLastAdsUrl = MyApp.MainUrl+"getLast5Ads.php";
    public static String LoginUrl = MyApp.MainUrl + "appLoginEmployees.php" ;
    public static String updateMyDataUrl = MyApp.MainUrl+"getDirectManager.php";
    public static String getUserAttendTimeUrl = MainUrlNew + "getUserAttendTime.php";
}
