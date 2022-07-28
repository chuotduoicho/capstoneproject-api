package com.jovinn.capstoneproject.util;

import com.jovinn.capstoneproject.exception.JovinnException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebConstant {
    public static String DOMAIN = "http://localhost:3000";
    public static final int MAX_PAGE_SIZE = 30;
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "createAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
}
