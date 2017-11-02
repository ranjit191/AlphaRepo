package com.textmaxx.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.ViewUtil;
import com.textmaxx.retro.ApiClient;
import com.textmaxx.retro.ApiInterface;
import com.textmaxx.retro.ModelChangePas;
import com.textmaxx.retro.RegisterResponse;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.firstImage)
    ImageView firstImage;
    @Bind(R.id.secondImage)
    ImageView secondImage;
    @Bind(R.id.txt_title)
    TextView txt_title;
    //    @Bind(R.id.et_old_pas)
//    EditText oldPas;
    @Bind(R.id.et_new_pas)
    EditText newPas;
    @Bind(R.id.et_confirm_pas)
    EditText confPas;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    SharedPreferences prefs;
    String newpas1;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getString(SharedPreferenceConstants.ID, "");
//        Toast.makeText(ChangePassword.this, "id is " + id, Toast.LENGTH_SHORT).show();
        txt_title.setText("Change Password");
        firstImage.setVisibility(View.VISIBLE);
        firstImage.setImageResource(R.drawable.left_icon);
        firstImage.setEnabled(true);
        secondImage.setVisibility(View.VISIBLE);
        secondImage.setImageResource(R.drawable.left_icon);
        secondImage.setEnabled(true);

        confPas.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    submit();
                }
                return false;
            }
        });

        listener();
    }

    public void listener() {
        btSubmit.setOnClickListener(this);
        firstImage.setOnClickListener(this);
        secondImage.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == firstImage) {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        if (view == secondImage) {
            onBackPressed();
            finish();
        }
        if (view == btSubmit) {
            submit();
        }
    }

    public void submit() {
        newpas1 = newPas.getText().toString();
        String confirmpas1 = confPas.getText().toString();

        if (newpas1.equals("") || confirmpas1.equals("")) {

            Toast.makeText(ChangePassword.this, "Please enter the password", Toast.LENGTH_SHORT).show();
        } else if (!newpas1.equals(confirmpas1)) {
            Toast.makeText(ChangePassword.this, "Password Don't Match", Toast.LENGTH_SHORT).show();
        } else {
            jsonChangePas();
        }
    }

    public void jsonChangePas() {
        ViewUtil.showProgressDialog(ChangePassword.this);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        ModelChangePas task = new ModelChangePas(newpas1);
        Call<RegisterResponse> call = apiService.changePas("sms/user/" + user_id, "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim(), "application/json", "application/json", task);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                ViewUtil.hideProgressDialog();

                Toast.makeText(ChangePassword.this, "Password Changed", Toast.LENGTH_SHORT).show();


                try {

                    if (!response.isSuccessful()) {
                        try {

                            String bluff = response.errorBody().string().toString();
                            JSONObject json = new JSONObject(bluff);
                            Log.e("getresponse", "" + response.code());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                        Gson gson = new Gson();
                        String json = gson.toJson("" + response.body());

                        Log.e("getresponse", "" + response.code());
                        Log.e("getresponse", "" + json);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                ViewUtil.hideProgressDialog();
                // Log error here since request failed
                Log.e("error", t.toString());
            }
        });


    }


}
