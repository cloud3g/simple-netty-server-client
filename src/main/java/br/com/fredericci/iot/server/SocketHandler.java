package br.com.fredericci.iot.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;

import java.net.InetAddress;

public class SocketHandler extends ChannelInboundHandlerAdapter {

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

//	public ObjectHandler(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
//		this.bossGroup = bossGroup;
//		this.workerGroup = workerGroup;
//	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		////SocketHandler
		ByteBuf message = (ByteBuf) msg;
		byte[] response = new byte[message.readableBytes()];
		message.readBytes(response);
		System.out.println("receive client info: " + new String(response));

		String sendContent = "hello client ,im server, this is u say:" + new String(response);
		ByteBuf seneMsg = Unpooled.buffer(sendContent.length());
		seneMsg.writeBytes(sendContent.getBytes());

		ctx.writeAndFlush(seneMsg);
		System.out.println("send info to client:" + sendContent);
		////SocketHandler

		//服务端 接收处理数据
//		Message message = (Message) object; //解析消息
//		String value = message.getValue();
//		System.out.println(value);
//		System.out.println(object.toString());

//		String message = object.toString();
//		System.out.println(message);

		//关闭处理
//		if( value.equalsIgnoreCase("bye")) {
//			workerGroup.shutdownGracefully();
//			bossGroup.shutdownGracefully();
//		}

//        try {
//            ByteBuf in = (ByteBuf)object;
//            int readableBytes = in.readableBytes();
//            byte[] bytes =new byte[readableBytes];
//            in.readBytes(bytes);
//            System.out.println(new String(bytes));
//            //System.out.print(in.toString(CharsetUtil.UTF_8));
//        } finally {
//            // 抛弃收到的数据
//           ReferenceCountUtil.release(object);
//        }
	}

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        // logger.error("连接的客户端地址:" + ctx.channel().remoteAddress());
        // logger.error("连接的客户端ID:" + ctx.channel().id());
        ctx.writeAndFlush("client"+ InetAddress.getLocalHost().getHostName() + "success connected！ \n");
        System.out.println("connection");
        //StaticVar.ctxList.add(ctx);
        //StaticVar.chc = ctx;
        super.channelActive(ctx);
    }
}
