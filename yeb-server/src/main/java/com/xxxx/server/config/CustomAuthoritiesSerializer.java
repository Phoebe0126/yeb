package com.xxxx.server.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义反序列化，用于json解析authorities
 * @author: 陈玉婷
 * @create: 2021-08-18 17:26
 **/
public class CustomAuthoritiesSerializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectMapper codec = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNodes = codec.readTree(jsonParser);
        Iterator<JsonNode> elements = jsonNodes.elements();
        List<GrantedAuthority> list = new LinkedList<>();
        while (elements.hasNext()) {
            JsonNode jsonNode = elements.next();
            // 找到每个权限名，手动添加到authority列表里
            JsonNode authority = jsonNode.get("authority");
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.asText());
            list.add(simpleGrantedAuthority);
        }
        return list;
    }
}
