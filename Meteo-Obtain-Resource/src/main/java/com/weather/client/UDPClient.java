package com.weather.client;

import com.weather.handler.UDPClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class UDPClient {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final Channel channel;
    private final InetSocketAddress serverAddress;
    private final UDPClientHandler udpClientHandler;

    public UDPClient(Environment environment, UDPClientHandler udpClientHandler) {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.serverAddress = new InetSocketAddress(
                environment.getProperty("udp.remote.host"),
                environment.getProperty("udp.remote.port", Integer.class)
        );
        this.udpClientHandler = udpClientHandler;
        this.channel = bootstrap.group(eventLoopGroup)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                .handler(new ChannelInitializer<DatagramChannel>() {
                    @Override
                    protected void initChannel(DatagramChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(udpClientHandler);
                    }
                })
                .bind(0)
                .syncUninterruptibly()
                .channel();
        // 打印日志信息
        if (channel.isActive()) {
            System.out.println("UDP client connected to server " + serverAddress.getHostString() + ":" + serverAddress.getPort());
        } else {
            System.out.println("UDP client failed to connect to server " + serverAddress.getHostString() + ":" + serverAddress.getPort());
        }
    }

    public void send(String message) throws Exception {
        byte[] data = message.getBytes();
        channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(data), serverAddress)).sync();
    }

    public void shutdown() {
        eventLoopGroup.shutdownGracefully();
    }

    @Bean
    public UDPClientHandler udpClientHandler() {
        return new UDPClientHandler();
    }
}
