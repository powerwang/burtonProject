package cn.burton.math;
/**
 * 插入排序
 * @author Administrator
 *
 */
public class InsertSort {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[] nums= new double[]{9,5,10,2,6,7};
		InsertSort.insertForm(nums);
		for(int i=0;i<nums.length;i++){
			System.out.println(nums[i]);
		}
	}
     // demo sort asc
	public static void insertForm(double[] sorted){
		int sortedLen =sorted.length; 
		for(int j=1;j<sortedLen;j++){  //从第二项开始
			if(sorted[j]<sorted[j-1]){  //先判断检查点与上一项的关系，不符合升序，则开始寻找插入点
				double key =sorted[j];  // 先将检查点值保存到临时空间
				int i=j-1;              
				while(i>=0 && sorted[i]>key){  // 从右边向左边检查，逐项想右移
					sorted[i+1]=sorted[i];
					i--;
				}
				sorted[i+1]=key;  // 向右移动结束，找到插入点，就是i+1这个位置
			}
		}
	}
	
	
	
}
