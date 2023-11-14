package com.weather.handler;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.weather.handler.response.ResponseHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Objects;

@Component
public class UDPClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Autowired
    private ResponseHandler resHandler;
    private static final Logger logger = LogManager.getLogger(UDPClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        ByteBuf content = packet.content();
        ByteBuffer byteBuffer = content.nioBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String response = new String(bytes);
        logger.info("Response size: " + response.getBytes().length + " bytes");
        System.out.println(response);
        JsonElement jsonRes = new GsonBuilder()
                .setLenient()
                .create()
                .fromJson(response, JsonElement.class);
        if (jsonRes.getAsJsonObject().get("code").getAsInt() == 6){
            codeStatusController(null, packet, response);
        }else {
            codeStatusController(jsonRes, packet, null);
        }

    }

    @SneakyThrows
    public void codeStatusController(JsonElement jsonRes, DatagramPacket packet, String exRes) {
        Integer code = null;
        if (Objects.isNull(jsonRes)){
            code = 6;
        }else {
            code = Integer.parseInt(getValue(jsonRes, "code"));
        }
        switch (code) {
            case 2:
                logger.info("Received 'Get Token' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.saveToken("root",jsonRes.getAsJsonObject().get("token").getAsString());
                break;
            case 4:
                logger.info("Received 'Void Token' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.deleteToken(jsonRes.getAsJsonObject().get("token").getAsString());
                break;
            case 6:
                logger.info("Received 'Get All Meteorological Station ID Information' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.saveAllStationCode(exRes);
                break;
            case 8:
                logger.info("Received 'Get Date Range of Specified Meteorological Station Data' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                resHandler.saveMeteoDateRange(getValue(jsonRes, "station"), getValue(jsonRes, "date"));
                break;
            case 10:
                logger.info("Received 'Request Meteorological Data' response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                System.out.println(jsonRes);
                resHandler.saveMeteoData(
                        Integer.parseInt(getValue(jsonRes, "last")),
                        getValue(jsonRes, "station"),
                        getValue(jsonRes, "date"),
                        getValue(jsonRes, "data")
                        );
                logger.info("The 'last' is " + Integer.parseInt(getValue(jsonRes, "last")));
                break;
            default:
                logger.info("Received unknown response from " + packet.sender().getHostString() + ":" + packet.sender().getPort());
                break;
        }

    }

    public String getValue(JsonElement jsonRes, String key) {
        return jsonRes.getAsJsonObject().get(key).toString();
    }
}
