package com.example.foodyshop.helper;

import static android.content.Context.MODE_PRIVATE;

import static com.example.foodyshop.config.Const.KEY_TOKEN_LOGIN;
import static com.example.foodyshop.config.Const.KEY_USER_OBJ;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.foodyshop.config.Const;
import com.example.foodyshop.model.CustomerModel;
import com.example.foodyshop.service.APIService;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Helper {

    public static CustomerModel currentAccount;

    public static void showKeyboard(@NonNull Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @NonNull
    public static String getTokenLogin(@NonNull SharedPreferences sharedPreferences){
        return sharedPreferences.getString(Const.KEY_TOKEN_LOGIN, "").trim();
    }

    public static boolean isLogin(@NonNull SharedPreferences sharedPreferences) {
        String token = getTokenLogin(sharedPreferences);
        Log.e("ddd", "isLogin: Token: " + token );
        if (!token.isEmpty()) {
            Jws<Claims> jws = JWT.decodeToken(token);
            return jws != null;
        }
        return false;
    }

    public static void saveUserInfo(@NonNull SharedPreferences sharedPreferences, String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        APIService.getService().getUserInfo(token).enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    Gson gson = new Gson();
                    String customerJson = gson.toJson(response.body());
                    editor.putString(KEY_USER_OBJ, customerJson);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
            }
        });
    }

    public static CustomerModel getUserInfo(@NonNull SharedPreferences sharedPreferences){
        String customerJson = sharedPreferences.getString(KEY_USER_OBJ, "");
        Gson gson = new Gson();
        return gson.fromJson(customerJson, CustomerModel.class);
    }

    public static void logOut(@NonNull SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN_LOGIN);
        editor.remove(KEY_USER_OBJ);
        editor.apply();
    }
}
