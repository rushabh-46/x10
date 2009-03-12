/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;

/**
 * Check that a cast involving types which are not related by supertype or subtype
 * relation fails
 *
 * @author pvarma
 */
public class CastChecking1_MustFailCompile extends x10Test {
	class Test(i:int, j:int) {
		def this(i: int, j: int): Test{self.i==i && self.j==j} = {
			property(i,j);
		}
    }
	class Test2(k: int) extends Test{i==j}  {
	   def this(k:int):Test2{self.i==i,self.j==j,self.k==k} = {
	     super(k,k);
	     property(k);
	     }
	}
	
	public def run(): boolean = {
		var a: Test2{k==1&&i==j} = new Test2(1);
		var b: Test{i==j} = a;
		var c: Test = 5 as Test;  // must fail compile
	   return true;
	}
	public static def main(var args: Rail[String]): void = {
		new CastChecking1_MustFailCompile().execute();
	}
}
