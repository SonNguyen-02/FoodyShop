/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.main;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.foodyshop.controller.MainController;
import com.foodyshop.model.Respond;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author N.C.Son
 */
public class APIService {

    private static final String PRIVATE_KEY = "Rm9PZHlTaG9QX3NlY3JldEtleVl5KiMxMjA0JV4mNDUxODgxIyEhMjI0NWRzZEREIzJFZGVk";

    public static Respond uploadImageToApi(File file, String type, String lastImgName) throws IOException {

        URL url = new URL(Config.API_URL + "upload_image");

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(Const.KEY_TOKEN, createToken());
        params.put(Const.KEY_TYPE, type);
        params.put(Const.KEY_BASE64_IMAGE, encodeFileToBase64Binary(file));
        if (lastImgName != null) {
            params.put(Const.KEY_LAST_IMG_NAME, lastImgName);
        }
        String result = call(url, params);
        Gson gson = new Gson();
        return gson.fromJson(result, Respond.class);
    }

    public static Respond uploadImageToApi(File file, String type) throws IOException {
        return uploadImageToApi(file, type, null);
    }

    public static Respond removeImageFromApi(String type, String imgName) throws IOException {

        URL url = new URL(Config.API_URL + "remove_image");

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(Const.KEY_TOKEN, createToken());
        params.put(Const.KEY_TYPE, type);
        params.put(Const.KEY_IMG_NAME, imgName);

        String result = call(url, params);
        Gson gson = new Gson();
        return gson.fromJson(result, Respond.class);
    }
    public static String createToken() {
        try {
            String jwt = JWT.create()
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000))
                    .withIssuedAt(new Date())
                    .sign(Algorithm.HMAC256(Base64.getDecoder().decode(PRIVATE_KEY)));
            return jwt;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    private static String encodeFileToBase64Binary(File file) throws IOException {
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes), "UTF-8");
    }

    private static String call(URL url, Map<String, Object> params) throws IOException {
        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append("&");
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append("=");
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataByte = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataByte);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            sb.append((char) c);
        }
        return sb.toString();
    }
}
