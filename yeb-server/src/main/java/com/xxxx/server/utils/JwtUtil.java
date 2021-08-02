package com.xxxx.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: gouzi
 * @create: 2021-07-28 15:24
 **/
@Component
public class JwtUtil {

    public static final String CLAIM_KEY_SUB = "sub";
    public static final String CLAIM_KEY_CREATED = "created";

    // 从配置application.yml中取秘钥和过期时间
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expiration;

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUB, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 根据荷载生成token
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpiration())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token的过期时间
     * @return
     */
    private Date generateExpiration() {
        Date date = new Date(System.currentTimeMillis() + expiration * 1000);
        return date;
    }

    /**
     * 根据token获取用户名
     * @param token
     * @return
     */
    public String getUserNameByToken(String token) {
        Claims claims = getClaimsByToken(token);
        return claims.getSubject();
    }

    /**
     * 根据token获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsByToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isValidateToken(String token, UserDetails userDetails) {
        String userName = getUserNameByToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpire(token);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpire(String token) {
        Claims claims = getClaimsByToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 判断是否可以刷新token，过期之后不可刷新
     * @param token
     * @return
     */
    public boolean isRefreshToken(String token) {
        return !isTokenExpire(token);
    }

    public String refreshToken(String token) {
        Claims claims = getClaimsByToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
