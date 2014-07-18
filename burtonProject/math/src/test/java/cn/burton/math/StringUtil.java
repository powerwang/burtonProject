package cn.burton.math;

import org.junit.Test;

/**
 * Description:字符串相关工具类
 * 
 * @author gu.kaiming
 * @version 1.0
 */
public class StringUtil {

	@Test
	public void test(){
		String str="<scrpit>alert</script>";
		
		System.out.println(StringUtil.filterParam(str));
	}
	
	public static String filterParam(String message) {

//		String regex="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		String regex="[<>\"%;()+]";
		return message.replaceAll(regex," ");
		
//		message = message.replace('<', ' ');
//		message = message.replace('>', ' ');
//		message = message.replace('"', ' ');
//		message = message.replace('\'', ' ');
//		message = message.replace('%', ' ');
//		message = message.replace(';', ' ');
//		message = message.replace('(', ' ');
//		message = message.replace(')', ' ');
//		message = message.replace('+', '_');
//		return message;
	}

}
