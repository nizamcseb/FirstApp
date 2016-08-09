package com.mst.salesforce.testapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobActivity extends SalesforceActivity {

    private ListView listView;
    private RestClient client;
    private String str_id, str_fname, str_mname, str_lname, str_email, str_wemail, str_mobile, str_gender, str_marital, str_term, str_program;
    EditText et_fname, et_mname, et_lname, et_email, et_wEmail, et_mobile;
    Spinner sp_term, sp_program, sp_gender, sp_marital;
    Button btn_apply, btn_cancel;
    // JSONArray records;
    List<String> program_name = new ArrayList<String>();
    List<String> program_id = new ArrayList<String>();
    List<String> term_name = new ArrayList<String>();
    List<String> term_id = new ArrayList<String>();
    //List<String[]> program = new ArrayList<String[]>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_application);


    }

    public void doinBtnInvoked() {
        str_fname = et_fname.getText().toString();
        str_mname = et_mname.getText().toString();
        str_lname = et_lname.getText().toString();
        str_email = et_email.getText().toString();
        str_wemail = et_wEmail.getText().toString();
        str_mobile = et_mobile.getText().toString();
        // str_gender = et_fname.getText().toString();
        //  str_marital = et_fname.getText().toString();
        // str_term = et_fname.getText().toString();
        // str_program = sp_program.ge.getText().toString();

        readUserInput();
        ApplyApplication();
        //setResult(RESULT_OK);
        // finish();
    }

    private void ApplyApplication() {

        String ApplicationObjectType = "Application__c";

       // JSONObject jsonBody = new JSONObject();

        HashMap<String, Object> new_Application = new HashMap<String, Object>();
        new_Application.put("FirstName__c", str_fname);
        new_Application.put("LastName__c", str_lname);
        new_Application.put("MiddleName__c", str_mname);
        new_Application.put("Term__c", str_term);
        new_Application.put("Program_Account__c", str_program);
        new_Application.put("ownerId", client.getClientInfo().userId);
        new_Application.put("Gender__c", str_gender);
        new_Application.put("ReferenceEmail2__c", str_email);
        new_Application.put("WorkEmail__c", str_wemail);
        new_Application.put("Mobile__c", str_mobile);
        new_Application.put("Married__c", str_marital);

      /*  new_Application.put("Date_of_Birth__c", "");
        new_Application.put("PermanentStreet_and_No_line_1__c", "");
        new_Application.put("PermanentStreet_and_No_line_2__c", "");
        new_Application.put("PermanentStreet_and_No_line_3__c", "");
        new_Application.put("EducationCity2__c", "");
        new_Application.put("PermanentState__c", "");
        new_Application.put("Country__c", "");
        new_Application.put("PermanentPostalcode__c", "");
        new_Application.put("Are_you_a_US_citizenship__c", "");
        new_Application.put("Country_of_Citizenship__c", "");
        new_Application.put("Visa_Status__c", "");
        new_Application.put("U_S_Social_Security_Number__c", "");
        new_Application.put("College_University1__c", "");
        new_Application.put("schoolcity__c", "");
        new_Application.put("Other_State_Edu1__c", "");
        new_Application.put("HighSchoolGradePointAverage__c", "");
        new_Application.put("HighSchoolGraduationDate__c", "");
        new_Application.put("High_School_Name__c", "");
        new_Application.put("High_School_Address__c", "");
        new_Application.put("Date_of_Graduation__c", "");
        new_Application.put("GPA__c", "");
        new_Application.put("Previous_College_School_Name__c", "");
        new_Application.put("Previous_College_Address__c", "");
        new_Application.put("Previous_College_Degree_Awarded__c", "");
        new_Application.put("Previous_College_Date_of_Attendance__c", "");
        new_Application.put("Previous_College_Graduation_Date__c", "");
        new_Application.put("Institute_Name__c", "");
        new_Application.put("SchoolState__c", "");
        new_Application.put("PermanentCity__c", "");
        new_Application.put("NonDegreeName_of_Award1__c", "");
        new_Application.put("NonDegreeDate_of_Award2__c", "");
        new_Application.put("NonDegreeArea_of_Study2__c", "");*/


        try {
            System.out.println("Application " + new_Application.toString());
            RestRequest request = RestRequest.getRequestForCreate(getString(R.string.api_version), ApplicationObjectType, new_Application);
            client.sendAsync(request, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, RestResponse response) {
                    try {
                        if (response == null) {

                            System.out.println("response is null");
                            return;

                        } else if (response.asJSONObject() == null) {
                            System.out.println("response.asJSONObject() is null");
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception exception) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readUserInput() {
    }

    public void cancelInvoked() {
        setResult(RESULT_OK);
        finish();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onResume(RestClient client) {
        JobActivity.this.client = client;
        displayForm();
        try {
            // displayForm();
            sendRequest_Terms("SELECT Id,Name FROM hed__Term__c");
            sendRequest_Program("SELECT Id,Name FROM Account");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest_Program(String soql) throws UnsupportedEncodingException {
        RestRequest restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                try {
                    program_name.clear();
                    program_id.clear();

                    JSONArray records = null;
                    records = result.asJSONObject().getJSONArray("records");
                    for (int i = 0; i < records.length(); i++) {
                        program_name.add(records.getJSONObject(i).getString("Name"));
                        program_id.add(records.getJSONObject(i).getString("Id"));


                        System.out.println("Accounts name" + records.getJSONObject(i).getString("Name"));
                        System.out.println("Accounts id" + records.getJSONObject(i).getString("Id"));
                        //System.out.println("Accounts "+hash_program.get("name"));
                    }
                    setProgramSpinner();
                } catch (Exception e) {
                    onError(e);
                }
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(JobActivity.this,
                        JobActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendRequest_Terms(String soql) throws UnsupportedEncodingException {
        RestRequest restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                try {
                    term_name.clear();
                    term_id.clear();

                    JSONArray records = null;
                    records = result.asJSONObject().getJSONArray("records");
                    for (int i = 0; i < records.length(); i++) {
                        term_name.add(records.getJSONObject(i).getString("Name"));
                        term_id.add(records.getJSONObject(i).getString("Id"));


                        System.out.println("Terms name" + records.getJSONObject(i).getString("Name"));
                        System.out.println("Terms id" + records.getJSONObject(i).getString("Id"));
                        //System.out.println("Accounts "+hash_program.get("name"));
                    }
                    setTermsSpinner();
                } catch (Exception e) {
                    onError(e);
                }
            }


            @Override
            public void onError(Exception exception) {
                Toast.makeText(JobActivity.this,
                        JobActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setProgramSpinner() {

        ArrayAdapter<String> program_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, program_name);
        program_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_program.setAdapter(program_dataAdapter);
    }

    private void setTermsSpinner() {

        ArrayAdapter<String> program_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, term_name);
        program_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_term.setAdapter(program_dataAdapter);
    }


    private void displayForm() {

        // et_fname,et_mname,et_lname,et_email,et_wEmail,et_mobile;

        et_fname = (EditText) findViewById(R.id.new_et_Firstname);
        et_mname = (EditText) findViewById(R.id.new_et_Mname);
        et_lname = (EditText) findViewById(R.id.new_et_LastNAme);
        et_email = (EditText) findViewById(R.id.new_et_email);
        et_wEmail = (EditText) findViewById(R.id.new_et_workEmail);
        et_mobile = (EditText) findViewById(R.id.new_et_mobile);


        et_fname.setText(client.getClientInfo().firstName);
        et_mname.setText("");
        et_lname.setText(client.getClientInfo().lastName);
        et_email.setText(client.getClientInfo().email);
        et_wEmail.setText("nizamuddeen@mstsolutions.com");
        et_mobile.setText("9994471706");

        sp_gender = (Spinner) findViewById(R.id.new_spin_gender);
        sp_marital = (Spinner) findViewById(R.id.new_spin_Marital);
        sp_term = (Spinner) findViewById(R.id.new_spin_Term);
        sp_program = (Spinner) findViewById(R.id.new_spin_Program);

        btn_apply = (Button) findViewById(R.id.new_btn_apply);
        btn_cancel = (Button) findViewById(R.id.new_btn_cancel);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doinBtnInvoked();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Spinner Drop down elements
        List<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("Female");


        // Spinner Drop down elements
        List<String> marital = new ArrayList<String>();
        marital.add("Married");
        marital.add("Divorced");
        marital.add("Widowed");
        marital.add("Seperated");


        // Creating adapter for spinner

        ArrayAdapter<String> gender_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        ArrayAdapter<String> marital_dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, marital);
        // Drop down layout style - list view with radio button
        gender_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marital_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_gender.setAdapter(gender_dataAdapter);
        sp_marital.setAdapter(marital_dataAdapter);


        // Spinner click listener
        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_gender = parent.getItemAtPosition(position).toString();


                System.out.println("str_gender selected name = " + str_gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner click listener
        sp_marital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_marital = parent.getItemAtPosition(position).toString();


                System.out.println("str_marital selected name = " + str_marital);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Spinner click listener
        sp_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                str_term = term_id.get(position);

                System.out.println("Terms selected name = " + item + " id= " + str_term);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Spinner click listener
        sp_program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                str_program = program_id.get(position);

                System.out.println("Account selected name = " + item + " id= " + str_program);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
