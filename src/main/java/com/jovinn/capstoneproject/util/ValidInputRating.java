package com.jovinn.capstoneproject.util;

import com.jovinn.capstoneproject.exception.JovinnException;
import org.springframework.http.HttpStatus;

public class ValidInputRating {
    public static Integer getInputRating(Integer value) {
        try {
            if(value > 5) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Phải nhập nhỏ hơn hoặc bằng 5");
            } else {
                return value;
            }
        } catch(NumberFormatException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Phải nhập nhỏ hơn hoặc bằng 5");
        }
    }
}
