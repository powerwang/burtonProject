package cn.burton.thread;
/**
 * 
 * @author wangshuimin
 * @addTime 2014年7月3日 下午5:59:18
 * @updateTime  
 * @说明
 * 
 * 
 */
public class Wait {

	public static void main(String[] args) {
		
		Message msg= new Message();
		Thread t = new Thread(new WaitThread(msg));
		t.start();
		Thread t1 = new Thread(new NotifyThread(msg));
		t1.start();
	}
	
}


class NotifyThread implements Runnable{

	Message msg;
	
	public NotifyThread(Message msg) {
		// TODO Auto-generated constructor stub
		this.msg =msg;
	}
	public void run() {
		// TODO Auto-generated method stub
		    try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (msg) {
				msg.notifyAll();
				System.out.println(msg.getResult());
			}
	}
	
	
}

class WaitThread implements Runnable{

	Message msg;
	
	public WaitThread(Message msg) {
		// TODO Auto-generated constructor stub
		this.msg =msg;
	}
	public void run() {
		// TODO Auto-generated method stub
		try {
			synchronized (msg) {
				msg.setResult(Thread.currentThread().getName()+"传递过来的通知");
				msg.wait();
				System.out.println("wait 释放");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}


class Message{
	
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}