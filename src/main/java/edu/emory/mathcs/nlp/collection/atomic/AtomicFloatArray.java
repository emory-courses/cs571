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
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

import sun.misc.Unsafe;

/**
 * Took the implementation of {@link AtomicIntegerArray}.
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AtomicFloatArray implements Serializable
{
	private static final long serialVersionUID = 7984677869783562651L;
	// setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final int base = unsafe.arrayBaseOffset(float[].class);
    private static final int scale = unsafe.arrayIndexScale(float[].class);
    private final float[] array;

    private long rawIndex(int i)
    {
        if (i < 0 || i >= array.length)
            throw new IndexOutOfBoundsException("index " + i);
        return base + i * scale;
    }

    /**
     * Creates a new AtomicLongArray of given length.
     * @param length the length of the array
     */
    public AtomicFloatArray(int length)
    {
        array = new float[length];
        // must perform at least one volatile write to conform to JMM
        if (length > 0)
            unsafe.putFloatVolatile(array, rawIndex(0), 0);
    }

    /**
     * Creates a new AtomicLongArray with the same length as, and
     * all elements copied from, the given array.
     *
     * @param array the array to copy elements from
     * @throws NullPointerException if array is null
     */
    public AtomicFloatArray(float[] array)
    {
        if (array == null)
            throw new NullPointerException();
        int length = array.length;
        this.array = new float[length];
        if (length > 0) {
            int last = length-1;
            for (int i = 0; i < last; ++i)
                this.array[i] = array[i];
            // Do the last write as volatile
            unsafe.putFloatVolatile(this.array, rawIndex(last), array[last]);
        }
    }

    /**
     * Returns the length of the array.
     * @return the length of the array
     */
    public final int length()
    {
        return array.length;
    }

    /**
     * Gets the current value at position {@code i}.
     * @param i the index
     * @return the current value
     */
    public final float get(int i) {
        return unsafe.getFloatVolatile(array, rawIndex(i));
    }

    /**
     * Sets the element at position {@code i} to the given value.
     * @param i the index
     * @param newValue the new value
     */
    public final void set(int i, float newValue)
    {
        unsafe.putFloatVolatile(array, rawIndex(i), newValue);
    }

    /**
     * Eventually sets the element at position {@code i} to the given value.
     * @param i the index
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(int i, float newValue)
    {
        unsafe.putOrderedObject(array, rawIndex(i), newValue);
    }


    /**
     * Atomically sets the element at position {@code i} to the given value and returns the old value.
     * @param i the index
     * @param newValue the new value
     * @return the previous value
     */
    public final float getAndSet(int i, float newValue)
    {
        while (true)
        {
        	float current = get(i);
            if (compareAndSet(i, current, newValue))
                return current;
        }
    }

    /**
     * Atomically sets the value to the given updated value if the current value {@code ==} the expected value.
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(int i, float expect, float update)
    {
        return unsafe.compareAndSwapObject(array, rawIndex(i), expect, update);
    }

    /**
     * Atomically sets the value to the given updated value if the current value {@code ==} the expected value.
     * <p>May <a href="package-summary.html#Spurious">fail spuriously</a>
     * and does not provide ordering guarantees, so is only rarely an
     * appropriate alternative to {@code compareAndSet}.
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true if successful.
     */
    public final boolean weakCompareAndSet(int i, float expect, float update)
    {
        return compareAndSet(i, expect, update);
    }

    /**
     * Atomically adds the given value to the element at index {@code i}.
     * @param i the index
     * @param delta the value to add
     * @return the previous value
     */
    public final float getAndAdd(int i, float delta)
    {
        while (true)
        {
        	float current = get(i);
        	float next = current + delta;
            if (compareAndSet(i, current, next))
                return current;
        }
    }

    /**
     * Atomically adds the given value to the element at index {@code i}.
     * @param i the index
     * @param delta the value to add
     * @return the updated value
     */
    public float addAndGet(int i, float delta)
    {
        while (true)
        {
        	float current = get(i);
        	float next = current + delta;
            if (compareAndSet(i, current, next))
                return next;
        }
    }

    /**
     * Returns the String representation of the current values of array.
     * @return the String representation of the current values of array.
     */
    public String toString()
    {
        if (array.length > 0) // force volatile read
            get(0);
        return Arrays.toString(array);
    }

}