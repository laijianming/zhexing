package com.zhexing.zhexingmanage.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 的配置类
 Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 Queue:消息的载体,每个消息都会被投到一个或多个队列。
 Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 Producer:消息生产者,就是投递消息的程序.
 Consumer:消息消费者,就是接受消息的程序.
 Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Configuration
public class RabbitMQConfig {




    // 注入连接工厂，spring的配置，springboot可以配置在属性文件中
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        /**
//         * #  rabbitmq:
//         * #    host: 127.0.0.1
//         * #    port: 5672
//         * #    username: zhexing
//         * #    password:  zhexing
//         * #    publisher-confirms: true
//         * #    virtual-host: /
//         */
//        CachingConnectionFactory connection = new CachingConnectionFactory();
//        connection.setAddresses("127.0.0.1:5672");
//        connection.setUsername("zhexing");
//        connection.setPassword("zhexing");
//        connection.setVirtualHost("/");
//        return connection;
//    }

    //    配置RabbitAdmin来管理rabbit
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //用RabbitAdmin一定要配置这个，spring加载的是后就会加载这个类================特别重要
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    //    跟spring整合注入改模板，跟springboot整合的话只需要在配置文件中配置即可
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }





}
