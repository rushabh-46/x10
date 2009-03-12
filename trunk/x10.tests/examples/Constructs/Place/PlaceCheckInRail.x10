// (C) Copyright IBM Corporation 2006
// This file is part of X10 Test.

import harness.x10Test;

/**
 * @author bdlucas
 */

public class PlaceCheckInRail extends x10Test {


    class C {
        val x:int = 0;
        def foo() = 0;
    }


    public def run01(): boolean {

        val r = Rail.makeVar[C](3, (nat)=>new C());

        try {
            (future (Place.places(1)) r(0).x).force();
        } catch (BadPlaceException) {
            return true;
        }
        x10.io.Console.OUT.println("01 fails");
        return false;
    }


    public def run02(): boolean {

        val r = Rail.makeVar[C](3, (nat)=>new C());

        try {
            (future (Place.places(1)) r(0).foo()).force();
        } catch (BadPlaceException) {
            return true;
        }
        x10.io.Console.OUT.println("01 fails");
        return false;
    }


    public def run03(): boolean {

        val r = Rail.makeVal[C](3, (nat)=>new C());

        try {
            (future (Place.places(1)) r(0).x).force();
        } catch (BadPlaceException) {
            return true;
        }
        x10.io.Console.OUT.println("01 fails");
        return false;
    }


    public def run04(): boolean {

        val r = Rail.makeVal[C](3, (nat)=>new C());

        try {
            (future (Place.places(1)) r(0).foo()).force();
        } catch (BadPlaceException) {
            return true;
        }
        x10.io.Console.OUT.println("01 fails");
        return false;
    }


    public def run() = run01() && run02() && run03() && run04();

    public static def main(var args: Rail[String]): void = {
        new PlaceCheckInRail().execute();
    }
}
