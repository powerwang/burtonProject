package cn.burton.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

public class AboutDatagramChannel {

	/**
	 * udp 是一个能收发UDP包的通道。因为UDP是无连接的网络协议，所以不能像其它通道那样读取和写入。
	 * 它发送和接收的是数据包。
	 * @author wangshuimin
	 * @说明
	 */
	public void receive(){
		
		try {
			DatagramChannel channel  = DatagramChannel.open();
			channel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), 9999));
		    
			ByteBuffer bf = ByteBuffer.allocate(1024);
			bf.clear();
			channel.receive(bf);
		    Charset set = Charset.forName("utf-8");
		    set.decode(bf);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void send(){
		
//		DatagramChannel ch
	}
	
	
	public static void main(String[] args) {
		
	}
}
