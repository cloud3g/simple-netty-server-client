package br.com.fredericci.iot.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import br.com.fredericci.iot.common.Message;
import io.netty.handler.codec.string.StringEncoder;

public class Client {

	EventLoopGroup workerGroup = new NioEventLoopGroup();
	private ChannelFuture channelFuture = null;

	public void connect(String host, Integer port) throws Exception {

		Bootstrap bootstrap = new Bootstrap(); // (1)
		bootstrap.group(workerGroup); // (2)
		bootstrap.channel(NioSocketChannel.class); // (3) //  NioServerSocketChannel=server/NioSocketChannel=client

		//在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下 这套机制才会被激活
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // 是否启用心跳保活机制
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				//ch.pipeline().addLast(new ObjectEncoder()); //ObjectEncoder=client / ObjectDecoder=server
				ch.pipeline().addLast(new StringEncoder());
			}
		});

		// Start the client.
		channelFuture = bootstrap.connect(host, port).sync(); // (5) 连接端口

	}
	
	public void send(Message message) {
		channelFuture.channel().writeAndFlush(message);
	}

	public void send(String message) {
		channelFuture.channel().writeAndFlush(message);
	}

	public void close() throws Exception {

		try {
			channelFuture.channel().close().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}

}

