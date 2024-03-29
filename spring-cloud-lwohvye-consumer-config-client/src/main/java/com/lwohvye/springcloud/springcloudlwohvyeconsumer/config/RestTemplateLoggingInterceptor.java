package com.lwohvye.springcloud.springcloudlwohvyeconsumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    static Logger LOGGER = LoggerFactory.getLogger(RestTemplateLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex) throws IOException {
        var requestBody = new String(reqBody, StandardCharsets.UTF_8);
        LOGGER.info("Request: {} {} {}", req.getMethod(), req.getURI(), requestBody);
        ClientHttpResponse response = ex.execute(req, reqBody);
//        if (LOGGER.isDebugEnabled()) {
        InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
        String body = new BufferedReader(isr)
                .lines()
                .collect(Collectors.joining("\n"));
        LOGGER.info("Response: {} {} || {} {}", req.getMethod(), req.getURI(), response.getStatusCode(), body);
//        }
        return response;
    }
}
