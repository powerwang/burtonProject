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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;
/**
 * 
 * @author Administrator
 * 关于io 流的装饰着模式？？？？
 * 
 */
public class CompareNio {

	private final static int num = 4000000;
	
	@Test
	public void ioWriteest(){
		
		
		DataOutputStream out =null;
		try {
//			new  OutputStreamWriter(out, charsetName)
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
	
	@Test
	public void ioReadest(){
		
		
		DataInputStream read =null;
		try {
			read = new DataInputStream(new BufferedInputStream(new FileInputStream(new File("d:"+File.separator+"io.log"))));
			for(int i=0;i<num;i++){
		    	 int tmp= read.readInt();
//		    	 System.out.println(tmp);
		    }
		    
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
		     ByteBuffer buffer = ByteBuffer.allocate(num*4);
		     for(int i=0;i<num;i++){
		    	 buffer.put(int2byte(i));
		     }
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
		    	 byte2int(buffer.get(),buffer.get(),buffer.get(),buffer.get());
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
