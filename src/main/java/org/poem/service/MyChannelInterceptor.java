package org.poem.service;


import org.poem.dao.StatDao;
import org.poem.entity.Guest;
import org.poem.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MyChannelInterceptor extends ChannelInterceptorAdapter {
    @Autowired
    private StatDao statDao;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public boolean preReceive(MessageChannel channel) {
        System.out.println("preReceive");
        return super.preReceive(channel);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        //检测用户订阅内容（防止用户订阅不合法频道）
        if (StompCommand.SUBSCRIBE.equals(command)) {
            //从数据库获取用户订阅频道进行对比(这里为了演示直接使用set集合代替)
            Set<String> subedChannelInDB = new HashSet<>();
            subedChannelInDB.add("/topic/group");
            subedChannelInDB.add("/topic/online_user");
            if (subedChannelInDB.contains(accessor.getDestination())) {
                //该用户订阅的频道合法
                return super.preSend(message, channel);
            } else {
                //该用户订阅的频道不合法直接返回null前端用户就接受不到该频道信息。
                return null;
            }
        } else {
            return super.preSend(message, channel);
        }

    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        //System.out.println("afterSendCompletion");
        //检测用户是否连接成功，搜集在线的用户信息如果数据量过大我们可以选择使用缓存数据库比如redis,
        //这里由于需要频繁的删除和增加集合内容，我们选择set集合来存储在线用户
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        System.out.println("命令=" + command);
        if (StompCommand.CONNECT.equals(command)) {
            Map<String, UserEntity> map = (Map<String, UserEntity>) accessor.getHeader("simpSessionAttributes");
            //ONLINE_USERS.add(map.get("user"));
            UserEntity user = map.get("user");
            if (user != null) {
                statDao.pushOnlineUser(user);
                Guest guest = new Guest();
                guest.setUserEntity(user);
                guest.setAccessTime(Calendar.getInstance().getTimeInMillis());
                statDao.pushGuestHistory(guest);
                //通过websocket实时返回在线人数
                this.simpMessagingTemplate.convertAndSend("/topic/online_user", statDao.getAllUserOnline());
            }

        }
        //如果用户断开连接，删除用户信息
        if (StompCommand.DISCONNECT.equals(command)) {
            Map<String, UserEntity> map = (Map<String, UserEntity>) accessor.getHeader("simpSessionAttributes");
            //ONLINE_USERS.remove(map.get("user"));
            UserEntity user = map.get("user");
            if (user != null) {
                statDao.popOnlineUser(user);
                simpMessagingTemplate.convertAndSend("/topic/online_user", statDao.getAllUserOnline());
            }

        }
        super.afterSendCompletion(message, channel, sent, ex);
    }

}
