package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class NettyTcpClient {
	private static NettyTcpClient ins = null;
	
	public static NettyTcpClient instance(){
		if(ins == null){
			ins = new NettyTcpClient();
		}
		return ins;
	}
	
	public void conect(String host, int port){
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", new ProtoDecoder(5120));
				pipeline.addLast("encoder", new ProtoEncoder(2048));
				pipeline.addLast("serverHandler", new ClientHandler());
			}
		});

		ChannelFuture future;
		try {
			future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
			System.out.println("----channel:"+future.channel());
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		//future.channel().closeFuture().awaitUninterruptibly();
	}
}
