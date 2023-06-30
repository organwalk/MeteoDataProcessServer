package com.weather.handler;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.weather.handler.response.ResponseHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
@Slf4j
public class UDPClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Autowired
    private ResponseHandler resHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        ByteBuf content = packet.content();
        ByteBuffer byteBuffer = content.nioBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String response = new String(bytes);
        log.info("Response size: " + response.getBytes().length + " bytes");
        System.out.println(response);
        JsonElement jsonRes = new GsonBuilder()
                .setLenient()
                .create()
                .fromJson(response, JsonElement.class);
        codeStatusController(jsonRes, packet);
    }

    @SneakyThrows
    public void codeStatusController(JsonElement jsonRes, DatagramPacket packet) {
        switch (Integer.parseInt(getValue(jsonRes, "code"))) {
            case 2:
                log.info("Received 'Get Token' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.saveToken("root",jsonRes.getAsJsonObject().get("token").getAsString());
                break;
            case 4:
                log.info("Received 'Void Token' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.deleteToken(jsonRes.getAsJsonObject().get("token").getAsString());
                break;
            case 6:
                log.info("Received 'Get All Meteorological Station ID Information' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.saveAllStationCode(getValue(jsonRes, "data"));
                break;
            case 8:
                log.info("Received 'Get Date Range of Specified Meteorological Station Data' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.saveMeteoDateRange(getValue(jsonRes, "station"), getValue(jsonRes, "date"));
                break;
            case 10:
                log.info("Received 'Request Meteorological Data' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                System.out.println(jsonRes);
                resHandler.saveMeteoData(
                        Integer.parseInt(getValue(jsonRes, "last")),
                        getValue(jsonRes, "station"),
                        getValue(jsonRes, "date"),
                        getValue(jsonRes, "data")
                        );
                log.info("The 'last' is " + Integer.parseInt(getValue(jsonRes, "last")));
                break;
            default:
                log.info("Received unknown response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
        }

    }

    public String getValue(JsonElement jsonRes, String key) {
        return jsonRes.getAsJsonObject().get(key).toString();
    }
}
