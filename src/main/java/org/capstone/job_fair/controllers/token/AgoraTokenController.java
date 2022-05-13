package org.capstone.job_fair.controllers.token;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.services.interfaces.token.AgoraTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AgoraTokenController {

    @Autowired
    private AgoraTokenService agoraTokenService;

    @GetMapping(ApiEndPoint.AgoraToken.AGORA_RTM_TOKEN)
    public ResponseEntity<?> getAgoraClientRtmToken() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String userId = userDetails.getId();;
        String token = agoraTokenService.getRtmToken(userId);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiEndPoint.AgoraToken.AGORA_RTC_TOKEN)
    public ResponseEntity<?> getAgoraClientRtcToken(@RequestParam @NotNull String channelName) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String userId = userDetails.getId();
        String token = agoraTokenService.getRtcToken(userId, channelName);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


}
