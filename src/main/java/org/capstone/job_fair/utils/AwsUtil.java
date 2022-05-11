package org.capstone.job_fair.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

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

    @SneakyThrows
    public static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);

        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        mac.update(userName.getBytes(StandardCharsets.UTF_8));
        byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(rawHmac);
    }
}
