package cn.burton.thread.blockQueue;

import java.util.LinkedList;
import java.util.List;

public class QueueWait {

	private int size =10;
	
	private List<Object> queue = new LinkedList<Object>();

	/**
	 * 入队列
	 * @author wangshuimin
	 * @param task
	 * @throws InterruptedException 
	 * @说明
	 */
	public synchronized void enqueue(String task) throws InterruptedException{
		// 1、当队列数达到上限，wait,不给如队列
		while(queue.size() ==this.size){
			wait();
		}
		if(queue.size() == 0){
			notifyAll();
		}
		queue.add(task);
	}
	/**
	 * 
	 * @author wangshuimin
	 * @return
	 * @throws InterruptedException
	 * @说明
	 * 
	 */
	public synchronized  Object dequeue() throws InterruptedException{
		
		 while(queue.size() == 0){
			 wait();
		 }
		 if(queue.size() == size){
			 notifyAll();
		 }
		 return queue.remove(0);
	} 
	
	
	
}
