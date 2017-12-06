package com.netty.learning.quickstart.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: Barry
 * @Date: 2017/12/5 19:57
 * @Description:
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        if(args == null || args.length != 1){
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " ");
        }

        try{
            int port = Integer.parseInt(args[0]);
            new EchoServer(port).start();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    //指定所使用的NIO传输的channel
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer() {
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            //异步的绑定服务器，调用sync方法阻塞等待直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            //调用channel的closeFuture，并且阻塞当前线程，直到完成
            future.channel().closeFuture().sync();
        }finally {
            //关闭EventLoopGroup，是否所有资源
            group.shutdownGracefully().sync();
        }

    }

}
