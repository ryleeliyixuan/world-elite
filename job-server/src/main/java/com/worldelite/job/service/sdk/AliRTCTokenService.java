package com.worldelite.job.service.sdk;

import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.RTCTokenForm;
import com.worldelite.job.service.BaseService;
import com.worldelite.job.vo.RTCTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
public class AliRTCTokenService extends BaseService {
    // Parsed from CLI.
    private String appId = "rifwol88";
    private String appKey = "9772af2eba07d5c84ce5075f11c6b904";

    public String createToken(
            String appId, String appKey, String channelId, String userId,
            String nonce, Long timestamp
    ) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(appId.getBytes());
        digest.update(appKey.getBytes());
        digest.update(channelId.getBytes());
        digest.update(userId.getBytes());
        digest.update(nonce.getBytes());
        digest.update(Long.toString(timestamp).getBytes());

        String token = DatatypeConverter.printHexBinary(digest.digest()).toLowerCase();
        return token;
    }

    public RTCTokenVo getToken(RTCTokenForm tokenForm){
        String channelId = tokenForm.getChannelId();
        String userId = String.valueOf(curUser().getId());
        Long timestamp = System.currentTimeMillis();
        String nonce = "AK-"+UUID.randomUUID().toString().replace("-", "");
        try {
            String token = createToken(appId,appKey,channelId,userId,nonce,timestamp);
            RTCTokenVo rtcTokenVo = new RTCTokenVo();
            rtcTokenVo.setToken(token);
            return rtcTokenVo;
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(message("api.error.fail"));
        }
    }
}
