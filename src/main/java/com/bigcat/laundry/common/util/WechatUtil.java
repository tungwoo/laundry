package com.bigcat.laundry.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 */
@Component
@Slf4j
public class WechatUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${wechat.miniapp.appid}")
    private String appid;

    @Value("${wechat.miniapp.secret}")
    private String secret;

    public WechatUtil(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 获取微信小程序用户openid
     *
     * @param code 临时登录凭证
     * @return 包含openid和sessionKey的映射
     */
    public Map<String, String> getOpenid(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
                "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();
            log.info("微信登录响应: {}", responseBody);

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                log.error("获取openid失败: {}", jsonNode.get("errmsg").asText());
                return null;
            }
            
            Map<String, String> result = new HashMap<>();
            result.put("openid", jsonNode.get("openid").asText());
            if (jsonNode.has("session_key")) {
                result.put("sessionKey", jsonNode.get("session_key").asText());
            }
            if (jsonNode.has("unionid")) {
                result.put("unionid", jsonNode.get("unionid").asText());
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取openid异常", e);
            return null;
        }
    }

    /**
     * 获取微信用户手机号
     *
     * @param code 手机号获取凭证
     * @return 用户手机号
     */
    public String getPhoneNumber(String code) {
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + getAccessToken();
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
            String responseBody = response.getBody();
            log.info("获取手机号响应: {}", responseBody);
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                log.error("获取手机号失败: {}", jsonNode.get("errmsg").asText());
                return null;
            }
            
            JsonNode phoneInfo = jsonNode.get("phone_info");
            if (phoneInfo != null && phoneInfo.has("phoneNumber")) {
                return phoneInfo.get("phoneNumber").asText();
            }
            
            return null;
        } catch (Exception e) {
            log.error("获取手机号异常", e);
            return null;
        }
    }
    
    /**
     * 获取微信小程序接口调用凭证
     *
     * @return 接口调用凭证
     */
    private String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid +
                "&secret=" + secret;
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();
            }
            
            log.error("获取access_token失败: {}", responseBody);
            return null;
        } catch (Exception e) {
            log.error("获取access_token异常", e);
            return null;
        }
    }
} 