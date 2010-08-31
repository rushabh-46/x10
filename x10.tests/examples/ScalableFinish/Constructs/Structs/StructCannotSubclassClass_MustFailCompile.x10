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
 * A struct cannot subclass a class.
 * @author vj
 */
public class StructCannotSubclassClass_MustFailCompile   {

	class B {}
    struct A extends B {
	 val x:int=5;
    }

    public def run()=true;

    public static def main(Rail[String])  {
	new StructCannotSubclassClass_MustFailCompile().run ();
    }
}
