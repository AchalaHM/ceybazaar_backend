package com.CeyBazaar.backend.util;

public class Constants {

    public static final Integer SUCCESS = 1000;
    public static final String UPLOAD_DIRECTORY = "/uploads";//"/home/ec2-user/uploads"
    public static final Integer ALREADY_EXIST = 1001;
    public static final Integer NOT_FOUND = 1003;
    public static final Integer RUNTIME_EXCEPTION = 1002;
    public static final String UNPAID = "UNPAID";

    public static final String MERCHANT_ID = "1230119";
    public static final String MERCHANT_SECRET = "MzUxMzQwOTA5MDE0MTgzMTQxODQxNjg0NzUyMDgxMzY3OTY2NjEw";

    public static final String PAID = "PAID";
    public static final String COMPLETED = "COMPLETED";
    
    // Delivery Status Constants
    public static final String DELIVERY_PENDING = "PENDING";
    public static final String DELIVERY_PROGRESSING = "PROGRESSING";
    public static final String DELIVERY_DELIVERED = "DELIVERED";
}
