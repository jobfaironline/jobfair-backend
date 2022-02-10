package org.capstone.job_fair.utils;

import org.springframework.beans.factory.annotation.Value;

public class AwsUtil {
    @Value("${amazonProperties.cdn-link}")
    private String cdnLink;

    public String generateAwsS3AccessString(String folderPath, String fileName) {
        StringBuffer url = new StringBuffer(cdnLink);
        url.append("/");
        url.append(folderPath);
        url.append("/");
        url.append(fileName);
        return url.toString();
    }
}
