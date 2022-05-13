package org.capstone.job_fair.services.interfaces.token;

public interface AgoraTokenService {
    String getRtmToken(String accountName);

    String getRtcToken(String accountName, String channelName);
}
