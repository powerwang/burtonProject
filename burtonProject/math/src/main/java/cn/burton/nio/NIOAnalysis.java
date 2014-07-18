package cn.burton.nio;

import org.junit.Test;
/**
 * 
 * @author Administrator
 *  nio 工作原理 http://weixiaolu.iteye.com/blog/1479656
 *  
 *  我们知道阻塞I/O在调用InputStream.read()方法时是阻塞的，它会一直等到数据到来时（或超时）才会返回；同样，在调用ServerSocket.accept()方法时，也会一直阻塞到有客户端连接才会返回，每个客户端连接过来后，服务端都会启动一个线程去处理该客户端的请求
 *  1. 当客户端多时，会创建大量的处理线程。且每个线程都要占用栈空间和一些CPU时间
 *  2. 阻塞可能带来频繁的上下文切换，且大部分上下文切换可能是无意义的。
 *  nio
 *  1. 由一个专门的线程来处理所有的 IO 事件，并负责分发。 
	2. 事件驱动机制：事件到的时候触发，而不是同步的去监视事件。 
	3. 线程通讯：线程之间通过 wait,notify 等方式通讯。保证每次上下文切换都是有意义的。减少无谓的线程切换。
	
	 关于http://ifeve.com/java-nio-all/ 
	 
	 问题一：io和nio 的区别
	 问题二：nio 的工作原理 有哪些特性
	 问题三：如何作图
	       
	 
 */
public class NIOAnalysis {

	
	@Test
	public void tenet(){
		
	}
}
