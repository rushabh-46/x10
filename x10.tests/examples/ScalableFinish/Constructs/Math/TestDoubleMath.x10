/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2006-2010.
 *  (C) Copyright Australian National University 2009-2010.
 */

 

/**
 * Test x10.lang.Math operations for Double operands.
 * @author milthorpe
 */
class TestDoubleMath   {
    public def run(): boolean {
        // error function
        val a = 0.6;
        // note in general this relationship does not hold to machine precision
        // if this is problematic on too many platforms, consider changing to 
        chk (Math.erf(a) == 1.0 - Math.erfc(a));

        chk (Math.erf(0) == 0.0);
        chk (Math.erfc(0) == 1.0);
        chk (Math.erf(Double.POSITIVE_INFINITY) == 1.0);
        chk (Math.erfc(Double.POSITIVE_INFINITY) == 0.0);

        return true;
    }

    public static def main(Rail[String]) {
        new TestDoubleMath().run ();
    }

}
