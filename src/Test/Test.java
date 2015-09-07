/**
 * Date: 09/06/2015
 * Description: This class is used to run bounded blocking queue.
 * Last Modified:09/06/2015
 */
package Test;


public class Test {
	
	/** the bounded blocking queue instance*/
	private BlockingQueue<Integer> q = new BlockingQueue<Integer>(3);
	
	/** the Producer class*/
	class Producer extends Thread
	{
		int count = 0;
		public Producer(int c)
		{
			count = c;
		}
		
		@Override
	    public void run() {
	        for (int i = 0; i < 4; i++) {
	            try {
	                Thread.sleep(1000);
	                q.offer(count++);
	                System.out.println("Producer added: ");
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	/** the Consumer class*/	
	class Consumer extends Thread
	{
		@Override
	    public void run() {
	        for (int i = 0; i < 8; i++) {
	            try {
	                Thread.sleep(3500);
	                System.out.println("Consumer removed: "+q.poll());
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            
	        }
		}
	}
	
	
	
	
	public static void main(String[] args)
	{
		Test t = new Test();
		Producer p1 = t.new Producer(1);
		Producer p2 = t.new Producer(20);
		Consumer c1 = t.new Consumer();
		
		p1.start();
		p2.start();
		c1.start();
	}
}
