package com.zhexing.zhexingzipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * 监控不到其他服务和服务的调用，参考：
 *  https://blog.csdn.net/zxingchao2009/article/details/88874057
 */
@EnableEurekaClient
@EnableZipkinServer
@SpringBootApplication
public class ZhexingZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhexingZipkinApplication.class, args);
    }

}
