package com.goose.calculator.config;

public final class RuntimeMSG {
    public static final String SUCCESS = "Success - The operation was successfully performed";
    public static final String BAD_REQUEST = "Bad request - Some parameters were invalid";
    public static final String NOT_FOUND = "Not Found - Resource not available";
    public static final String INTERNAL_ERROR = "Internal server error";

    public static final String SUCCESS_MESSAGE = "{\n  \"code\": 200,\n  \"type\": \"SUCCESS\",\n  \"body\": \"string\"\n}";
    public static final String ERROR_404 = "{\n  \"code\": 404,\n  \"type\": \"NOT FOUND\",\n  \"body\": \"string\"\n}";
    public static final String ERROR_500 = "{\n  \"code\": 500,\n  \"type\": \"INTERNAL SERVER ERROR\",\n  \"body\": \"string\"\n}";
    public static final String ERROR_400 = "{\n  \"code\": 400,\n  \"type\": \"BAD REQUEST\",\n  \"body\": \"string\"\n}";

}
