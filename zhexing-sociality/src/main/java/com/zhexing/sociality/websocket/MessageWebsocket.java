package com.zhexing.sociality.websocket;

import com.zhexing.sociality.pojo.WsUser;
import com.zhexing.sociality.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * 参考
 *  https://www.cnblogs.com/boshen-hzb/p/6841982.html
 *  https://blog.csdn.net/qq_38455201/article/details/80308771
 */

@ServerEndpoint("/websocket/message/{userId}/{unickname}/{uchathead}")
public class MessageWebsocket implements Serializable {


    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<MessageWebsocket> webSocketSet = new CopyOnWriteArraySet<MessageWebsocket>();

    /**
     *  这是单服务情况下的连接记录
     */
    // 保存在线用户的信息
    private static ConcurrentHashMap<Long,WsUser> usersMap = new ConcurrentHashMap<>(1000);


    /**
     * 多服务集群情况下，想法：使用redis来记录在线人数
     */
    // private String Connect = "WS_CONNECT_";  // 使用hash结构记录在线人数

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 存放user的信息
    private WsUser user;

    /**
     *  以下关于消息处理的常量
     */
    // message 分隔符  “!#+@+#!”   接收消息格式：接收者id-发送者id-消息  发送消息格式：接收者id-发送者id-消息-发送时间-发送者信息json
    private String MS_SPLIT = "!#+@+#!";

    // 通用消息队列
    String MS_MESSAGE_QUEUE = "MS_MESSAGE_QUEUE";

    // 用户特定消息队列前缀
    String MS_USER_MESSAGE_QUEUE = "MS_USER_MESSAGE_QUEUE_";


    @Autowired
//    RabbitTemplate rabbitTemplate;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") Long userId,
                       @PathParam("unickname") String unickname,
                       @PathParam("uchathead") String uchathead){
        System.out.println("建立连接");
        // 1、将用户信息添加到 webSocketSet 和 usersMap集合中保存
        user = new WsUser(userId,unickname,uchathead);
        usersMap.put(userId,user);
        this.session = session;
        /**
         * 后期做高可用搞扩展的话，将 this对线序列化保存到redis，用hash结构，H：WS_USER_SESSION K：userId V：JsonUtils.toJson(this);
         *
         * 1、先从本地webSocketSet集合中查用户是否在，若不在则再从redis上查，若在redis中，则保存到本地。
         *
         * 2、当用户断开连接的时候，先通知
         */

        webSocketSet.add(this);
        // 2、获取消息队列的消息，并发送给用户


    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        // 1、将用户信息从webSocketSet usersMap中移除
        webSocketSet.remove(this);
        usersMap.remove(user.getUserId());
    }


    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        // message 处理
        String[] ms = message.split(MS_SPLIT);
        StringBuffer result = new StringBuffer(message.length() + 100);
        result.append(message).append(MS_SPLIT).append(new Date()).append(MS_SPLIT).append(JsonUtils.objectToJson(user));
        // 获取接收用户id
        Long receiveId = Long.parseLong(ms[0]);

        // 1、若该用户在线，则直接发给该 通用消息队列处理
        if(usersMap.containsKey(receiveId)){

        }else {
            // 2、若该用户不在线
            // 则发给用户对应的消息队列中存储
        }


    }


    /**
     * 通用消息队列消息处理，自启
     *
     */
    @PostConstruct
    public void messageDeal(){
        // 得另外开一个线程去处理这个方法的业务，否则该方法会占用程序启动的主线程
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 处理消息队列的消息

                // 1、将通用消息队列的消息取出

                // 2、判断接收用户是否在线,用户在线，则直接发送给用户
                try {
                    sendMessage("");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 3、若接收用户不在线，则发送给该用户的特定队列中存储



            }
        }).start();
    }


    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误  error = " + error);
        if(error != null)
            error.printStackTrace();
    }


    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * 发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message); // 发送消息
    }








}
