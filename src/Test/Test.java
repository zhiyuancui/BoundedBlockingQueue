package Test;

import java.util.concurrent.ArrayBlockingQueue;

public class Test {
	
	public static void main(String[] args)
	{
		BlockingQueue<Integer> q = new BlockingQueue<Integer>(3);
		new Thread(new Runnable() {
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
        }).start();

		//Thread for producer1
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    try {
                        Thread.sleep(1000);
                        q.offer(i);
                        System.out.println("Producer1 added: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
		        
      //Thread for producer2        
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    try {
                        Thread.sleep(1000);
                        q.offer(i+10);
                        System.out.println("Producer2 added: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
		
	}
}
