package cn.burton.thread;

/**
 * 
 * @author wangshuimin
 * @addTime 2014年7月1日 下午3:53:18
 * @updateTime  
 * @说明
 * 使用join必须在 线程start 之后调用join 才有效
 *  join 作用： 一个线程执行结束，才执行下一个线程
 * 
 * 
 */
public class JoinTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		
		Thread t = new Thread(new JoinDemo());
		Thread t2 = new Thread(new JoinDemo());
		t.start();
		t.join(); // 线程t 执行完成才会执行 t2
		t2.start();
	}

}

class JoinDemo implements  Runnable {
    
	int j;
	
	public void run() {
	     
		for(int i=0;i<10;i++){
			j++;
			System.out.println(Thread.currentThread().getName()+"==>>"+j);
		}
	}
}
