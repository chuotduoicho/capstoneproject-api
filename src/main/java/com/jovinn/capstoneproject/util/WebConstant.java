package com.jovinn.capstoneproject.util;

import org.springframework.stereotype.Component;

@Component
public class WebConstant {
    public static String DOMAIN = "https://jovinn-fsm.web.app";
    public static final int MAX_PAGE_SIZE = 30;
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "12";
    public static final String DEFAULT_SORT_BY = "createAt";
    public static final String DEFAULT_BOX_SORT_BY = "totalFinalContract";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
    public static final String MIN_PRICE = "0";
    public static final String MAX_PRICE = "10000";
    public static final String NOT_FOUND_BOX = "Không tìm thấy dịch vụ";
    public static final String DEFAULT_BOX_STATUS = "ACTIVE";
}
