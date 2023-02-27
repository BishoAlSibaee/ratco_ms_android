package com.syrsoft.ratcoms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class USERDataBase extends SQLiteOpenHelper {

    private static final int DBVersion = 1;
    private static String DBName = "USER";
    SQLiteDatabase db;

    public USERDataBase(Context context) {
        super(context, DBName, null, DBVersion);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User ( 'id' INTEGER ,'JobNumber' INTEGER,'User' VARCHAR ,'FirstName' VARCHAR ,'LastName' VARCHAR ,'Department' VARCHAR , 'JobTitle' VARCHAR , 'DirectManager' INTEGER,'DepartmentManager' INTEGER,'WorkLocationLa' DOUBLE ,'WorkLocationLo' DOUBLE,'Mobile' VARCHAR,'Email' VARCHAR , 'Pic' TEXT ,'IDNumber' VARCHAR , 'IDExpireDate' DATE,'BirthDate' DATE,'Nationality'VARCHAR,'PassportNumber'VARCHAR,'PassportExpireDate'DATE,'ContractNumber'VARCHAR,'ContractStartDate'DATE,'ContractDuration'INTEGER,'ContractExpireDate' DATE,'InsuranceExpireDate' DATE ,'Bank' VARCHAR,'BankAccount' VARCHAR,'BankIban' VARCHAR,'IDsWarningNotifications' INTEGER,'PASSPORTsWarningNotification' INTEGER,'CONTRACTsWarningNotification' INTEGER,'Salary' DOUBLE,'VacationDays' DOUBLE,'SickDays' INTEGER,'EmergencyDays' INTEGER,'VacationStatus' INTEGER ,'VacationAlternative' INTEGER,'JoinDate' DATE,'AtWork' INTEGER) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS Counters ('ProjectsCounter' INTEGER , 'MaintenanceOrdersCounter' INTEGER , 'SalesCounter' INTEGER , 'HRCounter' INTEGER,'VacationCounter' INTEGER,'VacationSalaryCounter' INTEGER,'ResignationCounter' INTEGER,'BacksCounter' INTEGER,'CustudyCounter' INTEGER,'AdvancePaymentCounter'INTEGER) ");
        Log.d("countersTable", " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void logOut() {
        db.execSQL("DROP TABLE IF EXISTS 'User'");
        onCreate(db);
    }

    public void createCountersTble() {
        db.execSQL("CREATE TABLE IF NOT EXISTS Counters ('ProjectsCounter' INTEGER , 'MaintenanceOrdersCounter' INTEGER , 'SalesCounter' INTEGER , 'HRCounter' INTEGER,'VacationCounter' INTEGER,'VacationSalaryCounter' INTEGER,'ResignationCounter' INTEGER,'BacksCounter' INTEGER,'CustudyCounter' INTEGER,'AdvancePaymentCounter'INTEGER) ");
        insertCounters();
    }

    public boolean insertUser(int DBid, int jn, String User, String FirstName, String LastName, String Department, String JobTitle, int DirectManager, int departmentManager, double WLla, double WLlo, String Mobile, String Email, String Pic, String IDNumber, String IDExpireDate, String BirthDate, String Nationality, String PassportNumber, String PassportExpireDate, String ContractNumber, String ContractStartDate, int ContractDuration, String ContractExpireDate, String insurance, String Bank, String bankAccount, String BankIban, int IDsWarningNotifications, int PASSPORTsWarningNotification, int CONTRACTsWarningNotification, double Salary, double VacationDays, int SickDays, int EmergencyDays, int VacationStatus, int VacationAlternative, String JoinDate) {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("id", DBid);
        values.put("JobNumber", jn);
        values.put("User", User);
        values.put("FirstName", FirstName);
        values.put("LastName", LastName);
        values.put("Department", Department);
        values.put("JobTitle", JobTitle);
        values.put("DirectManager", DirectManager);
        values.put("DepartmentManager", departmentManager);
        values.put("WorkLocationLa", WLla);
        values.put("WorkLocationLo", WLlo);
        values.put("Mobile", Mobile);
        values.put("Email", Email);
        values.put("Pic", Pic);
        values.put("IDNumber", IDNumber);
        values.put("IDExpireDate", IDExpireDate);
        values.put("BirthDate", BirthDate);
        values.put("Nationality", Nationality);
        values.put("PassportNumber", PassportNumber);
        values.put("PassportExpireDate", PassportExpireDate);
        values.put("ContractNumber", ContractNumber);
        values.put("ContractStartDate", ContractStartDate);
        values.put("ContractDuration", ContractDuration);
        values.put("ContractExpireDate", ContractExpireDate);
        values.put("InsuranceExpireDate", insurance);
        values.put("Bank", Bank);
        values.put("BankAccount", bankAccount);
        values.put("BankIban", BankIban);
        values.put("IDsWarningNotifications", IDsWarningNotifications);
        values.put("PASSPORTsWarningNotification", PASSPORTsWarningNotification);
        values.put("CONTRACTsWarningNotification", CONTRACTsWarningNotification);
        values.put("Salary", Salary);
        values.put("VacationDays", VacationDays);
        values.put("SickDays", SickDays);
        values.put("EmergencyDays", EmergencyDays);
        values.put("VacationStatus", VacationStatus);
        values.put("VacationAlternative", VacationAlternative);
        values.put("JoinDate", JoinDate);
        values.put("AtWork", 0);
        try {
            long resulty = db.insert("User", null, values);
            result = true;
            Log.d("loginRes", resulty + " ");
        } catch (Exception e) {
            result = false;
            Log.d("loginRes", e.getMessage());
        }

        return result;
    }

    boolean isLoggedIn() {
        boolean result = false;
        Cursor c = db.query("User", new String[]{"id"}, "", null, null, null, null);
        if (c.getCount() == 1) {
            result = true;
        } else {
            result = false;
        }
        c.close();
        return result;
    }

    boolean isCountersLoggedIn() {
        boolean result = false;
        Cursor c = db.query("Counters", new String[]{"ProjectsCounter"}, "", null, null, null, null);
        if (c.getCount() == 1) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public USER getUser() {
        int id;
        int jn;
        String User;
        String FirstName;
        String LastName;
        String Department;
        String JobTitle;
        int DirectManager;
        int departmentManager;
        double WLla;
        double WLlo;
        String Mobile;
        String Email;
        String Pic;
        String IDNumber;
        String IDExpireDate;
        String BirthDate;
        String Nationality;
        String PassportNumber;
        String PassportExpireDate;
        String ContractNumber;
        String ContractStartDate;
        int ContractDuration;
        String ContractExpireDate;
        String insur;
        String bank;
        String bankacc;
        String bankiban;
        int idsW;
        int passW;
        int conW;
        double Salary;
        double VacationDays;
        int SickDays;
        int EmergencyDays;
        int vacationStatus = 0;
        int vacationAlternative = 0;
        String JoinDate = "";

        USER u = null;
        Cursor c = db.rawQuery("SELECT * FROM 'User' ; ", null);
        if (c.moveToFirst()) {
            id = c.getInt(0);
            jn = c.getInt(1);
            User = c.getString(2);
            FirstName = c.getString(3);
            LastName = c.getString(4);
            Department = c.getString(5);
            JobTitle = c.getString(6);
            DirectManager = c.getInt(7);
            departmentManager = c.getInt(8);
            WLla = c.getDouble(9);
            WLlo = c.getDouble(10);
            Mobile = c.getString(11);
            Email = c.getString(12);
            Pic = c.getString(13);
            IDNumber = c.getString(14);
            IDExpireDate = c.getString(15);
            BirthDate = c.getString(16);
            Nationality = c.getString(17);
            PassportNumber = c.getString(18);
            PassportExpireDate = c.getString(19);
            ContractNumber = c.getString(20);
            ContractStartDate = c.getString(21);
            ContractDuration = c.getInt(22);
            ContractExpireDate = c.getString(23);
            insur = c.getString(24);
            bank = c.getString(25);
            bankacc = c.getString(26);
            bankiban = c.getString(27);
            idsW = c.getInt(28);
            passW = c.getInt(29);
            conW = c.getInt(30);
            Salary = c.getDouble(31);
            VacationDays = c.getDouble(32);
            SickDays = c.getInt(33);
            EmergencyDays = c.getInt(34);
            vacationStatus = c.getInt(35);
            if (c.getColumnCount() > 36) {
                if (c.getColumnName(36).equals("VacationAlternative")) {
                    vacationAlternative = c.getInt(36);
                    JoinDate = c.getString(37);
                } else {
                    vacationAlternative = 0;
                    if (c.getColumnName(36).equals("JoinDate")) {
                        JoinDate = c.getString(36);
                    }
                }
            }
            u = new USER(id, jn, User, FirstName, LastName, Department, JobTitle, DirectManager, departmentManager, WLla, WLlo, Mobile, Email, Pic, IDNumber, IDExpireDate, BirthDate, Nationality, PassportNumber, PassportExpireDate, ContractNumber, ContractStartDate, ContractDuration, ContractExpireDate, insur, bank, bankacc, bankiban, idsW, passW, conW, Salary, VacationDays, SickDays, EmergencyDays, vacationStatus, vacationAlternative, JoinDate, "");
        }
        c.close();
        return u;
    }

    public boolean updateWorkLocaion(double WLla, double WLlo) {
        boolean status = false;
        if (isLoggedIn()) {
            ContentValues cv = new ContentValues();
            cv.put("WorkLocationLa", WLla);
            cv.put("WorkLocationLo", WLlo);
            int x = db.update("User", cv, "", null);
            Log.d("worklocation", x + "");
            if (x == 1) {
                status = true;
            }
        }
        return status;
    }

    public boolean updateUser(int DBid, int jn, String User, String FirstName, String LastName, String Department, String JobTitle, int DirectManager, double WLla, double WLlo, String Mobile, String Email, String Pic, String IDNumber, String IDExpireDate, String BirthDate, String Nationality, String PassportNumber, String PassportExpireDate, String ContractNumber, String ContractStartDate, int ContractDuration, String ContractExpireDate, String insurance, String Bank, String bankAccount, String BankIban, int IDsWarningNotifications, int PASSPORTsWarningNotification, int CONTRACTsWarningNotification, double Salary, double VacationDays, int SickDays, int EmergencyDays, String JoinDate) {
        boolean status = false;
        if (isLoggedIn()) {
            ContentValues values = new ContentValues();
            values.put("id", DBid);
            values.put("JobNumber", jn);
            values.put("User", User);
            values.put("FirstName", FirstName);
            values.put("LastName", LastName);
            values.put("Department", Department);
            values.put("JobTitle", JobTitle);
            values.put("DirectManager", DirectManager);
            values.put("WorkLocationLa", WLla);
            values.put("WorkLocationLo", WLlo);
            values.put("Mobile", Mobile);
            values.put("Email", Email);
            values.put("Pic", Pic);
            values.put("IDNumber", IDNumber);
            values.put("IDExpireDate", IDExpireDate);
            values.put("BirthDate", BirthDate);
            values.put("Nationality", Nationality);
            values.put("PassportNumber", PassportNumber);
            values.put("PassportExpireDate", PassportExpireDate);
            values.put("ContractNumber", ContractNumber);
            values.put("ContractStartDate", ContractStartDate);
            values.put("ContractDuration", ContractDuration);
            values.put("ContractExpireDate", ContractExpireDate);
            values.put("InsuranceExpireDate", insurance);
            values.put("Bank", Bank);
            values.put("BankAccount", bankAccount);
            values.put("BankIban", BankIban);
            values.put("IDsWarningNotifications", IDsWarningNotifications);
            values.put("PASSPORTsWarningNotification", PASSPORTsWarningNotification);
            values.put("CONTRACTsWarningNotification", CONTRACTsWarningNotification);
            values.put("Salary", Salary);
            values.put("VacationDays", VacationDays);
            values.put("SickDays", SickDays);
            values.put("EmergencyDays", EmergencyDays);
            values.put("JoinDate", JoinDate);
            int x = db.update("User", values, "", null);
            Log.d("worklocation", x + "");
            if (x == 1) {
                status = true;
            }
        }
        return status;
    }

    int getAtWork() {
        int result = 0;
        Cursor c = db.query("User", new String[]{"AtWork"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    boolean setAtWork(int atwork) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("AtWork", atwork);
        int x = db.update("User", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public boolean insertCounters() {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("ProjectsCounter", 0);
        values.put("MaintenanceOrdersCounter", 0);
        values.put("SalesCounter", 0);
        values.put("HRCounter", 0);
        values.put("VacationCounter", 0);
        values.put("VacationSalaryCounter", 0);
        values.put("ResignationCounter", 0);
        values.put("BacksCounter", 0);
        values.put("CustudyCounter", 0);
        values.put("AdvancePaymentCounter", 0);

        try {
            long X = db.insert("Counters", null, values);
            if (X == -1) {
                result = false;
            } else if (X > 0) {
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public boolean setProjectsCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("ProjectsCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getProjectsCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"ProjectsCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setSalesCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("SalesCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getSalesCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"SalesCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setHRCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("HRCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getHRCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"HRCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setMaintenanceOrderCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("MaintenanceOrdersCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getMaintenanceOrderCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"MaintenanceOrdersCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setVacationsCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("VacationCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getVacationsCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"VacationCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setVacationsSalaryCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("VacationSalaryCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getResignationCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"VacationSalaryCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setResignationCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("ResignationCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getVacationsSalaryCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"ResignationCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setBackCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("BacksCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getBackCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"BacksCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setCustodyCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("CustudyCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getCustodyCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"CustudyCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

    public boolean setAdvancePaymentCounter(int newCounter) {
        boolean status = false;
        ContentValues cv = new ContentValues();
        cv.put("AdvancePaymentCounter", newCounter);
        int x = db.update("Counters", cv, "", null);
        if (x == 1) {
            status = true;
        }
        return status;
    }

    public int getAdvancePaymentCounter() {
        int result = 0;
        Cursor c = db.query("Counters", new String[]{"AdvancePaymentCounter"}, "", null, null, null, null);
        c.moveToFirst();
        result = c.getInt(0);
        c.close();
        return result;
    }

}

