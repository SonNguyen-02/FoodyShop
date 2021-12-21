package com.example.foodyshop.activity;

import static com.example.foodyshop.config.Const.KEY_ADDRESS;
import static com.example.foodyshop.config.Const.KEY_NAME;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodyshop.R;
import com.example.foodyshop.config.Const;
import com.example.foodyshop.dialog.ChooseGenderDialog;
import com.example.foodyshop.dialog.ConfirmDialog;
import com.example.foodyshop.dialog.DatePickerSpinnerDialog;
import com.example.foodyshop.dialog.LoadingDialog;
import com.example.foodyshop.dialog.ShowOrChangeAvatarBottomSheet;
import com.example.foodyshop.dialog.ToastCustom;
import com.example.foodyshop.fragment.ShowAvatarFragment;
import com.example.foodyshop.helper.Helper;
import com.example.foodyshop.model.CustomerModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.service.APIService;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;
import java.util.Random;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import gun0912.tedbottompicker.util.RealPathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends AppCompatActivity {

    private ImageView imgBack, imgSave, imgAvatar;
    private RelativeLayout rlAvatar, rlCustomerName, rlPhone, rlPassword, rlGender, rlDateBirth, rlAddress;
    private TextView tvCustomerName, tvPhone, tvGender, tvDateBirth, tvAddress;
    private Uri mUri;
    private CustomerModel mUserData;
    private long lastTimeClick;

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        String name = intent.getStringExtra(KEY_NAME);
                        if (name != null && !name.isEmpty()) {
                            mUserData.setName(name);
                            tvCustomerName.setText(name);
                        }
                        String address = intent.getStringExtra(KEY_ADDRESS);
                        if (address != null && !address.isEmpty()) {
                            mUserData.setAddress(address);
                            tvAddress.setText(address);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mUserData = new CustomerModel();

        initUi();
        CustomerModel currentAccount = Helper.getCurrentAccount();

        Glide.with(getApplicationContext()).load(currentAccount.getImg())
                .placeholder(R.drawable.placeholder_user)
                .error(R.drawable.placeholder_user).into(imgAvatar);

        tvCustomerName.setText(currentAccount.getName());
        tvPhone.setText(currentAccount.getPhoneHide());
        tvGender.setText(currentAccount.getGenderString(this));
        tvDateBirth.setText(currentAccount.getDateBirthLocal());

        String address = currentAccount.getAddress();
        if (address == null || address.isEmpty()) {
            address = "Chưa có địa chỉ";
        }
        tvAddress.setText(address);

        imgBack.setOnClickListener(view -> {
            onBackPressed();
        });

        imgSave.setOnClickListener(view -> {
            saveUserInfo();
        });

        rlAvatar.setOnClickListener(view -> {
            ShowOrChangeAvatarBottomSheet.newInstant(new ShowOrChangeAvatarBottomSheet.IOnClickItem() {
                @Override
                public void onClickShowAvatar(ShowOrChangeAvatarBottomSheet bottomSheet) {
                    bottomSheet.dismiss();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame_root, ShowAvatarFragment.newInstance(mUri))
                            .setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_out)
                            .addToBackStack(ShowAvatarFragment.class.getName())
                            .commit();
                }

                @Override
                public void onClickChangeAvatar(ShowOrChangeAvatarBottomSheet bottomSheet) {
                    bottomSheet.dismiss();
                    requestPermission();
                }
            }).show(getSupportFragmentManager(), ShowOrChangeAvatarBottomSheet.class.getName());
        });

        rlCustomerName.setOnClickListener(view -> {
            if (System.currentTimeMillis() - lastTimeClick < 1000) {
                return;
            }
            lastTimeClick = System.currentTimeMillis();
            Intent intent = new Intent(this, EditNameActivity.class);
            String name = mUserData.getName();
            if (name == null || name.isEmpty()) {
                name = Helper.getCurrentAccount().getName();
            }
            intent.putExtra(KEY_NAME, name);
            mActivityResultLauncher.launch(intent);
        });

        rlPhone.setOnClickListener(view -> {
            if (System.currentTimeMillis() - lastTimeClick < 1500) {
                return;
            }
            lastTimeClick = System.currentTimeMillis();
            ToastCustom.notice(this, "Hiện tại chưa thể cập nhập\nsố điện thoại", ToastCustom.WARNING).show();
        });

        rlPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditPasswordActivity.class);
            startActivity(intent);
        });

        rlGender.setOnClickListener(view -> {
            if (System.currentTimeMillis() - lastTimeClick < 1000) {
                return;
            }
            lastTimeClick = System.currentTimeMillis();
            ChooseGenderDialog.newInstance(this, (dialog, typeChoose) -> {
                dialog.dismiss();
                mUserData.setGender(typeChoose);
                tvGender.setText(mUserData.getGenderString(this));
            }).show();
        });


        rlDateBirth.setOnClickListener(view -> {
            if (System.currentTimeMillis() - lastTimeClick < 1000) {
                return;
            }
            lastTimeClick = System.currentTimeMillis();
            String defaultDate = mUserData.getDatebirth();
            if (defaultDate == null) {
                defaultDate = Helper.getCurrentAccount().getDatebirth();
            }
            DatePickerSpinnerDialog.newInstance(this, (datePicker, year, month, day) -> {
                String days = (day > 9 ? String.valueOf(day) : "0" + day);
                String months = (month > 9 ? String.valueOf(month) : "0" + month);
                String date = year + "-" + months + "-" + days;

                String dateBirthLocal = days + "/" + months + "/" + year;
                mUserData.setDatebirth(date);
                tvDateBirth.setText(dateBirthLocal);
                datePicker.dismiss();
            }, defaultDate).show();
        });

        rlAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditAddressActivity.class);
            String defAdress = mUserData.getAddress();
            if (defAdress == null || defAdress.isEmpty()) {
                defAdress = Helper.getCurrentAccount().getAddress();
            }
            intent.putExtra(KEY_ADDRESS, defAdress);
            mActivityResultLauncher.launch(intent);
        });
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        imgSave = findViewById(R.id.img_save);
        imgAvatar = findViewById(R.id.img_avatar);
        rlAvatar = findViewById(R.id.rl_avatar);
        rlCustomerName = findViewById(R.id.rl_customer_name);
        rlPhone = findViewById(R.id.rl_phone);
        rlPassword = findViewById(R.id.rl_password);
        rlGender = findViewById(R.id.rl_gender);
        rlDateBirth = findViewById(R.id.rl_date_birth);
        rlAddress = findViewById(R.id.rl_address);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvGender = findViewById(R.id.tv_gender);
        tvDateBirth = findViewById(R.id.tv_date_birth);
        tvAddress = findViewById(R.id.tv_address);
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(@NonNull List<String> deniedPermissions) {
                Toast.makeText(MyAccountActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void openImagePicker() {
        TedBottomSheetDialogFragment.OnImageSelectedListener listener = imgUri -> {
            if (imgUri != null) {
                startCrop(imgUri);
            }
        };
        TedBottomPicker.with(MyAccountActivity.this)
                .setOnImageSelectedListener(listener)
                .create()
                .show(getSupportFragmentManager());
    }

    @NonNull
    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(70);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        options.setToolbarWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setToolbarTitle("Crop Image");
        return options;
    }

    private void startCrop(@NonNull Uri uri) {
        Random random = new Random();
        String destinationFileName = System.currentTimeMillis() + "" + random.nextInt(100000);
        destinationFileName += ".jpg";
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(300, 300)
                .withOptions(getCropOption())
                .start(MyAccountActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            Uri imgUriRequestCrop = UCrop.getOutput(data);
            if (imgUriRequestCrop != null) {
                releaseCacheUri();
                mUri = imgUriRequestCrop;
                imgAvatar.setImageURI(imgUriRequestCrop);
            }
        }
    }

    private void releaseCacheUri() {
        if (mUri != null) {
            String realPath = RealPathUtil.getRealPath(this, mUri);
            File file = new File(realPath);
            if (file.exists()) {
                if (file.delete()) {
                    Log.e("ddd", "releaseCacheUri: clear cache " + file.getName());
                }
            }
            deleteFile(mUri.getPath().substring(mUri.getPath().lastIndexOf("/") + 1));
            mUri = null;
        }
    }

    private void saveUserInfo() {
        if (System.currentTimeMillis() - lastTimeClick < 1000) {
            return;
        }
        lastTimeClick = System.currentTimeMillis();
        if (mUserData.isNotMatch(Helper.getCurrentAccount()) || mUri != null) {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
            Gson gson = new Gson();
            String userDataJson = gson.toJson(mUserData);
            if (mUri == null) {
                APIService.getService().changeUserInfo(Helper.getTokenLogin(getApplicationContext()), userDataJson).enqueue(new Callback<Respond>() {
                    @Override
                    public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Respond res = response.body();
                            if (res.isSuccess()) {
                                mUserData = new CustomerModel();
                                Helper.saveUserInfo(getApplicationContext(), gson.fromJson(response.body().getMsg(), CustomerModel.class));
                                ToastCustom.notice(getApplicationContext(), "Thay đổi thành công!", ToastCustom.SUCCESS).show();
                            } else {
                                ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                            }
                        } else {
                            ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                        dialog.dismiss();
                        ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                    }
                });
            } else {
                RequestBody requestToken = RequestBody.create(MediaType.parse("multipart/form-data"), Helper.getTokenLogin(getApplicationContext()));
                RequestBody requestUserData = RequestBody.create(MediaType.parse("multipart/form-data"), userDataJson);

                String realPath = RealPathUtil.getRealPath(this, mUri);
                File file = new File(realPath);
                RequestBody requestAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part multipartAvatar = MultipartBody.Part.createFormData(Const.KEY_AVATAR_USER, file.getName(), requestAvatar);

                APIService.getService().changeUserInfo(requestToken, requestUserData, multipartAvatar).enqueue(new Callback<Respond>() {
                    @Override
                    public void onResponse(@NonNull Call<Respond> call, @NonNull Response<Respond> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Respond res = response.body();
                            if (res.isSuccess()) {
                                releaseCacheUri();
                                mUserData = new CustomerModel();
                                Helper.saveUserInfo(getApplicationContext(), gson.fromJson(response.body().getMsg(), CustomerModel.class));
                                ToastCustom.notice(getApplicationContext(), "Thay đổi thành công!", ToastCustom.SUCCESS).show();
                            } else {
                                ToastCustom.notice(getApplicationContext(), res.getMsg(), ToastCustom.ERROR).show();
                            }
                        } else {
                            ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Respond> call, @NonNull Throwable t) {
                        dialog.dismiss();
                        ToastCustom.notice(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng!", ToastCustom.INFO).show();
                    }
                });
            }
        } else {
            ToastCustom.notice(getApplicationContext(), "Chưa có thay đổi!", ToastCustom.WARNING).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (mUserData.isNotMatch(Helper.getCurrentAccount()) || mUri != null) {
            ConfirmDialog.newInstance(this, "Hủy thay đổi?", confirmDialog -> {
                confirmDialog.dismiss();
                finish();
            }).show();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCacheUri();
    }
}