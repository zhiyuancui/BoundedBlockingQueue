/**
 * Date: 09/06/2015
 * Description: A implementation of bounded blocking queue with fair lock.
 * Plarform: Windows 8
 * Last Modified:
 */

package Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<E> {

	/** the head index for poll the next element*/
	private int head = 0;
	/** the tail index for offer the next element in the queue*/
	private int tail = 0;
	/** number of items in the queue*/
	private int count = 0;
	/** the queued item*/
	private E[] items;
	
	
	/** Main lock guarding all access */
    private final ReentrantLock lock;
    /** Condition for waiting takes */
    private final Condition notEmpty;
    /** Condition for waiting puts */
    private final Condition notFull;

	/**
	 * increase the index of queue
	 * @param index
	 * @return 0 if the index meet the end of queue, otherwise return index++
	 */
	private int inc(int index)
	{
		return (++index == items.length) ? 0 : index;
	}
	
	
	/**
	 * Create a Bounded Blocking queue with fixed capacity
	 * and access policy
	 * @param capacity: the size of queue
	 * @param fair: access policy
	 */
	public BlockingQueue(int capacity, boolean fair)
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
	
	
	/**
	 * Create a Bounded Blocking queue with fixed capacity
	 * and default access policy
	 * @param capacity: the size of queue
	 * @param fair: default is true
	 */
	public BlockingQueue(int capacity)
	{
		this(capacity,true);
	}
	
	
	/**
	 * Put an item at the tail of queue.
	 * If the queue is full, it will wait for available space
	 * @param e: item will be put into queue
	 */
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
