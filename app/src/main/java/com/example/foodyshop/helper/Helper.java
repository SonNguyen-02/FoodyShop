package com.example.foodyshop.helper;

import static android.content.Context.MODE_PRIVATE;

import static com.example.foodyshop.config.Const.KEY_TOKEN_LOGIN;
import static com.example.foodyshop.config.Const.KEY_USER_CART;
import static com.example.foodyshop.config.Const.KEY_USER_OBJ;
import static com.example.foodyshop.config.Const.KEY_USER_PREFERENCES;
import static com.example.foodyshop.config.Const.TOAST_DEFAULT;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.model.CustomerModel;
import com.example.foodyshop.model.OrderDetailModel;
import com.example.foodyshop.service.APIService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Helper {

    private static CustomerModel currentAccount;

    public static void setCurrentAccount(CustomerModel currentAccount) {
        Helper.currentAccount = currentAccount;
    }

    public static CustomerModel getCurrentAccount() {
        return currentAccount;
    }

    public static void showKeyboard(@NonNull Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @NonNull
    public static String convertListOrderDetailToJson(List<OrderDetailModel> listOrderDetail) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(listOrderDetail).getAsJsonArray();
        return jsonArray.toString();
    }

    @NonNull
    public static List<OrderDetailModel> convertJsonToListOrderDetail(String strJson) {
        List<OrderDetailModel> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            JSONObject jsonObject;
            OrderDetailModel orderDetail;
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                orderDetail = gson.fromJson(jsonObject.toString(), OrderDetailModel.class);
                list.add(orderDetail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void addOrChangeAmountOrderDetail(Context context, OrderDetailModel mOrderDetail, boolean isAdd) {
        List<OrderDetailModel> listOrderDetail = getAllOrderDetailCart(context);
        if (!listOrderDetail.isEmpty()) {
            boolean isExists = false;
            for (OrderDetailModel ord : listOrderDetail) {
                if (mOrderDetail.getProductId() == ord.getProductId()) {
                    if (isAdd) {
                        ord.setNumber(ord.getNumber() + mOrderDetail.getNumber());
                    } else {
                        ord.setNumber(mOrderDetail.getNumber());
                    }
                    isExists = true;
                    break;
                }
            }
            if (!isExists && isAdd) {
                listOrderDetail.add(mOrderDetail);
            }
        } else {
            listOrderDetail = new ArrayList<>(Collections.singletonList(mOrderDetail));
        }
        saveCart(context, listOrderDetail);
    }

    public static void removeOrderDetailFromCart(Context context, OrderDetailModel mOrderDetail) {
        List<OrderDetailModel> listOrderDetail = getAllOrderDetailCart(context);
        if (!listOrderDetail.isEmpty()) {
            OrderDetailModel tmpOrd = null;
            for (OrderDetailModel ord : listOrderDetail) {
                if (mOrderDetail.getProductId() == ord.getProductId()) {
                    tmpOrd = ord;
                    break;
                }
            }
            if (tmpOrd != null) {
                listOrderDetail.remove(tmpOrd);
            }
            saveCart(context, listOrderDetail);
        }
    }

    public static void clearCart(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_USER_CART);
        editor.apply();
    }

    @NonNull
    public static List<OrderDetailModel> getAllOrderDetailCart(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String listOrderDetailJson = sharedPreferences.getString(KEY_USER_CART, "");
        return convertJsonToListOrderDetail(listOrderDetailJson);
    }

    public static void saveCart(Context context, List<OrderDetailModel> listOrderDetail) {
        saveCart(getSharedPreferences(context), listOrderDetail);
    }

    private static void saveCart(@NonNull SharedPreferences sharedPreferences, List<OrderDetailModel> listOrderDetail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_CART, convertListOrderDetailToJson(listOrderDetail));
        editor.apply();
    }

    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(KEY_USER_PREFERENCES, MODE_PRIVATE);
    }

    @NonNull
    public static String getTokenLogin(Context context) {
        return getSharedPreferences(context).getString(Const.KEY_TOKEN_LOGIN, "").trim();
    }

    public static boolean isLogin(Context context) {
        String token = getTokenLogin(context);
        Log.e("ddd", "isLogin: Token: " + token);
        if (!token.isEmpty()) {
            Jws<Claims> jws = JWT.decodeToken(token);
            return jws != null;
        }
        return false;
    }

    public static void saveUserInfo(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        APIService.getService().getUserInfo(token).enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentAccount = response.body();
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

    public static CustomerModel getUserInfo(Context context) {
        String customerJson = getSharedPreferences(context).getString(KEY_USER_OBJ, "");
        Gson gson = new Gson();
        return gson.fromJson(customerJson, CustomerModel.class);
    }

    public static void logOut(Context context) {
        currentAccount = null;
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_TOKEN_LOGIN);
        editor.remove(KEY_USER_OBJ);
        editor.apply();
    }

    public static boolean isInvalidPassword(Context context, @NonNull EditText edtPassword, boolean isConfirmPass) {
        String password = edtPassword.getText().toString().trim();
        if (password.isEmpty()) {
            edtPassword.requestFocus();
            if (isConfirmPass) {
                ToastCustom.notice(context, "Vui lòng nhập lại mật khẩu", ToastCustom.WARNING, TOAST_DEFAULT).show();
            } else {
                ToastCustom.notice(context, "Vui lòng nhập mật khẩu", ToastCustom.WARNING, TOAST_DEFAULT).show();
            }
            return true;
        }
        if (password.length() < 8) {
            edtPassword.requestFocus();
            ToastCustom.notice(context, "Mật khẩu tối thiểu 8 kí tự", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return true;
        }
        if (!password.matches(Const.PASSWORD_REGEX)) {
            edtPassword.requestFocus();
            ToastCustom.notice(context, "Mật khẩu cần có ít nhất 1 chữ và 1 số", ToastCustom.WARNING, TOAST_DEFAULT).show();
            return true;
        }
        return false;
    }
}
