package com.goose.calculator.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class OpenAPIController {
    @Value("classpath:static/css/swagger-ui.css")
    private Resource cssFile;

    @GetMapping(value = "/swagger-ui/swagger-ui.css")
    public void resourceCSS(HttpServletRequest request, HttpServletResponse response) {
        setResource(cssFile, response, "text/css;charset=UTF-8");
    }

    private void setResource(Resource resource, HttpServletResponse response, String contentType) {
        try {
            response.setHeader("content-type", contentType);
            response.setHeader("Pragma", "no-cache");
            byte[] file = IOUtils.toByteArray(Objects.requireNonNull(resource.getURI()));
            response.getOutputStream().write(file);
        } catch (Exception e) {
            System.err.println("Error occurred while loading the OpenAPI CSS file: {} " + e.getMessage());
        }
    }
}