/*
 *
 * (C) Copyright IBM Corporation 2006
 *
 *  This file is part of X10 Test.
 *
 */
import harness.x10Test;;

public class Array1DCodeGen extends x10Test {

	final def matgen(val a: Array[double], val b: Array[double]): double = {
		var n: int = a.region.rank(0).high();
		var init: int = 1325;
		var norma: double = 0.0;
		/* Next two for() statements switched.  Solver wants
		   matrix in column order. --dmd 3/3/97
		 */
		for (val (i,j,k): point in a) {
			init = 3125*init % 65536;
			var value: double = (init - 32768.0)/16384.0;
			finish write(a, i, j, k, value);
			norma = value > norma ? value : norma;
		}
		finish ateach (val (i,j,k): point in b) b(i, j, k) = 0.0;
		finish ateach (val (i,j,k): point in a.dist | [0..n-1, 0..n-1, 0..n-1]) plusWrite(b, 0, j, k, a(i, j, k));
		return norma;
	}
	final def write(val a: Array[double], val i: int, val j: int, val k: int, val val: double): void = {
		async (a.dist(i, j, k)) atomic a(i, j, k) = val;
	}
	final def plusWrite(val a: Array[double], val i: int, val j: int, val k: int, val val: double): void = {
		async (a.dist(i, j, k)) atomic a(i, j, k) += val;
	}
	
	
	public def run(): boolean = {
		var R: region{rank==3&&zeroBased&&rect} = [0..9, 0..9, 0..9];
		val a = Array.make[double](Dist.makeConstant(R, here));
		val b  = Array.make[double](Dist.makeConstant(R, here));
		System.out.println("runtime type of 3dZeroBasedRect array is " + a.getClass());
		val result = matgen(a,b);
		var S: regoon = [0..9, 0..9, 0..9];
		val aa =  Array.make[double](Dist.makeConstant(S, here));
		val bb  =  Array.make[double](Dist.makeConstant(S, here));
		var result1: double = matgen(aa,bb);
		System.out.println("runtime type of unknown array is " + aa.getClass());
		System.out.println("results are " + result + " " + result1);
		var diff: double = result-result1;
		return diff < 0 ? diff > -0.001 : diff < 0.001;
	}

	public static def main(var args: Rail[String]): void = {
		new Array1DCodeGen().execute();
	}
}
