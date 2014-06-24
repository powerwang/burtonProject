package cn.burton.math;

import java.util.Arrays;

import org.junit.Test;

public class QuickDemo {

	@Test
	public void quick(){
		int s[] =new int[]{1,10,5,90,15,75,3,74,2};
		int l=0;
		int r=s.length-1;
	    quick_sort(s, l, r);
	    System.out.println(Arrays.toString(s));
	}
	
	void quick_sort(int s[], int l, int r)
	{
	    if (l < r)
	    {
			//Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1
	        int i = l, j = r, x = s[l];
	        while (i < j)
	        {
	            while(i < j && s[j] >= x) // 从右向左找第一个小于x的数
					j--;  
	            if(i < j) 
					s[i++] = s[j];
				
	            while(i < j && s[i] < x) // 从左向右找第一个大于等于x的数
					i++;  
	            if(i < j) 
					s[j--] = s[i];
	        }
	        s[i] = x;
	        quick_sort(s, l, i - 1); // 递归调用 
	        quick_sort(s, i + 1, r);
	    }     
	}
	
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
