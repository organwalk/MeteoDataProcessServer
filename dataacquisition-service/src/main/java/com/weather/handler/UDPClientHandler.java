package com.weather.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.weather.handler.response.GetTokenHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public class UDPClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Autowired
    GetTokenHandler getTokenHandler;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        ByteBuf content = packet.content();
        ByteBuffer byteBuffer = content.nioBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String response = new String(bytes);
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(response, JsonElement.class);
        int code = jsonElement.getAsJsonObject().get("code").getAsInt();
        switch (code) {
            case 2:
                System.out.println("Received '获取令牌' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                String token = jsonElement.getAsJsonObject().get("token").getAsString();
                getTokenHandler.saveTokenToRedis(token);
                break;
            case 4:
                System.out.println("Received '作废令牌' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
            case 6:
                System.out.println("Received '获取所有气象站编号信息' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
            case 8:
                System.out.println("Received '获取指定气象站的数据日期范围' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
            case 10:
                System.out.println("Received '请求气象数据' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
            default:
                System.out.println("Received unknown response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
        }
    }
}
