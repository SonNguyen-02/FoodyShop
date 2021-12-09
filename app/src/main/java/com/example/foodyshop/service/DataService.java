package com.example.foodyshop.service;


import com.example.foodyshop.config.Const;
import com.example.foodyshop.model.CustomerModel;
import com.example.foodyshop.model.FeedbackModel;
import com.example.foodyshop.model.ProductModel;
import com.example.foodyshop.model.Respond;
import com.example.foodyshop.model.TopicModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

//    =========== Account =============

    @FormUrlEncoded
    @POST("login")
    Call<Respond> login(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<Respond> forgotPassword(@Field(Const.KEY_TOKEN) String token);


    @FormUrlEncoded
    @POST("change_password_otp")
    Call<Respond> changePasswordOTP(@Field(Const.KEY_TOKEN) String token,
                                    @Field(Const.KEY_NEW_PASSWORD) String newPassword);

    @FormUrlEncoded
    @POST("check_account_exists")
    Call<Respond> checkAccountExists(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("register")
    Call<Respond> register(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_user_info")
    Call<CustomerModel> getUserInfo(@Field(Const.KEY_TOKEN) String token);


//    =========== Data =============

    @FormUrlEncoded
    @POST("get_all_topic")
    Call<List<TopicModel>> getAllTopic(@Field(Const.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_total_page_product_in_topic")
    Call<Integer> getTotalPageProductInTopic(@Field(Const.KEY_TOKEN) String token,
                                             @Field(Const.KEY_TOPIC_ID) int topicId,
                                             @Field(Const.KEY_IS_SALE) boolean isSale);

    @FormUrlEncoded
    @POST("get_all_product_in_topic")
    Call<List<ProductModel>> getAllProductInTopic(@Field(Const.KEY_TOKEN) String token,
                                                  @Field(Const.KEY_TOPIC_ID) int topicId,
                                                  @Field(Const.KEY_CURRENT_PAGE) int currentPage,
                                                  @Field(Const.KEY_IS_SALE) boolean isSale);

    @FormUrlEncoded
    @POST("get_total_page_product_in_category")
    Call<Integer> getTotalPageProductInCategory(@Field(Const.KEY_TOKEN) String token,
                                                @Field(Const.KEY_CATEGORY_ID) int categoryId,
                                                @Field(Const.KEY_IS_SALE) boolean isSale);

    @FormUrlEncoded
    @POST("get_all_product_in_category")
    Call<List<ProductModel>> getAllProductInCategory(@Field(Const.KEY_TOKEN) String token,
                                                     @Field(Const.KEY_CATEGORY_ID) int categoryId,
                                                     @Field(Const.KEY_CURRENT_PAGE) int currentPage,
                                                     @Field(Const.KEY_IS_SALE) boolean isSale);

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

    //    =========== Order =============

    @FormUrlEncoded
    @POST("send_order")
    Call<Respond> sendOrder(@Field(Const.KEY_TOKEN) String token,
                            @Field(Const.KEY_ORDER) String orderJson,
                            @Field(Const.KEY_LIST_ORDER_DETAIL) String listOrderDetailJson);

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
