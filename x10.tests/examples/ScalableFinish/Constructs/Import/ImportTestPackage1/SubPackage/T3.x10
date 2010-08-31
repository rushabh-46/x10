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

package ImportTestPackage1.SubPackage;

import ImportTestPackage2._T4;
 

/**
 * auxiliary class for ImportTest, also a test by itself.
 */
public class T3   {
	public static def m3(val x: int): boolean = {
		return (future(here) { _T4.m4(x) }).force();
	}
	public def run(): boolean = {
		return m3(49);
	}

	public static def main(var args: Rail[String]): void = {
		new T3().run ();
	}
}
