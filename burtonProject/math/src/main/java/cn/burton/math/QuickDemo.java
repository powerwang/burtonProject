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
	
	
}
