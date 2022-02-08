package org.capstone.job_fair.utils;

import org.springframework.beans.factory.annotation.Value;

public class DomainUtil {
    @Value("${api.endpoint.domain}")
    private String domain;

    @Value("${api.endpoint.port}")
    private String apiport;

    public String generateCurrentDomain() {
        String port = "";
        if (!apiport.isEmpty()) port = ":" + apiport;
        return domain + port;
    }
}
