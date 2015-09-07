/**
 * Date: 09/06/2015
 * Description: A implementation of bounded blocking queue 
 * with fair lock. Bounded means this queue is a queue with fixed
 * size. Blocking means when this queue is full, it will block all 
 * the adding thread until there is available space meanwhile when 
 * this queue is empty, the removing thread will be blocked until 
 * next item been put into queue.
 * Last Modified:09/06/2015
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
    private final Condition isEmpty;
    /** Condition for waiting puts */
    private final Condition isFull;

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
		isEmpty = lock.newCondition();
		isFull  = lock.newCondition();
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
				isFull.await();
			}
		}catch(InterruptedException ex)
		{
			offer(e);
		}
		
		items[ tail ] = e;
		tail = inc(tail);
		count++;
		isEmpty.signal();
		lock.unlock();

	}
		
	/**
	 * Remove an item from the head of queue.
	 * If the queue is empty, it will wait for available item.
	 * @return E the item been removed.
	 */
	public  E poll()
	{
		E tmp;
		try{
			lock.lock();
			while( count == 0 )
			{
				isEmpty.await();
			}
		} catch(InterruptedException e)
		{
			return poll();
		}
		
		tmp =  items[ head ];
		items[ head ] = null;
		head = inc(head);
		count--;
		
		isFull.signal();
		lock.unlock();

		return tmp;
	}
}
