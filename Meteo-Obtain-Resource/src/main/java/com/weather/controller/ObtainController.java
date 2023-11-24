package com.weather.controller;

import com.weather.handler.response.ResponseHandler;
import com.weather.mapper.SaveToMySQLMapper;
import com.weather.service.UdpRequestService;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CountDownLatch;


/**
 * 此处提供了内部Http端点供服务调用
 * 服务调用使用了SpringBoot3内置的Http客户端，即WebFlux
 * by organwalk 2023.05.28
 **/

@RestController
@RequestMapping("/api/obtain")
public class ObtainController {
    @Autowired
    private UdpRequestService udpRequestService;
    @Autowired
    private ResponseHandler response;
    @Autowired
    private SaveToMySQLMapper mapper;
    private static final Logger logger = LogManager.getLogger(ObtainController.class);

    @GetMapping("/token/user")
    public boolean getToken(@RequestParam String name){
        logger.info("接收到连接远程数据存储服务器获取用户令牌的请求");
        boolean success = udpRequestService.getToken(name);
        if (success){
            return true;
        }

        CountDownLatch latch = new CountDownLatch(1);
        logger.info("[等待]--正在获取用户令牌");
        response.setSaveTokenCallback(isSaved -> {
            logger.info("[完成]--成功进行获取用户令牌操作");
            latch.countDown();
        });
        try {
            latch.await(); // 等待计数器归零，即等待异步操作完成
        } catch (InterruptedException e) {
            return false;
        }

        return response.isTokenSaved();
    }

    @PostMapping("/token")
    public boolean voidToken(@RequestParam String name){
        return udpRequestService.voidToken(name);
    }

    @GetMapping("/meteo/station")
    public boolean getStationCode(@RequestParam String name) {
        logger.info("[完成]--接收到同步气象站编号数据的请求");
        udpRequestService.getAllStationCode(name);

        // 创建一个 CountDownLatch，初始值为 1
        CountDownLatch latch = new CountDownLatch(1);
        logger.info("[等待]--进行同步气象站编号数据的处理");
        response.setStationCodeCallback(isSaved -> {
            logger.info("[完成]--同步气象站编号数据的处理");
            latch.countDown(); // 异步操作完成后，调用 countDown() 方法减小计数器的值
        });
        try {
            latch.await(); // 等待计数器归零，即等待异步操作完成
        } catch (InterruptedException e) {
            return false;
        }
        return response.isStationCodeSave();
    }

    @GetMapping("/meteo/date_range")
    public boolean getDateRange(@RequestParam(name = "name") String name, @RequestParam(name = "station") String station) {
        logger.info("[完成]--接收到同步气象站有效采集日期的请求");
        udpRequestService.getAllStationDataRange(name,station);

        CountDownLatch latch = new CountDownLatch(1);
        logger.info("[等待]--正在同步气象站有效采集日期");
        response.setDateRangeCallback(isSaved -> {
            logger.info("[完成]--成功同步气象站有效采集日期");
            latch.countDown();
        });
        try {
            latch.await(); // 等待计数器归零，即等待异步操作完成
        } catch (InterruptedException e) {
            return false;
        }
        return response.isDateRangeSave();
    }

    @GetMapping("/meteo/data")
    public boolean getMeteoData(@RequestParam String name,
                                @RequestParam String station,
                                @RequestParam String start,
                                @RequestParam String end) {
        logger.info("[完成]--接收到同步气象数据的请求");
        udpRequestService.getMeteoData(name, station, start, end);

        return nowGetMeteoData(station+"_meteo_data",start);
    }

    @SneakyThrows
    public Boolean nowGetMeteoData(String table, String date){
        while (mapper.checkMeteoDataExist(table,date) == null){
            Thread.sleep(3000);
        }
        return true;
    }
}
