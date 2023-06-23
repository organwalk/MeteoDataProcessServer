package com.weather.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.weather.handler.response.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

@Component
public class UDPClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Autowired
    private GetTokenHandler getTokenHandler;
    @Autowired
    private VoidTokenHandler voidTokenHandler;
    @Autowired
    private GetAllStationCodeHandler getAllStationCodeHandler;
    @Autowired
    private GetMeteoDateRangeHandler getMeteoDateRangeHandler;
    @Autowired
    private GetMeteoDataHandler getMeteoDataHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws UnsupportedEncodingException {
        ByteBuf content = packet.content();
        ByteBuffer byteBuffer = content.nioBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String response = new String(bytes);
        System.out.println(response);
        byte[] responseBytes = response.getBytes();
        System.out.println("Response size: " + responseBytes.length + " bytes");
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(response, JsonElement.class);
        int code = jsonElement.getAsJsonObject().get("code").getAsInt();
        switch (code) {
            case 2:
                System.out.println("Received '获取令牌' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                String token = jsonElement.getAsJsonObject().get("token").getAsString();
                String username = "root";
                getTokenHandler.saveTokenToRedis(username,token);
                break;
            case 4:
                System.out.println("Received '作废令牌' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                String key = jsonElement.getAsJsonObject().get("token").getAsString();
                voidTokenHandler.deleteTokenInRedis(key);
                break;
            case 6:
                System.out.println("Received '获取所有气象站编号信息' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                String allStationCode = jsonElement.getAsJsonObject().get("data").toString();
                getAllStationCodeHandler.saveAllStationCodeToRedis(allStationCode);
                break;
            case 8:
                System.out.println("Received '获取指定气象站的数据日期范围' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                String d_station = jsonElement.getAsJsonObject().get("station").toString();
                String meteoDateRange = jsonElement.getAsJsonObject().get("date").toString();
                getMeteoDateRangeHandler.saveMeteoDateRangeToRedis(d_station,meteoDateRange);
                break;
            case 10:
                System.out.println("Received '请求气象数据' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                System.out.println(jsonElement);
                String station = jsonElement.getAsJsonObject().get("station").toString();
                String date = jsonElement.getAsJsonObject().get("date").toString();
                String meteoData = jsonElement.getAsJsonObject().get("data").toString();
                if(jsonElement.getAsJsonObject().get("last").getAsInt() == 1) {
                    getMeteoDataHandler.saveMeteoDataToRedis(station,date,meteoData);
                    System.out.println("数据已传输完毕");
                }else {
                    getMeteoDataHandler.saveMeteoDataToRedis(station,date,meteoData);
                    System.out.println("数据还未传输完毕");
                }
                break;
            default:
                System.out.println("Received unknown response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
        }
    }
}
