package com.jobportal.backend.constants;

public class ApplicationConstants {

    private ApplicationConstants(){
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "VeryStrongSecretVeryStrongSecretVeryStrongSecret";
    public static final String JWT_HEADER = "Authorization";
    public static final String ROLE_JOB_SEEKER = "ROLE_JOB_SEEKER";

}
