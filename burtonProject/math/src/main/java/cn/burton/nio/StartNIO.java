package cn.burton.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
/**
 * 
 * @author Administrator
 *
 * 关于缓冲的子缓存
 * io inputStream OutputStream 是基于流的实现以字节为单位处理数据，并且非常容易建立各种过滤器
 * 1、为所有的原始类型提供（buffer）缓冲支持
 * 2、使用 java.nio.charset.Charset 作为字节集编码解码解决方案
 * 3、增加通道（channel）对象，作为新的原始I/O 抽象
 * 4、支持锁和内存映射文件的文件访问接口
 * 5、提供了基于Selector 的异步网络I/O
 *    关于http://blog.csdn.net/tsyj810883979/article/details/6876599
 *    
 * 
 * NIO 是基于块（block）,它以块为单位处理单位，在NIO 中最重要的两个组件，channel和buffer,buffer是一个连续内存块
 * ，是nio 数据的中转站，通道是访问缓冲的接口，表示缓冲数据的源头或者目的地，它用于向缓冲或者写入数据
 * 
 * channel 双向通道，stream 是单向
 * 
 * nio 的工作原理
 * http://weixiaolu.iteye.com/blog/1479656
 * 
 * 阻塞io????
 * 
 * Java NIO 和io 最大的区别，
 * io 是面向流 nio 是面向缓冲区，io 每次从流中获取一个或者多个字节，直至获取所用字节，它没有
 * 被缓存在任何地方，此外，它不能前后移动流中的数据，如果想前后移动读取数据，必须将其缓存到一个缓冲区中，
 * Java NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。
 * 这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。
 * 而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据
 * 
 * 区别：
 *  1、io 是各种流阻塞，当一个线程执行 write read ,该线程阻塞直到数据写完或者读完
 *  2、Java NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。
 *     而不是保持线程阻塞，所以直至数据变的可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此。
 *     一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。
 *     线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。
 * 优缺点
 *  优点：NIO可让您只使用一个（或几个）单线程管理多个通道（网络连接或文件），
 *  缺点：但付出的代价是解析数据可能会比从一个阻塞流中读取数据更复杂。
 * 
 * 应用场景：
 *  nio :如果需要管理同时打开的成千上万个连接，这些连接每次只是发送少量的数据，例如聊天服务器，实现NIO的服务器可能是一个优势
 *  io:有少量的连接使用非常高的带宽，一次发送大量的数据，也许典型的IO服务器实现可能非常契合
 * 
 * -----------------------------------------------
 * 
 * ------------------------------------------------
 * selector ???? 多路总线
 * 
 * channel
 * buffer
 * selector
 * 
 * 
 * 
 * 
 */
public class StartNIO {

	
	
	
	
	
	
	
	/**
	 * 1、先读取文件
	 * 2、建立channel（双向通道） 
	 * 3、新建buffer ，将数据块，channel 写入buffer中,写满
	 * 4、flip() 将postion 至0,limit 至上次的position的位置
	 *    clear() 清楚总个buffer
	 *    compact() 清楚已读
	 *    方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面
	 * 5、buffer get()  或者 channel.writer()
	 * 
	 * rewind()方法
	 * Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。
	 * clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
	 * compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
	 * 
	 * size()  
	 * truncate()  
	 * 可以使用FileChannel.truncate()方法截取一个文件。截取文件时，文件将中指定长度后面的部分将被删除。如：
	 * channel.truncate(1024); 这个例子截取文件的前1024个字节。
		FileChannel.force()方法将通道里尚未写入磁盘的数据强制写到磁盘上。出于性能方面的考虑，操作系统会将数据缓存在内存中，所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法。
		force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
		下面的例子同时将文件数据和元数据强制写到磁盘上：
		channel.force(true);

	 */
	@Test
	public void startChannel(){
		
		try {
			RandomAccessFile file = new RandomAccessFile("d:"+File.separator+"stack.log","rw");
		     FileChannel channel = file.getChannel();
		     ByteBuffer bf = ByteBuffer.allocate(40);
		     /**
		      * 调用FileChannel.read()方法。该方法将数据从FileChannel读取到Buffer中。read()方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1
		      */
		     int tmp= channel.read(bf); 
		     while(tmp !=-1){
		    	 bf.flip();
		    	 while(bf.hasRemaining()){
		    	    System.out.println((char)bf.get());
		    	 }
		    	 /**
		    	  *  clear() 清楚总个buffer
		    	  */
		    	 bf.clear(); 
		    	 tmp = channel.read(bf);
		     }
		     channel.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * transferTo   从写入数据通道传数据到写数据通道
	 * transferForm  
	 */
	@Test
	public void aboutTransferForm(){
		
		try {
			RandomAccessFile formFile = new RandomAccessFile("d:"+File.separator+"stack.log","rw");
			RandomAccessFile toFile = new RandomAccessFile("d:"+File.separator+"to_stack.log","rw");
			FileChannel formChannel =formFile.getChannel();
			FileChannel toChannel =toFile.getChannel();
			formChannel.transferTo(0, formChannel.size(), toChannel);
//			toChannel.transferFrom(formChannel, 0, formChannel.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 字符编码解码 : 字节码本身只是一些数字，放到正确的上下文中被正确被解析。向 ByteBuffer 中存放数据时需要考虑字符集的编码方式，读取展示 ByteBuffer 数据时涉及对字符集解码
	 * 
	 */
	@Test
	public void aboutSocketChannel(){
		
		Charset cs = Charset.forName("GBK");
		try {
			InetSocketAddress sockedAddress= new InetSocketAddress("www.baidu.com",80);
			SocketChannel socket  = SocketChannel.open(sockedAddress);
			
			ByteBuffer bf = ByteBuffer.allocate(1024);
			socket.write(cs.encode("GET " + "/ HTTP/1.1" + "\r\n\r\n"));
			while(socket.read(bf) !=-1){
		    	 bf.flip();
		    	 System.out.println(cs.decode(bf));
		    	 /**
		    	  *  clear() 清楚总个buffer
		    	  */
		    	 bf.clear(); 
		     }
		     socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	@Test
	public void aboutSelector(){
		
		
	}
	
	
	
	@Test
	public void start(){
		
//		Charset
		FileChannel channel =null;
		try {
			FileInputStream file = new FileInputStream("D:"+File.separator+"stack.log");
			channel = file.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.read(buffer);
			buffer.flip();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(channel !=null){
				try {
					channel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * nio 
	 *  buffer  数据的读写操作，都是以缓冲为通道
	 *  channel
	 * 
	 * 
	 * @throws IOException
	 */
	@Test
	public  void copyFile() throws IOException{
		
		FileChannel readChannel = null;
		FileChannel writeChannel = null;
		try {
			FileInputStream read = new FileInputStream("D:"+File.separator+"stack.log");
			FileOutputStream out = new FileOutputStream("D:"+File.separator+"stack.log_bak");
			readChannel = read.getChannel();
			writeChannel =out.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(2);
			while(true){
				buffer.clear();
				int tmp = readChannel.read(buffer);
				System.out.println(tmp);
				if(tmp == -1){
					break;
				}
				buffer.flip();
				writeChannel.write(buffer);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			readChannel.close();
			writeChannel.close();
		}
	} 
	/**
	 * 关于buffer 的工作原理
	 *                              写                                                        读                                                      
	 * position（开始的位置）           将从position下一个位置开始写入         当前要读取数据的位置
	 * limit（当前的上限）                  缓存区的实际上限                                         可读取的缓存区容量，和上次写入的容易相等
	 * capactiy（缓存的容量）            初始化容器的大小
	 * 
	 * flip  
	 * 
	 * rewind()   重置postion=0；同时清楚mark
	 * clear()  将缓冲区设为初始化状态，也是 position =0，capacity=limit 
	 * flip()  position =0；limit =上一次数据操作position,为读写转换做准备
	 * 
	 */
	@Test
	public void aboutBuffer(){
		
		ByteBuffer buffer = ByteBuffer.allocate(15);
		System.out.println("position"+buffer.position()+";capacity:"+buffer.capacity()+";limit:"+buffer.limit());
		for(int i=0;i<10;i++){
			buffer.put((byte)i);
		}
		System.out.println("position"+buffer.position()+";capacity:"+buffer.capacity()+";limit:"+buffer.limit());
		/**
		 *flip 将会重置position,通常，buffer写模式要转化为读模式时，需要执行flip,flip 操作不仅重置了当前
		 *的position 为0，还将limit设置到当前position的位置，这样做是为了防止在读模式中，读到应用没有操作到的数据
		 *
		 */
		buffer.flip();
		System.out.println("position"+buffer.position()+";capacity:"+buffer.capacity()+";limit:"+buffer.limit());
		for(int i=0;i<5;i++){
			System.out.println(buffer.get());
		}
		System.out.println("position"+buffer.position()+";capacity:"+buffer.capacity()+";limit:"+buffer.limit());
		buffer.flip();
		System.out.println("position"+buffer.position()+";capacity:"+buffer.capacity()+";limit:"+buffer.limit());
		for(int i=0;i<10;i++){
			System.out.println(buffer.get());
		}
	}
	/**
	 * mark
	 * reset
	 */
	@Test
	public void aboutMark(){
		
		ByteBuffer buffer = ByteBuffer.allocate(20);
		for(int i=0;i<10;i++){
			if(i==5){
				buffer.mark();
			}
			buffer.put((byte)i);
		}
		buffer.reset();
		while(buffer.hasRemaining()){
			System.out.println(buffer.get());
		}
		
	}

	/**
	 * 关于散射和集聚
	 * 分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。 
                  聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
	 */
	@Test
	public void  aboutScatterAndGather(){
		
		try {
			// 聚集，就是将多个buffer写入同一个channel
			/**
			 * 汉字是2个byte
			 */
			ByteBuffer b1= ByteBuffer.wrap("王水民 0000".getBytes("utf-8"));
			ByteBuffer b2= ByteBuffer.wrap("start nio".getBytes("utf-8"));
			File file = new File("d:"+File.separator+"scatter.log");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			FileChannel channel = out.getChannel();
			ByteBuffer[] bs = new ByteBuffer[]{b1,b2}; 
			channel.write(bs);
			channel.close();
//			分散  读数据将数据读取到多个buffer 中
			ByteBuffer b3= ByteBuffer.allocate(6);
			ByteBuffer b4= ByteBuffer.allocate(20);              
			FileInputStream input = new FileInputStream("d:"+File.separator+"scatter.log");
			channel = input.getChannel();
			bs = new ByteBuffer[]{b3,b4}; 
			channel.read(bs);
			System.out.println(new String(bs[0].array(),"utf-8"));
			System.out.println(new String(bs[1].array(),"utf-8"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 关于buffer 内存映射
	 * 直接读取，并显示
	 * 
	 */
	@Test
	public void aboutMapped(){
		
		try {
			RandomAccessFile file = new RandomAccessFile("D:"+File.separator+"stack.log","rw");
            FileChannel channel=  file.getChannel();
            System.out.println(file.length());
            MappedByteBuffer mapped =channel.map(MapMode.READ_WRITE, 0, 1024);
//            while(mapped.hasRemaining()){
//            	System.out.println(mapped.get());
//            }
//            while(true){
//            	mapped.clear();
//            	int tmp =channel.read(mapped);
//            	if(tmp ==-1){
//            		break;
//            	}
//            	System.out.println(new String(mapped.array(),"utf-8"));
//            }
            channel.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
