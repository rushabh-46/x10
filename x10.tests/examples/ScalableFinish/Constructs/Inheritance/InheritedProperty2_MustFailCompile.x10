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
 * Test that if a class implements an interface, and the interface specifies a property, 
 * then the class defines the property, and not just another nullary method with the same name.
 * @author vj
 */
public class InheritedProperty2_MustFailCompile   { 

    public static interface Test (l:int) {
      def put():int;
    }
    
    // fail here
    class Tester  /*(l:int)*/ implements Test {
      public def this(arg:int):Tester {  }
      public /*safe property*/ def l():int = 0;
      public def put()=0;
    }
 
    public def run()=false;
    
    public static def main(Rail[String]) {
      new InheritedProperty2_MustFailCompile().run ();
    }
}
