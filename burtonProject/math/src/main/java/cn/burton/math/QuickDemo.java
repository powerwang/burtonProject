package cn.burton.math;

import java.util.Arrays;

import org.junit.Test;
/**\
 * 
 * @author wangshuimin
 * @addTime 2014年7月3日 下午4:32:13
 * @updateTime  
 * @说明
 * 快速排序，在大量数据时，排序效率最高
 * 其时间复杂度 n*lgn
 * 分析：将数组A[p,r]被划分成两个（可能为空） 子数组A[p,q-1]和A[q+1,r],使得A[p,q-1]中的每个元素都小于等于
 * A[q],而且，小于等于A[q-1,r]中的元素，小标q也在这个划分过程中计算。
 * 通过递归调用快速排序，对子数组[p,q-1][q+1,r]排序。
 * 
 * 
 */
public class QuickDemo {

	@Test
	public void quick(){
		int s[] =new int[]{1,10,5,90,15,75,3,74,2};
		int start=0;
		int end=s.length-1;
	    quick_sort(s, start, end);
	    System.out.println(Arrays.toString(s));
	}
	/**
	 * 
	 * @author wangshuimin
	 * @param s
	 * @param start
	 * @param end
	 * @说明
	 * 1、使用两个指针low、high,分别设置为序列的头、尾，设置中间轴为第一个记录。
	 * 2、high开始向左寻找第一个小于pivotKey，并与之交换。前提（low<high） 
	 * 3、low 开始向右寻找第一个大于povotkey,并与之交换。 前提（low<high） 
	 * 4、直到low=high
	 */
	void quick_sort(int s[], int start, int end)
	{
	    if (start < end)
	    {
//	    	4,10,5,90,15,75,3,74,2
	        int i = start, j = end, key = s[start];
	        while (i < j){
	            while(i < j && s[j] >= key) {// 从右向左找第一个小于x的数
					j--;
	            }	
	            if(i < j){ 
					s[i++] = s[j];
	            }
	            while(i < j && s[i] < key){ // 从左向右找第一个大于等于x的数
					i++;  
	            }	
	            if(i < j) {
					s[j--] = s[i];
	            }	
	        }
	        s[i] = key;
	        quick_sort(s, start, i - 1); // 递归调用 
	        quick_sort(s, i + 1, end);
	    }     
	}
	
	
}
