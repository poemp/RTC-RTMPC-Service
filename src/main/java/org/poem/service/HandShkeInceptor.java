package org.poem.service;


import org.poem.entity.UserEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HandShkeInceptor extends HttpSessionHandshakeInterceptor {


    private static final Set<UserEntity> ONLINE_USERS = new HashSet<>();

    /**
     * 握手之前操作
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("握手前" + request.getURI());
        //http协议转换websoket协议进行前，通常这个拦截器可以用来判断用户合法性等
        //鉴别用户
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            UserEntity user = (UserEntity) session.getAttribute("user");
            if (user != null) {
                //这里只使用简单的session来存储用户，如果使用了springsecurity可以直接使用principal
                return super.beforeHandshake(request, response, wsHandler, attributes);
            } else {
                System.out.println("用户未登录，握手失败！");
                return false;
            }
        }
        return false;
    }

    /**
     * 握手之后操作
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param ex
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        //握手成功后，通常用来注册用户信息
        System.out.println("握手后");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
