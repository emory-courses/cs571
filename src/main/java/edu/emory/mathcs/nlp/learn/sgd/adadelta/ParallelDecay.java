package edu.emory.mathcs.nlp.learn.sgd.adadelta;

/**
 * Created by Mike on 9/8/2015.
 */

public class ParallelDecay extends Thread {

    private float[] arr;
    private float mult;
    private int low, high;

    public ParallelDecay(float[] arr, float mult, int low, int high)
    {
        this.arr = arr;
        this.mult = mult;
        this.low = low;
        this.high = Math.min(high, arr.length);
    }

    public void run()
    {
        decay(arr, mult, low, high);
    }
    public static void decay(float[] arr, float mult)
    {
        decay(arr, mult, 0, arr.length);
    }

    public static void decay(float[] arr, float mult, int low, int high)
    {
        for (int i = low; i < high; i++)
            arr[i]*=mult;
    }

    public static float[] parallelDecay(float[] arr, float mult)
    {
        return parallelDecay(arr, mult, Runtime.getRuntime().availableProcessors());
    }

    public static float[] parallelDecay(float[] arr, float mult, int threads)
    {
        int size = (int) Math.ceil(arr.length * 1.0 / threads);

        ParallelDecay[] products = new ParallelDecay[threads];

        for (int i = 0; i < threads; i++) {
            products[i] = new ParallelDecay(arr, mult, i * size, (i + 1) * size);
            products[i].start();
        }

        try {
            for (ParallelDecay product : products) {
                product.join();
            }
        } catch (InterruptedException e) { }

        return arr;
    }

}