package com.example.foodyshop.service;


import com.example.foodyshop.config.Const;
import com.example.foodyshop.model.CustomerModel;
import com.example.foodyshop.model.FeedbackModel;
import com.example.foodyshop.model.OrderModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.model.TopicModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataService {

//    =========== Account =============

    @FormUrlEncoded
    @POST("login")
    Call<Respond> login(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("check_account_exists")
    Call<Respond> checkAccountExists(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("register")
    Call<Respond> register(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("change_password")
    Call<Respond> changePassword(@Field(Const.KEY_TOKEN) String token,
                                 @Field(Const.KEY_OLD_PASSWORD) String oldPassword,
                                 @Field(Const.KEY_NEW_PASSWORD) String newPassword);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<Respond> forgotPassword(@Field(Const.KEY_TOKEN) String token);


    @FormUrlEncoded
    @POST("change_password_otp")
    Call<Respond> changePasswordOTP(@Field(Const.KEY_TOKEN) String token,
                                    @Field(Const.KEY_NEW_PASSWORD) String newPassword);

    @FormUrlEncoded
    @POST("get_user_info")
    Call<CustomerModel> getUserInfo(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("change_user_info")
    Call<Respond> changeUserInfo(@Field(Const.KEY_TOKEN) String token,
                                 @Field(Const.KEY_DATA_USER) String userDataJson);

    @Multipart
    @POST("change_user_info")
    Call<Respond> changeUserInfo(@Part(Const.KEY_TOKEN) RequestBody requestToken,
                                 @Part(Const.KEY_DATA_USER) RequestBody requestUserData,
                                 @Part MultipartBody.Part multipartAvatar);

    @Multipart
    @POST("change_user_avatar")
    Call<Respond> changeUserAvatar(@Part(Const.KEY_TOKEN) RequestBody token,
                                   @Part MultipartBody.Part multipartAvatar);


//    =========== Data =============

    @FormUrlEncoded
    @POST("get_all_topic")
    Call<List<TopicModel>> getAllTopic(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_total_page_product_in_topic")
    Call<Integer> getTotalPageProductInTopic(@Field(Const.KEY_TOKEN) String token,
                                             @Field(Const.KEY_TOPIC_ID) int topicId,
                                             @Field(Const.KEY_IS_SALE) boolean isSale,
                                             @Field(Const.KEY_MIN_PRICE) int minPrice,
                                             @Field(Const.KEY_MAX_PRICE) int maxPrice,
                                             @Field(Const.KEY_SEARCH) String search);

    @FormUrlEncoded
    @POST("get_all_product_in_topic")
    Call<List<ProductModel>> getAllProductInTopic(@Field(Const.KEY_TOKEN) String token,
                                                  @Field(Const.KEY_TOPIC_ID) int topicId,
                                                  @Field(Const.KEY_CURRENT_PAGE) int currentPage,
                                                  @Field(Const.KEY_IS_SALE) boolean isSale,
                                                  @Field(Const.KEY_MIN_PRICE) int minPrice,
                                                  @Field(Const.KEY_MAX_PRICE) int maxPrice,
                                                  @Field(Const.KEY_SEARCH) String search,
                                                  @Field(Const.KEY_ORDER_PRICE) String orderSort);

    @FormUrlEncoded
    @POST("get_total_page_product_in_category")
    Call<Integer> getTotalPageProductInCategory(@Field(Const.KEY_TOKEN) String token,
                                                @Field(Const.KEY_CATEGORY_ID) int categoryId,
                                                @Field(Const.KEY_IS_SALE) boolean isSale,
                                                @Field(Const.KEY_MIN_PRICE) int minPrice,
                                                @Field(Const.KEY_MAX_PRICE) int maxPrice,
                                                @Field(Const.KEY_SEARCH) String search);

    @FormUrlEncoded
    @POST("get_all_product_in_category")
    Call<List<ProductModel>> getAllProductInCategory(@Field(Const.KEY_TOKEN) String token,
                                                     @Field(Const.KEY_CATEGORY_ID) int categoryId,
                                                     @Field(Const.KEY_CURRENT_PAGE) int currentPage,
                                                     @Field(Const.KEY_IS_SALE) boolean isSale,
                                                     @Field(Const.KEY_MIN_PRICE) int minPrice,
                                                     @Field(Const.KEY_MAX_PRICE) int maxPrice,
                                                     @Field(Const.KEY_SEARCH) String search,
                                                     @Field(Const.KEY_ORDER_PRICE) String orderSort);

    @FormUrlEncoded
    @POST("get_detail_product")
    Call<ProductModel> getDetailProduct(@Field(Const.KEY_TOKEN) String token,
                                        @Field(Const.KEY_PRODUCT_ID) int productId);

    @FormUrlEncoded
    @POST("get_total_feedback_in_product")
    Call<String> getTotalFeedbackFeedbackInProduct(@Field(Const.KEY_TOKEN) String token,
                                                   @Field(Const.KEY_PRODUCT_ID) int productId);

    @FormUrlEncoded
    @POST("get_feedback_in_product")
    Call<List<FeedbackModel>> getAllFeedbackInProduct(@Field(Const.KEY_TOKEN) String token,
                                                      @Field(Const.KEY_PRODUCT_ID) int productId,
                                                      @Field(Const.KEY_OFFSET) int offset);

    @FormUrlEncoded
    @POST("get_total_page_bought_product")
    Call<Integer> getTotalPageBoughtProduct(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_bought_product")
    Call<List<ProductModel>> getBoughtProduct(@Field(Const.KEY_TOKEN) String token,
                                              @Field(Const.KEY_CURRENT_PAGE) int currentPage);

    @FormUrlEncoded
    @POST("get_popular_product")
    Call<List<ProductModel>> getPopularProduct(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_total_page_suggest_product")
    Call<Integer> getTotalPageSuggestProduct(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_suggest_product")
    Call<List<ProductModel>> getSuggestProduct(@Field(Const.KEY_TOKEN) String token,
                                               @Field(Const.KEY_CURRENT_PAGE) int currentPage);

    @FormUrlEncoded
    @POST("get_total_page_search_product")
    Call<Integer> getTotalPageSearchProduct(@Field(Const.KEY_TOKEN) String token,
                                            @Field(Const.KEY_MIN_PRICE) int minPrice,
                                            @Field(Const.KEY_MAX_PRICE) int maxPrice,
                                            @Field(Const.KEY_SEARCH) String search);

    @FormUrlEncoded
    @POST("get_search_product")
    Call<List<ProductModel>> getSearchProduct(@Field(Const.KEY_TOKEN) String token,
                                              @Field(Const.KEY_CURRENT_PAGE) int currentPage,
                                              @Field(Const.KEY_MIN_PRICE) int minPrice,
                                              @Field(Const.KEY_MAX_PRICE) int maxPrice,
                                              @Field(Const.KEY_SEARCH) String search,
                                              @Field(Const.KEY_ORDER_PRICE) String orderSort);

    //    =========== Order =============

    @FormUrlEncoded
    @POST("get_list_order")
    Call<List<OrderModel>> getListOrder(@Field(Const.KEY_TOKEN) String token,
                                        @Field(Const.KEY_REQUEST_STATUS) String requestStatus);

    @FormUrlEncoded
    @POST("get_feedback_in_order")
    Call<OrderModel> getFeedbackInOrder(@Field(Const.KEY_TOKEN) String token,
                                        @Field(Const.KEY_ORDER) String orderJson);

    @FormUrlEncoded
    @POST("send_order")
    Call<Respond> sendOrder(@Field(Const.KEY_TOKEN) String token,
                            @Field(Const.KEY_ORDER) String orderJson);

    @FormUrlEncoded
    @POST("cancel_order")
    Call<Respond> cancelOrder(@Field(Const.KEY_TOKEN) String token,
                              @Field(Const.KEY_ORDER_ID) int orderId);

    @FormUrlEncoded
    @POST("confirm_buy")
        // 0 là hủy đơn, 1 là xác nhận
    Call<Respond> confirmBuy(@Field(Const.KEY_TOKEN) String token,
                             @Field(Const.KEY_ORDER_ID) int orderId);

    @FormUrlEncoded
    @POST("confirm_received")
        // 0 là hủy đơn, 1 là xác nhận
    Call<Respond> confirmReceived(@Field(Const.KEY_TOKEN) String token,
                                  @Field(Const.KEY_ORDER_ID) int orderId);

    @FormUrlEncoded
    @POST("add_feedback")
    Call<FeedbackModel> addFeedback(@Field(Const.KEY_TOKEN) String token,
                                    @Field(Const.KEY_FEEDBACK) String feedback);

    @FormUrlEncoded
    @POST("edit_feedback")
    Call<Respond> editFeedback(@Field(Const.KEY_TOKEN) String token,
                               @Field(Const.KEY_FEEDBACK_ID) int feedbackId,
                               @Field(Const.KEY_CONTENT) String content);

    @FormUrlEncoded
    @POST("delete_feedback")
    Call<Respond> deleteFeedback(@Field(Const.KEY_TOKEN) String token,
                                 @Field(Const.KEY_FEEDBACK_ID) int feedbackId);


}
