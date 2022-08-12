package com.jovinn.capstoneproject.util;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class DateDelivery {
    public Date expectDate(Integer expectDeliveryDate, Integer totalDeliveryTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(expectDeliveryDate, totalDeliveryTime);
        return cal.getTime();
    }
}
