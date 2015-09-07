package Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<E> {

	private int head = 0;
	private int tail = 0;
	private int count = 0;
	private E[] items;
	private final ReentrantLock lock;
	private final Condition notEmpty;
	private final Condition notFull;

	
	private int inc(int index)
	{
		return (++index == items.length) ? 0 : index;
	}
	
	public BlockingQueue(int capacity)
	{
		if (capacity <= 0)
		{
            throw new IllegalArgumentException();
		}
		items    = (E[]) new Object[capacity];
		lock     = new ReentrantLock(true);
		notEmpty = lock.newCondition();
		notFull  = lock.newCondition();
	}
	
	public void offer(E e)
	{
		if( e == null )
		{
			throw new NullPointerException();
		}
		
			try{
				lock.lock();
				while( count == items.length )
				{
					notFull.await();
				}
			}catch(InterruptedException ex)
			{
				offer(e);
			}
			
			items[ tail ] = e;
			tail = inc(tail);
			count++;
			notEmpty.signal();
			lock.unlock();

	}
	
	public  E poll()
	{
		E tmp;
			try{
				lock.lock();
				while( count == 0 )
				{
					notEmpty.await();
				}
			} catch(InterruptedException e)
			{
				return poll();
			}
			
			tmp =  items[ head ];
			items[ head ] = null;
			head = inc(head);
			count--;
			
			notFull.signal();
			lock.unlock();

		return tmp;
	}
}
