/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.main;

/**
 *
 * @author N.C.Son
 */
public class Config {

    // for database
    public static final String HOSTNAME = "foodyshop.tk";
    public static final String USERNAME = "sonnctvk_foody_shop";
    public static final String PASSWORD = "VItGY{a7]0CIRDz{VQ";
    public static final String DATABASE = "sonnctvk_foody_shop";
    
    // for api
    public static final String BASE_URL = "https://foodyshop.tk/FoodyShop/";
//    public static final String BASE_URL = "https://foodyshop.000webhostapp.com/FoodyShop/";
//    public static final String BASE_URL = "http://localhost/FoodyShop/";
    public static final String BASE_API = BASE_URL + "api/";
    private static final String IMG_FOLDER = "public/images/upload/";
    public static final String IMG_FOOD_DIR = BASE_URL + IMG_FOLDER + "foods/";
    public static final String IMG_TOPIC_DIR = BASE_URL + IMG_FOLDER + "topic/";
    public static final String IMG_SALE_DIR = BASE_URL + IMG_FOLDER + "sale/";
    public static final String IMG_AVATAR_DIR = BASE_URL + IMG_FOLDER + "avatar/";
    
}
