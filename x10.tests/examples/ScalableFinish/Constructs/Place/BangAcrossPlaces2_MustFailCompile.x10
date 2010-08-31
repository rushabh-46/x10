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

//OPTIONS: -STATIC_CALLS

 

/**
 * Cannot reference a banged method parameter across a place-shift.
 *
 * 
 * @author vj
 */
class BangAcrossPlaces2_MustFailCompile    {
	class C {
		var x:Int=0;
		def x() =x;
	}
	def m(z:C!) {
		at (here.next()) {
			// this should generate an error.
			val y = z.x();
		}
	}
    public def run() {
    	m(new C());
    	return true;
    }
    public static def main(Rail[String]) {
	  new 
	  BangAcrossPlaces2_MustFailCompile().run ();
    }

}
