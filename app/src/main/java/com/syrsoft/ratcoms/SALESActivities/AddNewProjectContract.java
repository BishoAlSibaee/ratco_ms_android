package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.VolleyMultipartRequest;
import com.syrsoft.ratcoms.select_other_location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddNewProjectContract extends AppCompatActivity {

    Activity act;
    EditText SearchField, ProjectName, ProjectDesc, ProjectResponsible, ResponsibleMobile, DeliveryLocationET, AvailabilityET, InstallationET, WarrantyET, PaymentET1, PaymentET2, PaymentET3, PaymentET4, PaymentET1text, PaymentET2text, PaymentET3text, PaymentET4text;
    TextView SupplyDateET , InstallDateET , HandoverDateET ;
    TextView ClientNameTV, ResponsibleNameTV, FileNameTV , LocationTV , ContractDateTV;
    Spinner SearchBySpinner, ClientsResultSpinner;
    ProgressBar p;
    String[] SearchByArray, ResultArray;
    Button cancel, select;
    CLIENT_CLASS THE_CLIENT;
    List<CLIENT_CLASS> THE_Result_CLIENTS;
    String searchClientUrl = MyApp.MainUrl + "searchClient.php";
    String getInChargesUrl = MyApp.MainUrl + "getInCharges.php";
    String saveContractUrl = MyApp.MainUrl + "insertNewProject.php";
    String recordFileLinkInTableUrl = MyApp.MainUrl+"updateTableLinkField.php" ;
    List<RESPONSIBLE_CLASS> Responsible;
    int CAM_REQCODE, ATTACHFILE_REQCODE;
    private String upload_URL = MyApp.MainUrl + "insertFile.php";
    private RequestQueue Q;
    String url = "https://www.google.com";
    Uri UriFile;
    String FileName;
    CheckBox DeliveryLocationCB, PaymentCB, AvailabilityCB, InstallationCB, WarrantyCB , InstalledCB , SuppliedCB , HandoveredCB ;
    LocationManager locationManager;
    LocationListener listener;
    public static Location THE_LOCATION;
    int PermissionRequestCode = 34 ;
    int ReqCodeOtherLocation = 18 ;
    Loading LocationLoadingDialog ;
    List<Address> addresses ;
    TermsAndConditions termsAndConditions ;
    List<PaymentTerms> paymentTerms ;
    public static List<View> ItemsList ;
    LinearLayout ItemsLayout , ProjectStatusLayout ;
    String ContractDate , SupplyDate , InstallDate , HandoverDate , WarrantyExpireDate ;
    RadioButton OldContractRB , NewContractRB ;
    String ContractType ;
    Button attachFileBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_add_new_project_contract_activity);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this;
        termsAndConditions = new TermsAndConditions();
        ClientNameTV = (TextView) findViewById(R.id.AddNewProject_ClientName);
        ResponsibleNameTV = (TextView) findViewById(R.id.AddNewProject_Responsible);
        ProjectName = (EditText) findViewById(R.id.AddNewProject_ProjectName);
        ProjectDesc = (EditText) findViewById(R.id.AddNewProject_ProjectDesc);
        ContractDateTV = (TextView) findViewById(R.id.AddNewProject_ContractDate);
        ProjectResponsible = (EditText) findViewById(R.id.AddNewProject_ProjectResponsible);
        ResponsibleMobile = (EditText) findViewById(R.id.AddNewProject_ProjectResponsibleMobile);
        attachFileBtn = findViewById(R.id.AddNewClientDialog_attach);
        FileNameTV = (TextView) findViewById(R.id.fileName);
        DeliveryLocationCB = (CheckBox) findViewById(R.id.addContract_deliveryLocationCheckbox);
        PaymentCB = (CheckBox) findViewById(R.id.addContract_paymetnCheckbox);
        AvailabilityCB = (CheckBox) findViewById(R.id.addContract_availabilityCheckbox);
        InstallationCB = (CheckBox) findViewById(R.id.addContract_installationCheckbox);
        WarrantyCB = (CheckBox) findViewById(R.id.addContract_warrantyCheckbox);
        InstalledCB = (CheckBox) findViewById(R.id.addContract_installedCB);
        SuppliedCB = (CheckBox) findViewById(R.id.addContract_suppliedCB);
        HandoveredCB = (CheckBox) findViewById(R.id.addContract_handoveredCB);
        SupplyDateET = (TextView) findViewById(R.id.supplyDateTV);
        SupplyDateET.setVisibility(View.GONE);
        InstallDateET = (TextView) findViewById(R.id.installDateTV);
        InstallDateET.setVisibility(View.GONE);
        HandoverDateET = (TextView) findViewById(R.id.handoverDateTV);
        HandoverDateET.setVisibility(View.GONE);
        OldContractRB = (RadioButton) findViewById(R.id.radioButton5);
        NewContractRB = (RadioButton) findViewById(R.id.radioButton4);
        DeliveryLocationET = (EditText) findViewById(R.id.addCLient_deliveryLocationET);
        DeliveryLocationET.setEnabled(false);
        AvailabilityET = (EditText) findViewById(R.id.addCLient_avaliabilityET);
        AvailabilityET.setEnabled(false);
        PaymentET1 = (EditText) findViewById(R.id.addClient_paymentED1);
        PaymentET1.setEnabled(false);
        PaymentET2 = (EditText) findViewById(R.id.addClient_paymentED2);
        PaymentET2.setEnabled(false);
        PaymentET3 = (EditText) findViewById(R.id.addClient_paymentED3);
        PaymentET3.setEnabled(false);
        PaymentET4 = (EditText) findViewById(R.id.addClient_paymentED4);
        PaymentET4.setEnabled(false);
        InstallationET = (EditText) findViewById(R.id.addCLient_installationET);
        InstallationET.setEnabled(false);
        WarrantyET = (EditText) findViewById(R.id.addCLient_warrantyET);
        WarrantyET.setEnabled(false);
        PaymentET1text = (EditText) findViewById(R.id.addClient_paymentED1text);
        PaymentET1text.setEnabled(false);
        PaymentET2text = (EditText) findViewById(R.id.addClient_paymentED2text);
        PaymentET2text.setEnabled(false);
        PaymentET3text = (EditText) findViewById(R.id.addClient_paymentED3text);
        PaymentET3text.setEnabled(false);
        PaymentET4text = (EditText) findViewById(R.id.addClient_paymentED4text);
        PaymentET4text.setEnabled(false);
        LocationTV = (TextView) findViewById(R.id.LocationTV);
        Responsible = new ArrayList<RESPONSIBLE_CLASS>();
        THE_Result_CLIENTS = new ArrayList<CLIENT_CLASS>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationLoadingDialog = new Loading(act);
        paymentTerms = new ArrayList<PaymentTerms>();
        ItemsList = new ArrayList<View>();
        ProjectStatusLayout = (LinearLayout) findViewById(R.id.projectStatusLayout);
    }

    void setActivityActions() {
        ClientNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_old_client_dialog);
                D.setCancelable(false);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                SearchBySpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchBySpinner);
                ClientsResultSpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchResultSpinner);
                SearchField = (EditText) D.findViewById(R.id.MyVisitsReports_searchWord);
                p = (ProgressBar) D.findViewById(R.id.progressBar3);
                p.setVisibility(View.GONE);
                SearchByArray = getResources().getStringArray(R.array.searchByArray);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, SearchByArray);
                SearchBySpinner.setAdapter(adapter);
                SearchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        p.setVisibility(View.VISIBLE);
                        searchClient(SearchBySpinner.getSelectedItemPosition(), SearchField.getText().toString());

                    }
                });
                cancel = (Button) D.findViewById(R.id.oldClientsDialog_cancelBtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                select = (Button) D.findViewById(R.id.oldClientsDialog_selectBtn);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ClientsResultSpinner.getSelectedItem() == null) {
                            ToastMaker.Show(0, "select client first", act);
                            return;
                        }
                        THE_CLIENT = THE_Result_CLIENTS.get(ClientsResultSpinner.getSelectedItemPosition());
                        ClientNameTV.setText(THE_CLIENT.ClientName);
                        getInCharges();
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        DeliveryLocationCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DeliveryLocationET.setEnabled(true);
                } else {
                    DeliveryLocationET.setEnabled(false);
                }
            }
        });
        AvailabilityCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AvailabilityET.setEnabled(true);
                } else {
                    AvailabilityET.setEnabled(false);
                }
            }
        });
        InstallationCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InstallationET.setEnabled(true);
                } else {
                    InstallationET.setEnabled(false);
                }
            }
        });
        WarrantyCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    WarrantyET.setEnabled(true);
                } else {
                    WarrantyET.setEnabled(false);
                }
            }
        });
        PaymentCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PaymentET1.setEnabled(true);
                    PaymentET2.setEnabled(true);
                    PaymentET3.setEnabled(true);
                    PaymentET4.setEnabled(true);
                    PaymentET1text.setEnabled(true);
                    PaymentET2text.setEnabled(true);
                    PaymentET3text.setEnabled(true);
                    PaymentET4text.setEnabled(true);
                } else {
                    PaymentET1.setEnabled(false);
                    PaymentET2.setEnabled(false);
                    PaymentET3.setEnabled(false);
                    PaymentET4.setEnabled(false);
                    PaymentET1text.setEnabled(false);
                    PaymentET2text.setEnabled(false);
                    PaymentET3text.setEnabled(false);
                    PaymentET4text.setEnabled(false);
                }
            }
        });
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LocationLoadingDialog.close();
                THE_LOCATION = location;
                locationManager.removeUpdates(listener);
                Geocoder geocoder;
                geocoder = new Geocoder(act, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(THE_LOCATION.getLatitude(), THE_LOCATION.getLongitude(), 1);
                    LocationTV.setText(addresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };
        Q = Volley.newRequestQueue(act);
        ContractDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        ContractDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(ContractDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContractDateTV.setText(ContractDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        InstalledCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InstallDateET.setVisibility(View.VISIBLE);
                    Dialog D = new Dialog(act);
                    D.setContentView(R.layout.dialog_select_date_dialog);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    D.setCancelable(false);
                    CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                    TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                    C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            InstallDate = year+"-"+(month+1)+"-"+dayOfMonth;
                            date.setText(InstallDate);
                        }
                    });
                    Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                    Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                    Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                            InstalledCB.setChecked(false);
                        }
                    });
                    Select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InstallDateET.setText(InstallDate);
                            D.dismiss();
                        }
                    });
                    D.show();
                }
                else {
                    InstallDateET.setVisibility(View.GONE);
                    InstallDateET.setText("");
                }
            }
        });
        InstallDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        InstallDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(InstallDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                        InstalledCB.setChecked(false);
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InstallDateET.setText(InstallDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        SupplyDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        SupplyDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(SupplyDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                        SuppliedCB.setChecked(false);
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupplyDateET.setText(SupplyDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        SuppliedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SupplyDateET.setVisibility(View.VISIBLE);
                    Dialog D = new Dialog(act);
                    D.setContentView(R.layout.dialog_select_date_dialog);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    D.setCancelable(false);
                    CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                    TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                    C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            SupplyDate = year+"-"+(month+1)+"-"+dayOfMonth;
                            date.setText(SupplyDate);
                        }
                    });
                    Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                    Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                    Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                            SuppliedCB.setChecked(false);
                        }
                    });
                    Select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SupplyDateET.setText(SupplyDate);
                            D.dismiss();
                        }
                    });
                    D.show();
                }
                else {
                    SupplyDateET.setVisibility(View.GONE);
                    SupplyDateET.setText("");
                }
            }
        });
        HandoverDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        HandoverDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(HandoverDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                        HandoveredCB.setChecked(false);
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HandoverDateET.setText(HandoverDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        HandoveredCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HandoverDateET.setVisibility(View.VISIBLE);
                    Dialog D = new Dialog(act);
                    D.setContentView(R.layout.dialog_select_date_dialog);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    D.setCancelable(false);
                    CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                    TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                    C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            HandoverDate = year+"-"+(month+1)+"-"+dayOfMonth;
                            date.setText(HandoverDate);
                        }
                    });
                    Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                    Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                    Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                            HandoveredCB.setChecked(false);
                        }
                    });
                    Select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HandoverDateET.setText(HandoverDate);
                            D.dismiss();
                        }
                    });
                    D.show();
                }
                else {
                    HandoverDateET.setVisibility(View.GONE);
                    HandoverDateET.setText("");
                }
            }
        });
        WarrantyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (WarrantyET.getText() != null && !WarrantyET.getText().toString().isEmpty()) {
                    if (Integer.parseInt(WarrantyET.getText().toString()) > 10) {
                        WarrantyET.setText("");
                        ToastMaker.Show(0,"warranty must be less than 10 years",act);
                    }
                }
            }
        });
        OldContractRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ContractType = "Old";
                    NewContractRB.setChecked(false);
                    ProjectStatusLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        NewContractRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OldContractRB.setChecked(false);
                    ContractType = "New";
                    ProjectStatusLayout.setVisibility(View.GONE);
                }
            }
        });
        NewContractRB.setChecked(true);
        attachFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    act.startActivityForResult(Intent.createChooser(intent, "select File"), 6);
                    //act.startActivity(Intent.createChooser(intent, "select File"));
                }
                catch (Exception e) {
                    Log.d("AttacheFileError",e.getMessage()) ;
                }
            }
        });
    }

    void searchClient(int searchBy, String field) {

        if (field.isEmpty()) {
            ResultArray = new String[]{""};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, ResultArray);
            ClientsResultSpinner.setAdapter(adapter);
            p.setVisibility(View.GONE);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, searchClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                p.setVisibility(View.GONE);
                if (response.equals("0")) {
                    ToastMaker.Show(1, "no results", act);
                } else if (response.equals("-1")) {
                    ToastMaker.Show(1, "error", act);
                } else {
                    try {
                        THE_Result_CLIENTS.clear();
                        JSONArray arr = new JSONArray(response);
                        ResultArray = new String[arr.length()];
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"), row.getString("ClientName"), row.getString("City"), row.getString("PhonNumber"), row.getString("Address"), row.getString("Email"), row.getInt("SalesMan"), row.getDouble("LA"), row.getDouble("LO"), row.getString("FieldOfWork"));
                            THE_Result_CLIENTS.add(c);
                            ResultArray[i] = c.ClientName;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, ResultArray);
                    ClientsResultSpinner.setAdapter(adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.setVisibility(View.GONE);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> par = new HashMap<String, String>();
                par.put("searchBy", String.valueOf(searchBy));
                par.put("Field", field);
                par.put("SalesMan", String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getInCharges() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getInChargesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {

                } else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        Responsible.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            RESPONSIBLE_CLASS r = new RESPONSIBLE_CLASS(row.getInt("id"), row.getInt("ClientID"), row.getString("Name"), row.getString("JobTitle"), row.getString("MobileNumber"), row.getString("Email"), row.getString("Link"));
                            Responsible.add(r);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    THE_CLIENT.setResponsibles(Responsible);
                    ResponsibleNameTV.setText(THE_CLIENT.getResponsibles().get(0).Name);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("ClientID", String.valueOf(THE_CLIENT.id));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

//    public void attachImageFile(View view) {
////        AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
////        selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
////        selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
////        selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
////                {
////                    ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},ClientVisitReport.CAM_PERMISSION_REQCODE);
////                }
////                else {
////                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    try {
////                        act.startActivityForResult(takePictureIntent, CAM_REQCODE);
////                    } catch (ActivityNotFoundException e) {
////                        // display error state to the user
////                    }
////
////                }
////            }
////        });
////        selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                Intent open = new Intent(Intent.ACTION_GET_CONTENT);
////                open.setType("image/*");
////                act.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE);
////            }
////        });
////        selectDialog.create().show();
//
////        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
////        intent.setType("application/pdf");
////        // Optionally, specify a URI for the file that should appear in the
////        // system file picker when it loads.
////        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
////        act.startActivityForResult(Intent.createChooser(intent,"select File"), ATTACHFILE_REQCODE);
//
//    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQCODE) {
        } else if (requestCode == 6) {
            Log.d("fileselection", "here");
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Log.d("fileselection", "error");
                    return;
                }
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    UriFile = uri;
                    // Perform operations on the document using its URI.
                    File F = new File(uri.toString());
                    String path = F.getAbsolutePath();
                    String displayName = null;
                    if (uri.toString().startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                FileName = displayName;
                                FileNameTV.setText(FileName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uri.toString().startsWith("file://")) {
                        displayName = F.getName();
                        Log.d("fileselection", displayName);
                    }
                }
//                try {
//                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
//                    try {
//                        File F = bufferedInputStream.read() ;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    CardBitmap = bmp ;
//                    image.setImageBitmap(bmp);
//                    fileName.setText(x+".jpg");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            } else {
                //Log.d("path", "error result " + data.getData().getPath());
            }
        }
        else if (requestCode == ReqCodeOtherLocation) {
            if (data == null) {
                return;
            }
            String LA = data.getStringExtra("LA");
            String LO = data.getStringExtra("LO");
            Log.d("otherLocation" , LA+" "+LO);
            THE_LOCATION = new Location("");
            THE_LOCATION.setLatitude(Double.parseDouble(LA));
            THE_LOCATION.setLongitude(Double.parseDouble(LO));
            Geocoder geocoder;
            geocoder = new Geocoder(act, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(THE_LOCATION.getLatitude(), THE_LOCATION.getLongitude(), 1);
                if (addresses.size() > 0 ) {
                    LocationTV.setText(addresses.get(0).getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestCode) {
            if (grantResults != null && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        LocationLoadingDialog.show();
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,listener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,listener);
                        Log.d("locationService", "i am started");
                    }
                }
                else {
                    ToastMaker.Show(1,getResources().getString(R.string.mustAcceptLocationPermission),act);
                    act.finish();
                }
            }
        }
    }

    private void uploadPDF(final String pdfname, Uri pdffile , int ID) {

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            Q.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                jsonObject.toString().replace("\\\\", "");
                                recordFileLinkInTable(jsonObject.getString("message"),"Projects","ContractLink",ID);
//                                if (jsonObject.getString("status").equals("true")) {
//                                    Log.d("come::: >>>  ", "yessssss");
//                                    arraylist = new ArrayList<HashMap<String, String>>();
//                                    JSONArray dataArray = jsonObject.getJSONArray("data");
//                                    for (int i = 0; i < dataArray.length(); i++) {
//                                        JSONObject dataobj = dataArray.getJSONObject(i);
//                                        url = dataobj.optString("pathToFile");
//                                        //FileNameTV.setText(url);
//                                        Log.d("ressssssoo", url);
//                                    }
//
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("image", new DataPart(pdfname, inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Q = Volley.newRequestQueue(act);
            Q.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    void saveContract() {
        if (THE_CLIENT == null) {
            ToastMaker.Show(1, getResources().getString(R.string.pleaseSelectClient), act);
            return;
        }
        if (ProjectName.getText() == null || ProjectName.getText().toString().isEmpty()) {
            ToastMaker.Show(1, getResources().getString(R.string.pleaseEnterProjectName), act);
            return;
        }
        if (ProjectDesc.getText() == null || ProjectDesc.getText().toString().isEmpty()) {
            ToastMaker.Show(1, getResources().getString(R.string.pleaseEnterProjectDesc), act);
            return;
        }
        if (THE_LOCATION == null && addresses == null ) {
            ToastMaker.Show(1,getResources().getString(R.string.selectLocation),act);
            return;
        }
        if (ContractDateTV == null || ContractDateTV.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.selectDate) ,act);
            ContractDateTV.setHintTextColor(Color.RED);
            return;
        }
        if (AvailabilityET.getText() == null || AvailabilityET.getText().toString().isEmpty()) {
            ToastMaker.Show(1, getResources().getString(R.string.enterAvaliability), act);
            return;
        }
        if (WarrantyET.getText() == null || WarrantyET.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.enter_warranty),act);
            return;
        }

        if (HandoveredCB.isChecked() && HandoverDateET.getText() != null && !HandoverDateET.getText().toString().isEmpty()) {
            if (WarrantyET.getText() == null || WarrantyET.getText().toString().isEmpty()) {
                WarrantyET.setHintTextColor(Color.RED);
                ToastMaker.Show(1,"enter warranty in years",act);
                return;
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    int years = Integer.parseInt(WarrantyET.getText().toString());
                    date = sdf.parse(HandoverDateET.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.YEAR , years );
                    Calendar now = Calendar.getInstance(Locale.getDefault());
                    WarrantyExpireDate = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("warrantyError" , e.getMessage());
                }
            }
        }
        if (DeliveryLocationET.getText() != null && !DeliveryLocationET.getText().toString().isEmpty()) {
            termsAndConditions.setDeliveryLocation(DeliveryLocationET.getText().toString());
        }
        if (AvailabilityET.getText() != null && !AvailabilityET.getText().toString().isEmpty()) {
            termsAndConditions.setAvailability(AvailabilityET.getText().toString());
        }
        if (InstallationET.getText() != null && !InstallationET.getText().toString().isEmpty()) {
            termsAndConditions.setInstallation(InstallationET.getText().toString());
        }
        if (WarrantyET.getText() != null && !WarrantyET.getText().toString().isEmpty()) {
            termsAndConditions.setWarranty(WarrantyET.getText().toString());
        }
        if (PaymentET1.getText() != null && !PaymentET1.getText().toString().isEmpty() && PaymentET1text.getText() != null && !PaymentET1text.getText().toString().isEmpty() ) {
            paymentTerms.add(new PaymentTerms(PaymentET1.getText().toString(),PaymentET1text.getText().toString()));
        }
        if (PaymentET2.getText() != null && !PaymentET2.getText().toString().isEmpty() && PaymentET2text.getText() != null && !PaymentET2text.getText().toString().isEmpty()) {
            paymentTerms.add(new PaymentTerms(PaymentET2.getText().toString(),PaymentET2text.getText().toString()));
        }
        if (PaymentET3.getText() != null && !PaymentET3.getText().toString().isEmpty() && PaymentET3text.getText() != null && !PaymentET3text.getText().toString().isEmpty()) {
            paymentTerms.add(new PaymentTerms(PaymentET3.getText().toString(),PaymentET3text.getText().toString()));
        }
        if (PaymentET4.getText() != null && !PaymentET4.getText().toString().isEmpty() && PaymentET4text.getText() != null && !PaymentET4text.getText().toString().isEmpty()) {
            paymentTerms.add(new PaymentTerms(PaymentET4.getText().toString(),PaymentET4text.getText().toString()));
        }
        if (paymentTerms != null && paymentTerms.size() > 0  ) {
            termsAndConditions.setPayment(paymentTerms);
        }
        int TermsCounter = 0 ;
        if (termsAndConditions != null ) {
            if (termsAndConditions.getDeliveryLocation() != null && !termsAndConditions.getDeliveryLocation().isEmpty()) {
                TermsCounter++ ;
            }
            if (termsAndConditions.getAvailability() != null && !termsAndConditions.getAvailability().isEmpty()) {
                TermsCounter++ ;
            }
            if (termsAndConditions.getInstallation() != null && !termsAndConditions.getInstallation().isEmpty()) {
                TermsCounter++;
            }
            if (termsAndConditions.getWarranty() != null && !termsAndConditions.getWarranty().isEmpty()) {
                TermsCounter++;
            }
            if ( termsAndConditions.getPayment() != null &&  termsAndConditions.getPayment().size()>0) {
                TermsCounter++;
            }
        }
        Loading d = new Loading(act); d.show();
        int finalTermsCounter = TermsCounter;
        StringRequest request = new StringRequest(Request.Method.POST, saveContractUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                Log.d("saveProjectResp" , response);
                if (Integer.parseInt(response) > 0 ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Saved","Saves",0);
                    if (UriFile != null && FileName != null ) {
                        uploadPDF(FileName,UriFile,Integer.parseInt(response));
                    }
                }
                else if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Error Try Again");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
                Log.d("saveProjectResp" , error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String, String> par = new HashMap<String, String>();
                par.put("ClientID", String.valueOf(THE_CLIENT.id));
                par.put("ProjectName", ProjectName.getText().toString());
                par.put("Date" , ContractDateTV.getText().toString()) ;
                if (THE_LOCATION != null && addresses != null  ) {
                    par.put("City", addresses.get(0).getLocality());
                    par.put("Address" ,addresses.get(0).getAddressLine(0));
                    par.put("LA" , String.valueOf(THE_LOCATION.getLatitude()));
                    par.put("LO",String.valueOf(THE_LOCATION.getLongitude()));
                }
                par.put("ProjectDescription", ProjectDesc.getText().toString());
                par.put("SalesMan", String.valueOf(MyApp.db.getUser().JobNumber));
                if (ProjectResponsible.getText() != null && !ProjectResponsible.getText().toString().isEmpty()) {
                    par.put("ProjectManager", ProjectResponsible.getText().toString());
                }
                if (ResponsibleMobile.getText() != null && !ResponsibleMobile.getText().toString().isEmpty()) {
                    par.put("MobileNumber", ResponsibleMobile.getText().toString());
                }
                if (finalTermsCounter > 0 ) {
                    par.put("TermsTotal" , String.valueOf(finalTermsCounter));
                    int counter = 0 ;
                    if (termsAndConditions.getDeliveryLocation() != null && !termsAndConditions.getDeliveryLocation().isEmpty()) {
                        par.put("Term"+counter ,termsAndConditions.getDeliveryLocation() );
                        par.put("name"+counter , "DeliveryLocation");
                        counter++;
                    }
                    if (termsAndConditions.getAvailability() != null && !termsAndConditions.getAvailability().isEmpty()) {
                        par.put("Term"+counter ,termsAndConditions.getAvailability() );
                        par.put("name"+counter , "Availability");
                        counter++;
                    }
                    if (termsAndConditions.getInstallation() != null && !termsAndConditions.getInstallation().isEmpty()) {
                        par.put("Term"+counter , termsAndConditions.getInstallation() );
                        par.put("name"+counter , "Installation");
                        counter++;
                    }
                    if (termsAndConditions.getWarranty() != null && !termsAndConditions.getWarranty().isEmpty()) {
                        par.put("Term"+counter , termsAndConditions.getWarranty() );
                        par.put("name"+counter , "Warranty");
                        counter++;
                    }
                    if (termsAndConditions.getPayment() != null && termsAndConditions.getPayment().size()>0) {
                        String tt = "" ;
                        for (PaymentTerms p : termsAndConditions.getPayment()) {
                            tt = tt+"-"+p.Percent+"%"+p.Condition ;
                        }
                        par.put("Term"+counter , tt );
                        par.put("name"+counter , "Payment");
                        counter++ ;
                    }
                }

                if (ItemsList.size() > 0 ) {
                    par.put("ItemsTotal" , String.valueOf(ItemsList.size()));
                    for (int i=0;i<ItemsList.size();i++) {
                        EditText itemName = (EditText) ItemsList.get(i).findViewById(R.id.itemUnit_name);
                        EditText itemQuantity = (EditText) ItemsList.get(i).findViewById(R.id.itemUnit_quantity);
                     //   EditText itemPrice = (EditText) ItemsList.get(i).findViewById(R.id.itemUnit_price);
                        String name = "" ;
                        String quantity = "" ;
//                        String price = "" ;
                        if (itemName.getText() != null && !itemName.getText().toString().isEmpty()) {
                            name = itemName.getText().toString();
                        }
                        par.put("itemName"+i , name);
                        if (itemQuantity.getText() != null && !itemQuantity.getText().toString().isEmpty()) {
                            quantity = itemQuantity.getText().toString() ;
                        }
                        par.put("quantity"+i , quantity);
//                        if (itemPrice.getText() != null && !itemPrice.getText().toString().isEmpty()) {
//                            price = itemPrice.getText().toString() ;
//                        }
 //                       par.put("price"+i , price);
                    }
                }

                if (SuppliedCB.isChecked() && SupplyDateET.getText() != null && !SupplyDateET.getText().toString().isEmpty()) {
                    par.put("SupplyDate" , SupplyDateET.getText().toString());
                }

                if (InstalledCB.isChecked() && InstallDateET.getText() != null && !InstallDateET.getText().toString().isEmpty()) {
                    par.put("InstallDate" , InstallDateET.getText().toString());
                }

                if (HandoveredCB.isChecked() && HandoverDateET.getText() != null && !HandoverDateET.getText().toString().isEmpty()) {
                    par.put("HandoverDate" , HandoverDateET.getText().toString() );
                }

                if (WarrantyExpireDate != null ) {
                    par.put("WarrantyExpireDate",WarrantyExpireDate);
                }


                return par;
            }
        };

        Q.add(request);
    }

    public void getCurrentLocation(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            LocationLoadingDialog.show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,listener);
        }
        else {
            AlertDialog.Builder B = new AlertDialog.Builder(act);
            B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
            B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
            B.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PermissionRequestCode);
                }
            });
            B.create();
            B.show();
        }
    }

    public void selectOtherLocation(View view) {
        Intent i = new Intent(act, select_other_location.class);
        act.startActivityForResult(i,ReqCodeOtherLocation);
    }

    public void save(View view) {
        saveContract();
    }

//    public void addNewItem(View view) {
//        if (ItemsList.size() == 0) {
//            Log.d("showItemList" , "Enter if ");
//            View v = LayoutInflater.from(act).inflate(R.layout.item_unit,null);
//            EditText itemName = v.findViewById(R.id.itemUnit_name);
//            itemName.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Log.d("showItemList","Enter if Long Click A");
//                    Search_Item_Dialog D = new Search_Item_Dialog(act);
//                    D.show();
//                    return false;
//                }
//            });
//            ItemsList.add(v);
//            ItemsLayout.addView(v);
//            Log.d("showItemList" , "Enter if Long Click B");
//            Search_Item_Dialog D = new Search_Item_Dialog(act);
//            D.show();
//        }
//        else {
//                EditText itemName = ItemsList.get(ItemsList.size()-1).findViewById(R.id.itemUnit_name);
//                EditText itemQuantity = ItemsList.get(ItemsList.size()-1).findViewById(R.id.itemUnit_quantity);
//                itemName.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Log.d("showItemList" , "Enter if Long Click C");
//                    Search_Item_Dialog D = new Search_Item_Dialog(act);
//                    D.show();
//                    return false;
//                }
//            });
//                if (itemName.getText() == null || itemName.getText().toString().isEmpty()) {
//                    ToastMaker.Show(1,"enter the current item information",act);
//                    itemName.setHintTextColor(Color.RED);
//                }
//                else if (itemQuantity.getText() == null || itemQuantity.getText().toString().isEmpty()) {
//                    ToastMaker.Show(1,"enter the current item information",act);
//                    itemQuantity.setHintTextColor(Color.RED);
//                }
//                else {
//                    View v = LayoutInflater.from(act).inflate(R.layout.item_unit,null);
//                    EditText itemNameee = v.findViewById(R.id.itemUnit_name);
//                    itemNameee.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//                            Log.d("showItemList" , "Enter if Long Click D");
//                            Search_Item_Dialog D = new Search_Item_Dialog(act);
//                            D.show();
//                            return false;
//                        }
//                    });
//                    ItemsList.add(v);
//                    ItemsLayout.addView(v);
//                    Log.d("showItemList" , "Enter if Long Click E");
//                    Search_Item_Dialog D = new Search_Item_Dialog(act);
//                    D.show();
//                }
//        }
//        Log.d("itemsCount",ItemsList.size()+" ");
//        for (int i=0;i<ItemsList.size();i++) {
//            EditText itemName = (EditText) ItemsList.get(i).findViewById(R.id.itemUnit_name);
//            EditText itemQuantity = (EditText) ItemsList.get(i).findViewById(R.id.itemUnit_quantity);
//            itemName.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Log.d("showItemList" , "Enter if Long Click F" + ItemsList.size());
//                    Search_Item_Dialog D = new Search_Item_Dialog(act);
//                    D.show();
//                    return false;
//                }
//            });
//            if (itemName.getText() == null || itemName.getText().toString().isEmpty()) {
//                ToastMaker.Show(1,"enter the current item information",act);
//                itemName.setHintTextColor(Color.RED);
//                return;
//            }
//            if (itemQuantity.getText() == null || itemQuantity.getText().toString().isEmpty()) {
//                ToastMaker.Show(1,"enter the current item information",act);
//                itemQuantity.setHintTextColor(Color.RED);
//                return;
//            }
//        }
////        if (ItemsList.size() == 0 ) {
////            View v = LayoutInflater.from(act).inflate(R.layout.item_unit,null);
////            ItemsList.add(v);
////            ItemsLayout.addView(v);
////            EditText itemName = (EditText) ItemsList.get(ItemsList.size()-1).findViewById(R.id.itemUnit_name);
////            itemName.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    Search_Item_Dialog D = new Search_Item_Dialog(act);
////                    D.show();
////                    return false;
////                }
////            });
////            Search_Item_Dialog D = new Search_Item_Dialog(act);
////            D.show();
////        }
////        else {
////            EditText itemName = (EditText) ItemsList.get(ItemsList.size()-1).findViewById(R.id.itemUnit_name);
////            EditText itemQuantity = (EditText) ItemsList.get(ItemsList.size()-1).findViewById(R.id.itemUnit_quantity);
////            EditText itemPrice = (EditText) ItemsList.get(ItemsList.size()).findViewById(R.id.itemUnit_price);
////            itemName.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    Search_Item_Dialog D = new Search_Item_Dialog(act);
////                    D.show();
////                    return false;
////                }
////            });
////            if (itemName.getText() == null || itemName.getText().toString().isEmpty()) {
////                ToastMaker.Show(1,"enter the current item information",act);
////                itemName.setHintTextColor(Color.RED);
////                return;
////            }
////            if (itemQuantity.getText() == null || itemQuantity.getText().toString().isEmpty()) {
////                ToastMaker.Show(1,"enter the current item information",act);
////                itemQuantity.setHintTextColor(Color.RED);
////                return;
////            }
////            if (itemPrice.getText() == null || itemPrice.getText().toString().isEmpty()) {
////                ToastMaker.Show(1,"enter the current item information",act);
////                itemPrice.setHintTextColor(Color.RED);
////                return;
////            }
////            View v = LayoutInflater.from(act).inflate(R.layout.item_unit,null);
////            ItemsList.add(v);
////            ItemsLayout.addView(v);
////            Search_Item_Dialog D = new Search_Item_Dialog(act);
////            D.show();
////        }
//    }

    void recordFileLinkInTable(String Link , String Table , String Column , int ID) {

        StringRequest request = new StringRequest(Request.Method.POST, recordFileLinkInTableUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("insertIntoTable" , response);
                if (response.equals("1")) {
                    ToastMaker.Show(0,"file saved" ,act);
                }
                else if (response.equals("0")) {
                    ToastMaker.Show(0,"file not saved" ,act);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("insertIntoTable" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table",Table);
                par.put("ID",String.valueOf(ID));
                par.put("Link",Link);
                par.put("Field",Column);
                return par;
            }
        };
        Q.add(request);
    }
}