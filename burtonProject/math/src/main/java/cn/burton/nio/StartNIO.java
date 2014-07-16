package cn.burton.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.junit.Test;
/**
 * 
 * @author Administrator
 *
 * 关于缓冲的子缓存
 */
public class StartNIO {

	
	@Test
	public void start(){
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
	 */
	@Test
	public void aboutMapped(){
		
		try {
			RandomAccessFile file = new RandomAccessFile("D:"+File.separator+"stack.log","rw");
            FileChannel channel=  file.getChannel();
            System.out.println(file.length());
            MappedByteBuffer mapped =channel.map(MapMode.READ_WRITE, 0, file.length());
            while(mapped.hasRemaining()){
            	System.out.println(mapped.get());
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
}
