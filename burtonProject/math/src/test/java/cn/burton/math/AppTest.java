package cn.burton.math;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest {
   
	@org.junit.Test
	public void test(){
		
		int a[] = new int[]{1,2,4};
		int i=0;
		a[i++]=5;
		System.out.println(a[i]);
		System.out.println(Arrays.toString(a));
	}
}
