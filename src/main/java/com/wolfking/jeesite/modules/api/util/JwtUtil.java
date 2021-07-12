package com.wolfking.jeesite.modules.api.util;

import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.api.config.Constant;
import com.wolfking.jeesite.modules.api.entity.md.RestSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

//import org.json.simple.JSONObject;

@Component
public class JwtUtil {
    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = Constant.JWT_SECRET;
        //byte[] encodedKey = Base64.decodeBase64(stringKey);
        byte[] encodedKey = DatatypeConverter.parseBase64Binary(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     *
     * @param id        jwtid
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    /**
     * 生成subject信息
     *
     * @param session
     * @return
     */
    public static String generalSubject(String userId, String session) {
        RestSession restSession = new RestSession();
        restSession.setSession(session);
        restSession.setUserId(userId);
        return GsonUtils.getInstance().toGson(restSession);
        //JSONObject jo = new JSONObject();
        //jo.put("userId", userId);
        //jo.put("session", session);
        //return jo.toString();
        //return jo.toJSONString();
    }

}
