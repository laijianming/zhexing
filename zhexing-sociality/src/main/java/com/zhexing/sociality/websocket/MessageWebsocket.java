package com.zhexing.sociality.websocket;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
import com.zhexing.sociality.dao.RedisDao;
import com.zhexing.sociality.pojo.WsUser;
import com.zhexing.sociality.utils.JsonUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * 参考
 *  https://www.jianshu.com/p/e647758a7c50  √
 *  https://www.cnblogs.com/xiaoyao-001/p/9609900.html
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



    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 存放user的信息
    private WsUser user;

    /**
     *  以下关于消息处理的常量
     */

    @Autowired
    RedisDao redisDao;

    @Autowired
    RabbitAdmin rabbit;
    @Autowired
    RabbitTemplate rabbitTemplate;


    // 服务redis websocket注册
    static String WS_USERID_SERVER_ = "WS_USERID_SERVER_";
    // 该服务的通用消息队列
    static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    static final String key = df.format(new Date());
    static final String WS_MSG_QUEUE = "WS_MSG_QUEUE_" + key;
    static final String WS_MSG_ROUTINGKEY = "WS_MSG_ROUTINGKEY" + key;
    // 消息队列交换机
    static final String WS_MSG_EXCHANGE = "WS_MSG_EXCHANGE";
    // 用户特定消息队列前缀
    static final String MS_USER_MESSAGE_QUEUE = "WS_USER_MESSAGE_QUEUE_";
    // message 分隔符  “!#+@+#!”   接收消息格式：接收者id-发送者id-消息  发送消息格式：接收者id-发送者id-消息-发送时间-发送者信息json
    static final String MS_SPLIT = "!#+@+#!";



    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") Long userId,
                       @PathParam("unickname") String unickname,
                       @PathParam("uchathead") String uchathead){
        System.out.println(unickname + " ==> 建立连接");
        // 1、将用户信息添加到 webSocketSet 和 usersMap集合中保存
        user = new WsUser(userId,unickname,uchathead);
        usersMap.put(userId,user);
        this.session = session;
        // 2、将用户添加到redis上
        redisDao.hput(WS_USERID_SERVER_,userId + "",WS_MSG_QUEUE,0L);

        webSocketSet.add(this);
        // 3、获取消息队列的消息，并发送给用户

    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        // 1、将用户信息从webSocketSet usersMap中移除
        webSocketSet.remove(this);
        usersMap.remove(user.getUserId());
        // 2、删除redis上的在线记录
        redisDao.hdel(WS_USERID_SERVER_,this.user.getUserId() + "");
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

        // 1、若该用户在线，则直接发给本服务消息队列处理
        if(usersMap.containsKey(receiveId)){

        }else {
            // 2、若该用户不在线,看redis上其他服务是否在线，在则发给对应服务的消息队列

            // 否则发给用户对应的消息队列中存储
        }


    }


    /**
     * 通用消息队列 与 服务特定消息队列 消息处理，自启
     *
     */
    @PostConstruct
    public void messageDeal(){
        // 初始化 rabbitadmin 配置好 rabbittemplate
        rabbit.declareQueue(new Queue(WS_MSG_QUEUE));
        rabbit.declareExchange(new DirectExchange(WS_MSG_EXCHANGE));
//        rabbit.declareQueue(new Queue())
        //String destination, Binding.DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments
//        rabbit.declareBinding(new Binding(WS_MSG_QUEUE,Binding.DestinationType.QUEUE,WS_MSG_EXCHANGE,));


        // 将自己的消息队列注册到redis上
//        rabbitTemplate.

        // 得另外开一个线程去处理这个方法的业务，否则该方法会占用程序启动的主线程

        // 服务特定消息队列处理
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 处理消息队列的消息

                // 1、将通用消息队列的消息取出

                // 2、判断接收用户是否在线,用户在线，则直接发送给用户
                try {
                    sendMessage("");
//                    rabbitTemplate.send();
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
