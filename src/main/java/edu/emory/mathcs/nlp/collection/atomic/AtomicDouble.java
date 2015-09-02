/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.collection.atomic;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.Unsafe;

/**
 * Took the implementation of {@link AtomicInteger}.
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AtomicDouble extends Number implements Serializable {
	private static final long serialVersionUID = -7625330960151259225L;
	// setup to use Unsafe.compareAndSwapInt for updates
	private static final Unsafe unsafe = Unsafe.getUnsafe();
	private static final long valueOffset;
	
	static
	{
		try
		{
			valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
		}
		catch (Exception ex) { throw new Error(ex); }
	}
	
	private volatile double value;

    /**
     * Creates a new AtomicInteger with the given initial value.
     * @param initialValue the initial value
     */
    public AtomicDouble(double initialValue)
    {
        value = initialValue;
    }

    /** Creates a new AtomicInteger with initial value {@code 0}. */
    public AtomicDouble() {}

    /**
     * Gets the current value.
     * @return the current value
     */
    public final double get()
    {
        return value;
    }

    /**
     * Sets to the given value.
     * @param newValue the new value
     */
    public final void set(double newValue)
    {
        value = newValue;
    }

    /**
     * Eventually sets to the given value.
     * @param newValue the new value
     */
    public final void lazySet(double newValue)
    {
    	unsafe.putOrderedObject(this, valueOffset, newValue);
    }

    /**
     * Atomically sets to the given value and returns the old value.
     * @param newValue the new value
     * @return the previous value
     */
    public final double getAndSet(double newValue)
    {
        for (;;)
        {
        	double current = get();
            if (compareAndSet(current, newValue)) return current;
        }
    }

    /**
     * Atomically sets the value to the given updated value if the current value {@code ==} the expected value.
     * @param expect the expected value
     * @param update the new value
     * @return true if successful. False return indicates that the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(double expect, double update)
    {
    	return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }

    /**
     * Atomically sets the value to the given updated value if the current value {@code ==} the expected value.
     * <p>May <a href="package-summary.html#Spurious">fail spuriously</a> and does not provide ordering guarantees, so is only rarely an appropriate alternative to {@code compareAndSet}.
     * @param expect the expected value
     * @param update the new value
     * @return true if successful.
     */
    public final boolean weakCompareAndSet(double expect, double update)
    {
        return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }

    /**
     * Atomically adds the given value to the current value.
     * @param delta the value to add
     * @return the previous value
     */
    public final double getAndAdd(double delta)
    {
        for (;;)
        {
            double current = get();
            double next = current + delta;
            if (compareAndSet(current, next)) return current;
        }
    }

    /**
     * Atomically adds the given value to the current value.
     * @param delta the value to add
     * @return the updated value
     */
    public final double addAndGet(double delta)
    {
        for (;;)
        {
            double current = get();
            double next = current + delta;
            if (compareAndSet(current, next)) return next;
        }
    }

    public String toString()
    {
        return Double.toString(get());
    }

    public int intValue()
    {
        return (int)get();
    }

    public long longValue()
    {
        return (long)get();
    }

    public float floatValue()
    {
        return (float)get();
    }

    public double doubleValue()
    {
        return get();
    }
}