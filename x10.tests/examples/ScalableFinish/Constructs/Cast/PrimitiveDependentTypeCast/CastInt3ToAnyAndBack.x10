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
 * Purpose: Checks a constrained cast leading to primitive unboxing works 
 *          actually checks the unboxed primitive.
 * @author vcave
 * @author vj  -- Moved to X10 2.0 and renamed
 **/
 public class CastInt3ToAnyAndBack   {

   public def run() {
      val x = mth() as Int(3);
      return true;
   }
   
   public def mth()=3 as Any;
   public static def main(Rail[String]) {
      new CastInt3ToAnyAndBack().run ();
   }
}
