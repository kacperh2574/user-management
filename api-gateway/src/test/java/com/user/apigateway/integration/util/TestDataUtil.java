package com.user.apigateway.integration.util;

public class TestDataUtil {

    static public String createLoginPayload(final String email) {
        return String.format("""
            {
              "email": "%s",
              "password": "TestPassword123"
            }
        """, email);
    }
}
