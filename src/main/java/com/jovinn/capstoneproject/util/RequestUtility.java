package com.jovinn.capstoneproject.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtility {
    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(),"");
    }
}
