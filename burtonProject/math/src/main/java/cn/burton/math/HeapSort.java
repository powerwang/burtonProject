package cn.burton.math;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author wangshuimin
 * @addTime 2014年7月3日 下午1:54:35
 * @updateTime  
 * @说明
 * 
 * 堆排序---二叉树
 * 高度 --lgn ????
 * 
 * parent[i] return i/2   left[i] return 2i right[i] return 2i+1
 * heapsort 运行时间 n*lgn 
 *
 *当父结点的键值总是大于或等于任何一个子节点的键值时为最大堆。
 *当父结点的键值总是小于或等于任何一个子节点的键值时为最小堆
 */
public class HeapSort {

	@Test
	public void test(){
		int s[] =new int[]{1,10,5,90,15,75,3,74,2};
		MakeMinHeap(s, 8);
		System.out.println(Arrays.toString(s));
	}
	
	
	void MakeMinHeap(int a[], int n) {  
	    for (int i = n / 2 - 1; i >= 0; i--)  
	        MinHeapFixdown(a, i, n);  
	}  
	
	void MinHeapFixdown(int a[], int i, int n)  
	{  
	    int j, temp;  
	  
	    temp = a[i];  
	    j = 2 * i + 1;  
	    while (j < n)  
	    {  
	        if (j + 1 < n && a[j + 1] < a[j]) //在左右孩子中找最小的  
	            j++;  
	  
	        if (a[j] >= temp)  
	            break;  
	  
	        a[i] = a[j];     //把较小的子结点往上移动,替换它的父结点  
	        i = j;  
	        j = 2 * i + 1;  
	    }  
	    a[i] = temp;  
	} 
	

}
