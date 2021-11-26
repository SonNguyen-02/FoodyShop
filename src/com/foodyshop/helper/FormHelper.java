/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import java.util.Random;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

/**
 *
 * @author WIN1064
 */
public abstract class FormHelper {

    public static final String EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String PASS_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
    
    public static void showErr(TextField control, Label lbErr, String textErr) {
        if (!lbErr.getText().trim().equals(textErr)) {
            control.setBorder(new Border(new BorderStroke(Paint.valueOf("RED"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            lbErr.setText(textErr);
        }
    }

    public static void resetErr(TextField control, Label lbErr) {
        if (!lbErr.getText().trim().equals("")) {
            control.setBorder(new Border(new BorderStroke(Paint.valueOf("#CECFD6"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            lbErr.setText("");
        }
    }

    public static void showErr(DatePicker control, Label lbErr, String textErr) {
        if (!lbErr.getText().trim().equals(textErr)) {
            control.setBorder(new Border(new BorderStroke(Paint.valueOf("RED"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            lbErr.setText(textErr);
        }
    }

    public static void resetErr(Control control, Label lbErr) {
        if (!lbErr.getText().trim().equals("")) {
            control.setBorder(new Border(new BorderStroke(Paint.valueOf("#CECFD6"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            lbErr.setText("");
        }
    }

    public static boolean IsEmptyField(TextField control, Label label, String textErr) {
        if (control.getText().trim().equals("")) {
            control.requestFocus();
            showErr(control, label, textErr);
            return true;
        }
        return false;
    }

    public static boolean IsEmptyField(Control control, Label label, String textErr) {
        if (control instanceof DatePicker) {
            if (((DatePicker) control).getValue() == null) {
                control.requestFocus();
                showErr(((DatePicker) control), label, textErr);
                return true;
            }
        }
        return false;
    }

    public static boolean isEmailField(TextField txtField, Label label, String textErr) {
        if (!txtField.getText().trim().matches(EMAIL_REGEX)) {
            txtField.requestFocus();
            showErr(txtField, label, textErr);
            return false;
        }
        return true;
    }
    
    public static boolean isPasswordField(TextField txtField, Label label, String textErr) {
        if (!txtField.getText().trim().matches(PASS_REGEX)) {
            txtField.requestFocus();
            showErr(txtField, label, textErr);
            return false;
        }
        return true;
    }

    /**
     *
     * @param fieldTyping dòng đang gõ
     * @param labErr lỗi dưới dòng đó
     * @param strErr error messenger
     * @param nextField dòng muốn focus sau khi gõ enter
     * @param event sự kiện bàn phím
     */
    public static void onEnter(TextField fieldTyping, Label labErr, String strErr, TextField nextField, KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.ENTER || keyCode == KeyCode.TAB) {
            if (!FormHelper.IsEmptyField(fieldTyping, labErr, strErr) && nextField != null) {
                nextField.requestFocus();
            }
        } else {
            FormHelper.resetErr(fieldTyping, labErr);
        }
    }

    public static String getRandomColor() {
        String[] letters = "0123456789ABCDEF".split("");
        StringBuilder color = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            color.append(letters[new Random().nextInt(16)]);
        }
        return color.toString();
    }

}
