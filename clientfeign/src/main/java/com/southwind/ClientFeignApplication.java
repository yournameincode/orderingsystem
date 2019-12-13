package com.southwind;

import com.myrule.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@ServletComponentScan
@EnableHystrix
@EnableHystrixDashboard
@RibbonClient(name = "menu",configuration = MyRule.class )
public class ClientFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientFeignApplication.class,args);
    }
}
