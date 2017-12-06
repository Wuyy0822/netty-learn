package com.netty.learning.quickstart.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @Author: Barry
 * @Date: 2017/12/5 20:23
 * @Description:
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //记录已经接受到的消息
        System.out.println("Client received : " + msg.toString(Charset.defaultCharset()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
