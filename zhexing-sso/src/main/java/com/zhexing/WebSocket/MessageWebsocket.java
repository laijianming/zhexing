package com.zhexing.WebSocket;

import com.zhexing.sso.domain.WsUser;
import com.zhexing.sso.Util.JsonUtils;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{userId}/{uname}/{unickname}/{uchathead}")
public class MessageWebsocket {


    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户userId标识
    private static ConcurrentHashMap<Long,MessageWebsocket>
            webSocketMap = new ConcurrentHashMap<>();

    /**
     *  这是单服务情况下的连接记录
     */
    // 保存在线用户的信息
    public static ConcurrentHashMap<Long,WsUser> usersMap = new ConcurrentHashMap<>(1000);

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 存放user的信息
    private WsUser user;

    /**
     *  以下关于消息处理的常量
     */
    // 用户routingKey
    static final String WS_MSG_USER_RK = "WS_MSG_USER_RK_";
    // 该服务的routingKey
    static final String WS_MANAGER_ROUTINGKEY = "WS_MANAGER_ROUTINGKEY";
    // 该服务的通用消息队列
    static final String WS_MANAGER_QUEUE_ = "WS_MANAGER_QUEUE_";
    // 消息队列交换机
    static final String WS_MANAGER_EXCHANGE = "WS_MANAGER_EXCHANGE";
    // 私信消息队列交换机
    static final String WS_MSG_EXCHANGE = "WS_MSG_EXCHANGE";
    // message 分隔符  “!#+@+#!”   接收消息格式：接收者id-发送者id-消息  发送消息格式：接收者id-发送者id-消息-发送时间-发送者信息json
    static final String MS_SPLIT = "!∮@∮!";

    // 消息持久化前缀
    public static final String WS_MSG = "WS_MSG:";



    static RedisTemplate redisTemplate;


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate){
        MessageWebsocket.redisTemplate = redisTemplate;
    }



    static RabbitAdmin rabbit;

    @Autowired
    public void setRabbitAdmin(RabbitAdmin rabbitAdmin){
        MessageWebsocket.rabbit = rabbitAdmin;
    }

    static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate){
        MessageWebsocket.rabbitTemplate = rabbitTemplate;
    }
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId,
                       @PathParam("uname") String uname,
                       @PathParam("unickname") String unickname,
                       @PathParam("uchathead") String uchathead){
        System.out.println(unickname + " ==> 建立连接 ");
        // 1、将用户信息添加到 webSocketMap 和 usersMap集合中保存
        user = new WsUser(userId,uname,unickname,uchathead);
        usersMap.put(userId,user);
        this.session = session;
        webSocketMap.put(userId,this);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        // 1、将用户信息从webSocketSet usersMap中移除
        logout();
    }


    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("接受消息 message == " + message);
        // message 处理
        StringBuffer result = new StringBuffer(message.length() + 100);
        result.append(message).append(MS_SPLIT).append(new Date()).append(MS_SPLIT).append(JsonUtils.objectToJson(user));
        // 获取接收用户id
        String userId = message.substring(0, message.indexOf("!∮@∮!"));
        Long receiveId = Long.parseLong(userId);
        Message message1 = new Message((result+"").getBytes(), new MessageProperties());
        // 1、若该用户在线，则直接发给用户
        if(usersMap.containsKey(receiveId)){
            sendMessage(result + "");
        }else {
            // 否则发给用户对应的消息队列中存储
            System.out.println("用户不在线 需要发送给用户特定队列");
            rabbitTemplate.send(WS_MSG_EXCHANGE,WS_MSG_USER_RK+receiveId,message1);
        }


    }


    /**
     * 通用消息队列 与 服务特定消息队列 消息处理，自启
     *
     */
    @PostConstruct
    public void messageDeal(){
        // 初始化 rabbitadmin 配置好 rabbittemplate
        rabbit.declareExchange(new DirectExchange(WS_MSG_EXCHANGE,true,false));
        // 断开连接时删除队列
        rabbit.declareQueue(new Queue(WS_MANAGER_QUEUE_,false,true,false,null));
        // 将队列绑定到交换机
        rabbit.declareBinding(new Binding(WS_MANAGER_EXCHANGE,Binding.DestinationType.QUEUE,WS_MSG_EXCHANGE,WS_MANAGER_ROUTINGKEY,new HashMap<String,Object> ()));
        // 得另外开一个线程去处理这个方法的业务，否则该方法会占用程序启动的主线程
        // 服务消息队列处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("=======>  开启消息队列处理线程  ");
                // 处理消息队列的消息
                Message receive;
                while (true){
                    // 1、将通用消息队列的消息取出
                    receive = rabbitTemplate.receive(WS_MANAGER_QUEUE_);
                    if(receive != null){

                        // 处理消息
                        try {
                            sendMessage(new String(receive.getBody()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        receive = null;
                    }
                }

            }
        }).start();
    }

    /**
     * 用户退出
     */
    public void logout(){
        Long userId = user.getUserId();
        // 删除本地记录
        webSocketMap.remove(userId);
        usersMap.remove(userId);
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误  error = " + error);
        logout();
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
        String userIdstr = message.substring(0, message.indexOf("!∮@∮!"));
        long userId = Long.parseLong(userIdstr);
        MessageWebsocket user = webSocketMap.get(userId);
        user.session.getBasicRemote().sendText(message); // 发送消息

        // 消息持久化
        String[] msg = message.split(MS_SPLIT);
        Long receiveId = Long.parseLong(msg[0]);
        Long sendId = Long.parseLong(msg[1]);
        // 获取消息持久化的key后缀
        String receive = receiveId + "_" + sendId;
        String send =  sendId + "_" + receiveId;
        // 把消息添加到接受者 和 发送者 缓存
        redisTemplate.opsForList().leftPush(WS_MSG + receive,message);
        redisTemplate.opsForList().leftPush(WS_MSG + send,message);
    }



}
