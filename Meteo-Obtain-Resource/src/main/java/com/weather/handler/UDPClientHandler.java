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
    private GetTokenHandler getTokenHandler;
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
                //待实现，当响应完成后，也应从redis中删除令牌，因为数据存储服务器不再接收该令牌，因此没有存储的必要
                break;
            case 6:
                System.out.println("Received '获取所有气象站编号信息' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                //待实现，这里应该将获取的data作某种字符串处理，得到其中的值，然后存储进station表中
                break;
            case 8:
                System.out.println("Received '获取指定气象站的数据日期范围' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                //待实现，这里应该将获取的data作某种字符串处理，得到其中的值，然后存储进station表中
                break;
            case 10:
                System.out.println("Received '请求气象数据' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                /**
                 * 待实现，这里应该将获取的station、date、data以目前假数据规范，即有序集合形式持久化成rdb文件
                 * 尝试将其恢复至redis，名称应以”气象站编号_weather_年份“为规范
                 * 完成redis-to-mysql，可调用python脚本
                 * 合作实现
                 **/
                break;
            default:
                System.out.println("Received unknown response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
        }
    }
}
