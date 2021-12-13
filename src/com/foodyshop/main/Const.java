/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.main;

import java.nio.file.Paths;
import javafx.scene.image.Image;

/**
 *
 * @author N.C.Son
 */
public class Const {

    public static final String KEY_TOKEN = "token";
    public static final String KEY_TYPE = "type";
    public static final String KEY_BASE64_IMAGE = "base64_image";
    public static final String KEY_LAST_IMG_NAME = "last_img_name";
    public static final String KEY_IMG_NAME = "img_name";
    
    public static final String TYPE_FOOD = "type_food";
    public static final String TYPE_TOPIC = "type_topic";
    public static final String TYPE_SALE = "type_sale";
    public static final String PLACEHOLDER_NO1_IMG_PATH = Paths.get(".").toAbsolutePath().getParent().toString() + "\\src\\public\\image\\main_layout\\placeholder_imgDetail.jpg";
    public static final String PLACEHOLDER_NO_IMG_PATH = Paths.get(".").toAbsolutePath().getParent().toString() + "\\src\\public\\image\\main_layout\\placeholder_img.jpg";
    public static final String PLACEHOLDER_USER_IMG_PATH = Paths.get(".").toAbsolutePath().getParent().toString() + "\\src\\public\\image\\main_layout\\placeholder_user.jpg";
    
    public static final Image NO_IMAGE_OBJ = new Image("file:" + Const.PLACEHOLDER_NO_IMG_PATH, 100, 100, false, true);
}
