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

package x10.lang;

/**
 * The class Exception and its subclasses are a form of Throwable that indicates conditions that
 * a reasonable application might want to catch.
 */
public class Exception extends Throwable {

    /**
     * Construct an Exception with no detail message and no cause.
     */
    public def this() { super(); }

    /**
     * Construct an Exception with the specified detail message and no cause.
     *
     * @param message the detail message
     */
    public def this(message: String) { super(message); } 

    /**
     * Construct an Exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public def this(message: String, cause: Throwable) { super(message, cause); } 

    /**
     * Construct an Exception with no detail message and the specified cause.
     *
     * @param cause the cause
     */
    public def this(cause: Throwable) { super(cause); } 

    /**
     * Create a String representation of this Exception.
     */
    public def toString() = typeName() + ": " + getMessage();
}

// vim:tabstop=4:shiftwidth=4:expandtab
