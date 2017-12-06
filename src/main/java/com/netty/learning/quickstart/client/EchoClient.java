package com.netty.learning.quickstart.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: Barry
 * @Date: 2017/12/5 20:34
 * @Description:
 */
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public static void main(String[] args) throws Exception{
        if(null == args || args.length != 2){
            System.err.println("Usage : " + EchoClient.class.getSimpleName()
            + "<host><port>");
        }

        try{

            String host = args[0];
            int port = Integer.parseInt(args[1]);
            new EchoClient(host,port).start();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }


    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    //在创建channel时，向channelPipekine中添加一个Echo-clientHandler实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture future = bootstrap.connect().sync();
            //阻塞线程，直到channel关闭
            future.channel().closeFuture().sync();
        }finally {
            //关闭线程池并且释放资源
            group.shutdownGracefully().sync();
        }

    }
}
