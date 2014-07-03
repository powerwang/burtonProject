package cn.burton.math;

import java.util.Arrays;

import org.junit.Test;

public class BubDemo {

	@Test
	public void bub(){
		// 使用冒泡排序，从小到大 ,每次把最大（最小）的一个数往一边推，使一边都是最值
		int a[] =new int[]{1,10,5,90,15,75,3,74,2};
		int z=a.length;
		int w=0;
		for(int i=0;i<z;i++){
			for(int j=0;j<z-i;j++){
				if((j <z-i-1) && a[j]>a[j+1]){
					int tmp=a[j+1];
					a[j+1]=a[j];
					a[j]=tmp;
				}
				w++;
			}
		}
		System.out.println(Arrays.toString(a));
		System.out.println(w);
		
	}
}
