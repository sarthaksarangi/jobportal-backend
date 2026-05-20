package com.jobportal.backend.constants;

public class ApplicationConstants {

    private ApplicationConstants(){
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "VeryStrongSecret";
    public static final String JWT_HEADER = "Authorization";
}
