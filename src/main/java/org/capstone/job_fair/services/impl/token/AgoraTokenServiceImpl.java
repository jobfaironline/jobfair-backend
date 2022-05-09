package org.capstone.job_fair.services.impl.token;

import lombok.SneakyThrows;
import org.capstone.job_fair.services.interfaces.token.AgoraTokenService;
import org.capstone.job_fair.third_party.io.agora.media.RtcTokenBuilder;
import org.capstone.job_fair.third_party.io.agora.rtm.RtmTokenBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class AgoraTokenServiceImpl implements AgoraTokenService {

    private final String appID = "c99b02ecc0b940fe90959c6490af4d06";
    private final String appCertificate = "62ab025daa5d4b78a25483ad33e29d7b";
    private final Long tokenExpiredDuration = 60 * 60 * 1000L;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public String getRtmToken(String accountName) {
        int currentTimestamp = Math.toIntExact(new Date().getTime() / 1000);
        int expireTimestamp = Math.toIntExact(tokenExpiredDuration) + currentTimestamp;
        RtmTokenBuilder builder = new RtmTokenBuilder();
        String result = builder.buildToken(appID, appCertificate, accountName, RtmTokenBuilder.Role.Rtm_User, expireTimestamp);
        return result;
    }

    @Override
    public String getRtcToken(String accountName, String channelName) {
        int currentTimestamp = Math.toIntExact(new Date().getTime() / 1000);
        int expireTimestamp = Math.toIntExact(tokenExpiredDuration) + currentTimestamp;
        RtcTokenBuilder token = new RtcTokenBuilder();
        String result = token.buildTokenWithUserAccount(appID, appCertificate,
                channelName, accountName, RtcTokenBuilder.Role.Role_Publisher, expireTimestamp);
        return result;
    }
}
