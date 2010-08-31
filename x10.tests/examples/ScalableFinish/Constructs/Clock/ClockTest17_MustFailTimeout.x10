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
 * TODO: rename to ClockTest17 and check for exception

 * OLD SEMANTICS:
 * Clarification of language definition needed here:
 * Whether async clocked(c) S occurs inside a finish
 * body is hard to detect at compile time.
 * E.g. if the finish body contains an indirect function invocation
 * or a library routine.
 * How should this test behave?
 *
 * Currently this test causes a deadlock.
 *
 * Tentatively declaring that this is an error
 * that must be caught at compile time (may not be possible).
 *
 * NEW SEMANTICS: Clock Use Exception such as
 *
 *  'Transmission of c (to a child) requires that I am registered with c'
 *  'Transmission of c requires that I am not between c.resume() and a next'
 *  'The immediate body of finish  can never transmit any clocks'
 *
 * are now caught at run time. The compiler
 * can remove the run time checks using static techniques,
 * and can issue warnings when it is statically detected that
 * clock use exceptions will
 * definitely occur, or will likely occur.
 *
 * Hence this file is renamed as *MustFailRun.x10
 *
 * @author kemal 5/2005
 */
public class ClockTest17_MustFailTimeout   {

	public def run(): boolean = {
		/*A0*/
		val c0: Clock = Clock.make();
		var x: X! = new X();
		// f0 does not transmit clocks to subactivity
		var f0: foo! = new foo() {
			public def apply(): void = {
				async {
					x10.io.Console.OUT.println("hello from finish async S");
				}
			}
		};
		// f1 transmits clocks to subactivity
		var f1: foo! = new foo() {
			public def apply(): void = {
				/*Activity A1*/
				async clocked(c0) {
					x10.io.Console.OUT.println("#1 before next");
					next;
					x10.io.Console.OUT.println("#1 after next");
				}
			}
		};

		val fooArray: Rail[foo]! = [f0,f1];  // FIXME: should be Rail[foo!]!

		// This is invoking Y.test(f0) but not clear to a compiler
		Y.test(fooArray(x.zero()) as foo!);
		// Finish in Y.test completes and then the following.run s.
		//No deadlock occurs here.
		x10.io.Console.OUT.println("#0a before next");
		next;
		x10.io.Console.OUT.println("#0a after next");

		// This is invoking Y.test(f1) but not clear to a compiler
		Y.test(fooArray(x.one()) as foo!);
		// Execution never reaches here (deadlock occurs) since:
		// A1 inside Y.test(f1) must first finish, but it
		// cannot since A0 has not.run d next on clock c0 yet.
		x10.io.Console.OUT.println("#0b before next");
		next;
		x10.io.Console.OUT.println("#0b after next");

		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new ClockTest17_MustFailTimeout().run ();
	}

	/**
	 * A class to invoke a 'function pointer' inside of finish
	 */
	static class Y {
		static def test(var f: foo!): void = {
			finish {
				f.apply(); // it is hard to determine f does an async clocked(c) S
			}
		}
	}

	/**
	 * An interface to use like a simple 'function pointer'
	 *
	 * foo f1 = new foo() { public void apply() S1 }; //assign body S1 to f1
	 *
	 * // values of free final variables of S1 are also captured in f1
	 *
	 * f1.apply(); // invoke S1 indirectly using its captured
	 *
	 * // free variables
	 */
	static interface foo {
		 def apply(): void;
	}

	/**
	 * Dummy class to make static memory disambiguation difficult
	 * for a typical compiler
	 */
	static class X {
		public val z:Rail[Int]! = [1,0];
		def zero(): int = { return z(z(z(1))); }
		def one(): int = { return z(z(z(0))); }
		def modify(): void = { z(0) += 1; }
	}
}
