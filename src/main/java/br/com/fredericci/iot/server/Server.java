package br.com.fredericci.iot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Server {
	private int port;

	//一旦"boss"接收到连接，就会把信息注册到"worker"上。如何知道多少个线程已经被使用，如何映射到已经创建的Channel上都需要依赖于EventLoopGroup的实现，
	// 并且可以通过构造函数来配置它们之间的关系
	private EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1) 处理I/O事件的多线程事件循环器 用来接收进来的连接
	private EventLoopGroup workerGroup = new NioEventLoopGroup(); //用来处理被接收的连接

	public Server(int port) {
		this.port = port;
	}

	public void startAndWait() throws Exception {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap(); // (2)
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class); //  NioServerSocketChannel=server/NioSocketChannel=client
			bootstrap.childHandler(new Channel()); // (3)

			//BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
			// 如果未设置或所设置的值小于1，Java将使用默认值50
			bootstrap.option(ChannelOption.SO_BACKLOG, 128); // (5) //server=SO_BACKLOG /client=SO_KEEPALIVE //
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture channel = bootstrap.bind(port).sync(); // (7) 绑定端口

			// Wait until the server socket is closed.
			channel.channel().closeFuture().sync(); //关闭
			
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	class Channel extends ChannelInitializer<SocketChannel> { // (4)
		@Override
		public void initChannel(SocketChannel ch) throws Exception {
			//ch.pipeline().addLast("frameDecoder" , new ObjectDecoder(ClassResolvers.cacheDisabled(null))); //ObjectEncoder=client / ObjectDecoder=server
			//ch.pipeline().addLast("objectHandler", new ObjectHandler(bossGroup, workerGroup)); //class ObjectHandler extends ChannelInboundHandlerAdapter =>channelRead

			// ch.pipeline().addLast(new NettyMessageDecoder(1024 * 10, 4, 4));

			// ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
			// ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,7,2,10,0));

			// ch.pipeline().addLast("objectHandler",new SocketHandler());

            ch.pipeline().addLast("frameDecoder" , new StringDecoder());
            ch.pipeline().addLast("objectHandler", new ObjectHandler(bossGroup, workerGroup));
		}
	}
}

