package com.xxxx.server.config;

import com.xxxx.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author: 陈玉婷
 * @create: 2021-08-14 14:25
 **/
@Configuration
@EnableWebSocketMessageBroker //使用此注解来标识使能WebSocket的broker.即使用broker来处理消息
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserDetailsService userDetailsService;



    /**
     * 前端建立STOMP连接时需要连接该节点,配置该节点的url
     * endpoint: 指定了客户端建立连接时的请求地址
     * setAllowedOriginPatterns: 允许跨域
     * withSockJS: 指定使用SockJS协议
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ep").setAllowedOriginPatterns("*").withSockJS();
    }

    /**
     * 由于使用了JWT，故连接时需拦截并判断用户是否有效
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                // 判断COMMAND是否为连接
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Auth-Token");
                    if (!StringUtils.isEmpty(token) && token.startsWith(tokenHead)) {
                        String authToken = token.substring(tokenHead.length());
                        String userName = jwtUtil.getUserNameByToken(authToken);
                        // token有效
                        if (!StringUtils.isEmpty(token)) {
                            // 如果还未登录
                            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                            // 判断token是否有效
                            if (jwtUtil.isValidateToken(authToken, userDetails)) {
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                // websocket也需要设置用户
                                accessor.setUser(authentication);
                            }

                        }
                    }
                }
                return message;
            }
        });
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置代理域，可以配置多个，配置代理地址前缀为/queue，可以在配置域上向订阅了此broker的客户端推送消息
        registry.enableSimpleBroker("/queue");
    }
}
