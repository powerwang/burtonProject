package cn.burton.nio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * 关于io 的操作
 * 
 * inputStream outputStream 
 * writer  read
 * 
 * @author Administrator
 *
 */
public class AboutIo {
 
	/**
	 * 关于bufferedRead 的readLine
	 */
	@Test
	public void bufferRead(){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("d:"+File.separator+"stack.log"),Charset.forName("UTF-8")));
			while(true){
				String line =br.readLine();
				if(line == null ){
					break;
				}
				System.out.println(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
