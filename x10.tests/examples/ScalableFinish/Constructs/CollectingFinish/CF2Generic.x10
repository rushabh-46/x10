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

 

/**
 * Check you can offer the result of a method invocation, where the reducer is of a generic type.
 * @author vj
 */
public class CF2Generic   {

    static struct Reducer implements Reducible[Int] {
    	safe public global def zero()=0;
    	safe public global def apply(a:Int,b:Int)=a+b;
    }
    static class C[T] {
    	def m(b:T)=b;
        def m(a:Reducible[T], b:T):T {
      	  val x = finish(a) {
      		  offer m(b);
      		  offer m(b);
      	  };
      	  return x;
        }
        
  	}
	public def run() = 6==new C[Int]().m(Reducer(), 3);

	public static def main(Rail[String]) {
		new CF2Generic().run ();
	}
}
