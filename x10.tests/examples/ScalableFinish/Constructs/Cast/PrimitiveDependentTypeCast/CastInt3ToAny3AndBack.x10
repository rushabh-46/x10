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
 * Purpose: Checks boxing/unboxing works properly.
 * @author vcave
 * @author vj -- Changed to X10 2.0
 **/
 public class CastInt3ToAny3AndBack   {

   public def run(): boolean = {      
      var obj: Any(3) = 3;
      val i = obj as Int(3);
      return true;
   }

   public static def main(Rail[String]) {
      new CastInt3ToAny3AndBack().run ();
   }
}
