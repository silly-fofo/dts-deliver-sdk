package com.aliyun.dts.deliver.commons.openapi;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class DtsOpenApiTest {
    @Test
    public void testGetUserPassword() throws Exception {
        DtsOpenApi dtsOpenApi = new DtsOpenApi("", "",
                "", "cn-hangzhou");

        Pair<String, String> userPassword = dtsOpenApi.getUserPassword();

        System.out.println("userPassword: " + userPassword);
    }
}
