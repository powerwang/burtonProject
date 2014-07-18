package cn.burton.nio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.junit.Test;
/**
 * 
 * @author Administrator
 * 关于io 流的装饰着模式？？？？
 * http://blog.csdn.net/hepeng19861212/article/details/4457023
 * 
 * 
 */
public class CompareNio {

	private final static int num = 4000000;
	
	@Test
	public void ioWriteest(){
		
		
		DataOutputStream out =null;
		try {
		    out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("d:"+File.separator+"io.log")));
		    for(int i=0;i<num;i++){
		    	out.writeByte(i);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * java.io.EOFException
	 * http://lavasoft.51cto.com/62575/235269
	 * 
	 */
	@Test
	public void ioReadest(){

		DataInputStream read =null;
		try {
			read = new DataInputStream(new BufferedInputStream(new FileInputStream("d:"+File.separator+"io.log")));
			int readNum=0;
			while(true){
				int tmp =  read.read();
				if(tmp ==-1){
					break;
				}
				readNum ++;
//				System.out.println(tmp);
			}
             System.out.println(readNum);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(read !=null){
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void nioBufferWriteest(){
		FileChannel channel =null;
		try {
			 FileOutputStream file =   new FileOutputStream("d:"+File.separator+"bufferNIO.log");
		     channel= file.getChannel();
		     ByteBuffer buffer = ByteBuffer.allocate(1000000);
//		     Charset  cs = Charset.forName("utf-8");
//		     CharsetDecoder  decoder = cs.newDecoder();
		     String code = System.getProperty("file.encoding");
		     System.out.println(code);
//		     for(int i=1;i<1000;i++){
//		    	 System.out.println(int2byte(i)[2]);
//		    	 buffer.put(int2byte(i));
		    	 buffer.put((byte) 1);
//		     }
//		     decoder.decode(buffer);
		     buffer.flip();
		     channel.write(buffer);
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
	
	
	@Test
	public void nioBufferReadTest(){
		FileChannel channel =null;
		try {
			 FileInputStream file =   new FileInputStream("d:"+File.separator+"bufferNIO.log");
		     channel= file.getChannel();
		     ByteBuffer buffer = ByteBuffer.allocate(num*4);

		     channel.read(buffer);
		     channel.close();
		     buffer.flip();
		     while(buffer.hasRemaining()){
		    	 System.out.println(buffer.get());
//		    	 byte2int(buffer.get(),buffer.get(),buffer.get(),buffer.get());
		     }
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
	
	@Test
	public void  mappedWriteTest(){
		
		try {
			RandomAccessFile file = new RandomAccessFile("d:"+File.separator+"bufferMappedNIO.log", "rw");
		    FileChannel channel =file.getChannel();
		    MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, num*4);
		    for(int i=0;i<num;i++){
		    	buffer.put(int2byte(i));
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
	
	@Test
	public void  mappedReadTest(){
		
		try {
			FileInputStream input = new FileInputStream("d:"+File.separator+"bufferMappedNIO.log");
			FileChannel channel =input.getChannel();
		    MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		    while(buffer.hasRemaining()){
		    	buffer.get();
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
	
	
	
    public static byte[] int2byte(int res){
    	byte[] target = new byte[4];
    	target[0] =(byte) ((res >>> 24) & 0xFF);
    	target[1] =(byte) ((res >>> 16) & 0xFF);
    	target[2] =(byte) ((res >>>  8) & 0xFF);
    	target[3] =(byte) ((res >>>  0) & 0xFF);
    	return target;
    }
    
    public static int byte2int(byte ch1,byte ch2,byte ch3,byte ch4){
    	
    	return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

}
