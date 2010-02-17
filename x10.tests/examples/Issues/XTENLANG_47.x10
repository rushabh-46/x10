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

import harness.x10Test;

/**
 * @author bdlucas 10/2008
 */

class XTENLANG_47 extends x10Test {

    class C(rank:int) {
        def this(r:int) = property(r);
    }
    
    class CL(rank2:int) {
    
        private class It implements Iterator[C{self.rank==rank2}] {
            incomplete public def hasNext(): boolean;
            incomplete public def next(): C{self.rank==rank2};
            incomplete public def remove(): void;
        }
    
        def this(r:int) = property(r);
    }

    public def run(): boolean {
        return true;
    }

    public static def main(Rail[String]) {
        new XTENLANG_47().execute();
    }
}
