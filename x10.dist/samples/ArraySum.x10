/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2006-2010.
 */

import x10.io.Console;

/**
 * A simple illustration of loop parallelization within a single place.
 */
public class ArraySum {

    var sum: Int;
    val size: Int;
    val data: Rail[Int]!;
    val R:Region{rail};

    public def this(n: Int) {
        size=n;
        R= 0..n-1 as Region{rail};
        data = Rail.make[Int](n, (x:int)=>1);
        // for ((i) in R) S executes S for each point in R.
        // R must be a 1-d region. (i) decomposes the 1-d point
        // to retrieve the index in the 0th dimension.
        // Thus for iteration over a 2d point, you would use
        // the idiom for ((i,j) in R) S
        // The syntax for (p in R) S will also work, but p
        // will be bound to the points in R. 
        sum=0;
    }

    def sum(a: Rail[Int]!, start: Int, last: Int): Int = {
        var mySum: Int = 0;
        for ((i) in start..last-1) mySum += a(i);
        return mySum;
    }

    def sum(numThreads: Int) {
        val mySize = size/numThreads;
        finish foreach ((p) in 0..numThreads-1) {
            val mySum = sum(data, p*mySize, (p+1)*mySize);
            // Multiple activities will simultaneously update
            // this location -- so use an atomic operation.
            atomic sum += mySum;
        }
    }
    
    public static def main(args: Rail[String]!) {

        var size:Int = 5*1000*1000;
        if (args.length >=1)
            size = Int.parse(args(0));

        Console.OUT.println("Initializing.");
        val a = new ArraySum(size);
        val P = [1,2,4];

        //warmup loop
        val R = 0..P.length-1;
        Console.OUT.println("Warming up.");
        for ((i) in R)
            a.sum(P(i));
        
        for ((i) in R) {
            Console.OUT.println("Starting with " + P(i) + " threads.");
            a.sum=0;
            var time: long = - System.nanoTime();
            a.sum(P(i));
            time += System.nanoTime();
            Console.OUT.println("For p=" + P(i) 
                    + " result: " + a.sum 
                    + ((size==a.sum)? " ok" : "  bad") 
                    + " (time=" + (time/(1000*1000)) + " ms)");
        }
        
        
    }
}
