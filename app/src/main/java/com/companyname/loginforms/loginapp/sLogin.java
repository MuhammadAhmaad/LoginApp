package com.companyname.loginforms.loginapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class sLogin extends AppCompatActivity {

    private EditText tiLoginNickName;
    private EditText tiLoginPassword;
    private EditText tiLoginMobileNo;
    private TextView toLoginMsgs;
    private Button bSignIn;
    private Button bSignUp;
    private boolean signUpFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_login);
        tiLoginNickName = (EditText) findViewById(R.id.ti_login_nick_name);
        tiLoginPassword = (EditText) findViewById(R.id.ti_login_password);
        tiLoginMobileNo = (EditText) findViewById(R.id.ti_login_Mobile);
        toLoginMsgs = (TextView) findViewById(R.id.to_login_msgs);
        bSignIn = (Button) findViewById(R.id.b_login_sign_in);
        bSignUp = (Button) findViewById(R.id.b_login_sign_up);
        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputs()) {
                    signUpFlag = false;
                    new GetData().execute();
                }
            }
        });
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputs()) {
                    signUpFlag = true;
                    new GetData().execute();
                }
            }
        });
    }

    private boolean checkInputs() {
        String name = tiLoginNickName.getText().toString();
        String mobile = tiLoginMobileNo.getText().toString();
        String password = tiLoginPassword.getText().toString();
        boolean flag=true;
        if (name.equals("")) {
            tiLoginNickName.setError("Please enter a NickName");
            flag= false;
        }
        if (mobile.equals("")|| mobile.length() != 11 ||!mobile.matches("[0-9]+")) {
            tiLoginMobileNo.setError("Please enter a valid mobile number");
            flag= false;
        }
        if(password.equals("")||password.length()<6)
        {
            tiLoginPassword.setError("Password Must be at least 6 characters");
            flag = false;
        }

        return flag;
    }

    public class GetData extends AsyncTask<Void, Void, Void> {

        ArrayList<User> list = new ArrayList<>();
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(sLogin.this);
            dialog.setMessage("Loading");
            dialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonObject = JsonParser.getDataFromWeb();

            try {

                if (jsonObject != null) {

                    if (jsonObject.length() > 0) {

                        JSONArray array = jsonObject.getJSONArray("DataBase");

                        int lenArray = array.length();
                        if (lenArray > 0) {
                            for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                                User user = new User();

                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String name = innerObject.getString("tiLoginNickName");
                                String mobile = innerObject.getString("tiLoginMobileNo");
                                String password = innerObject.getString("tiLoginPassord");
                                user.setNickName(name);
                                user.setMobileNo(mobile);
                                user.setLoginPassword(password);
                                list.add(user);
                            }
                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(JsonParser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            boolean flag1 = false;
            if (list.size() == 0) {
                toLoginMsgs.setText("please input correct data");
            } else {
                String mobile = tiLoginMobileNo.getText().toString().toLowerCase();
                String nickName = tiLoginNickName.getText().toString().toLowerCase();
                String password = tiLoginPassword.getText().toString().toLowerCase();
                boolean flag = false;
                for (User x : list) {
                    if (mobile.equals(x.getMobileNo().toLowerCase())) {
                        flag1 = true;
                        if (nickName.equals(x.getNickName().toLowerCase())) {
                            if (password.equals(x.getLoginPassword().toLowerCase())) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
                if (signUpFlag) {
                    if (flag1)
                        toLoginMsgs.setText("Already Registered");
                    else {
                        new SendRequest().execute();
                    }
                } else if (flag) {
                    toLoginMsgs.setText("SignIn Ok");
                } else {

                    toLoginMsgs.setText("please input correct data");
                }
            }
        }
    }

    public class SendRequest extends AsyncTask<String, Void, String> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(sLogin.this);
            dialog.setMessage("Loading");
            dialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://script.google.com/macros/s/AKfycbwlB_pedXeYl31XcQEhVNAgkE62MwWw1nXuET7rdEH9bEyyKBsI/exec");

                JSONObject postDataParams = new JSONObject();

                String id = "1mJY7USq2mpNjv8C23W5SViW4lZWjipWyCEKl9lPrvXY";

                postDataParams.put("tiLoginNickName", tiLoginNickName.getText().toString());
                postDataParams.put("tiLoginMobileNo", tiLoginMobileNo.getText().toString());
                postDataParams.put("tiLoginPassord", tiLoginPassword.getText().toString());
                postDataParams.put("id", id);


                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals("\"Success\""))
                toLoginMsgs.setText("SignUp Ok");

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
