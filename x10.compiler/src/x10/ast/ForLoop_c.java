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

package x10.ast;

import java.util.Collections;
import java.util.List;

import polyglot.ast.Expr;
import polyglot.ast.Formal;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.types.Name;
import polyglot.types.SemanticException;
import polyglot.types.StructType;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ContextVisitor;
import polyglot.visit.FlowGraph;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import x10.ast.X10Loop.LoopKind;
import x10.errors.Errors;
import x10.types.X10Context;
import x10.types.X10Flags;
import x10.types.X10MethodInstance;
import x10.types.X10TypeSystem;
import x10.types.checker.PlaceChecker;

/**
 * An immutable representation of an X10 for loop: for (i : D) S
 *
 * @author vj Dec 9, 2004
 */
public class ForLoop_c extends X10Loop_c implements ForLoop {

	/**
	 * @param pos
	 */
	public ForLoop_c(Position pos) {
		super(pos);
		loopKind=LoopKind.FOR;
	}

	/**
	 * @param pos
	 * @param formal
	 * @param domain
	 * @param body
	 */
	public ForLoop_c(Position pos, Formal formal, Expr domain, Stmt body) {
		super(pos, formal, domain, body);
		loopKind=LoopKind.FOR;
	}

	/* (non-Javadoc)
	 * @see polyglot.ast.Term#acceptCFG(polyglot.visit.CFGBuilder, java.util.List)
	 */
	public <S> List<S> acceptCFG(CFGBuilder v, List<S> succs) {
		v.visitCFG(formal, domain, ENTRY);
		v.visitCFG(domain, FlowGraph.EDGE_KEY_TRUE, body,
						   ENTRY, FlowGraph.EDGE_KEY_FALSE, this, EXIT);
		v.push(this).visitCFG(body, continueTarget(), ENTRY);
		return succs;
	}

	private static final Name ITERATOR = Name.make("iterator");
	public Node typeCheck(ContextVisitor tc) throws SemanticException {
	    X10Loop result = (X10Loop) super.typeCheck(tc);
	    X10TypeSystem xts = (X10TypeSystem) tc.typeSystem();
	    // TODO: generate a cast if STATIC_CALLS is off
	    X10MethodInstance mi = null;
	    Expr domain = result.domain();
	    mi = ClosureCall_c.findAppropriateMethod(tc, domain.type(), ITERATOR, Collections.<Type>emptyList(), Collections.<Type>emptyList());
	    assert (mi != null);
	    domain = (Expr) PlaceChecker.makeReceiverLocalIfNecessary(tc, domain, X10Flags.toX10Flags(mi.flags()));
	    if (domain != null) {
	        if (domain != result.domain()) result = result.domain(domain);
	    } else if (!xts.isUnknown(result.domain().type())) {
	        Errors.issue(tc.job(),
	                new SemanticException("The domain of this iterated for loop must be local",
	                        result.domain().position()));
	    }
	    return result;
	}

	/** Type check the statement. */
//	public Node typeCheck(TypeChecker tc) throws SemanticException {
//		ForLoop_c n = (ForLoop_c) super.typeCheck(tc);
//		X10TypeSystem ts = (X10TypeSystem) tc.typeSystem();
//		Expr newDomain = n.domain;
//		X10Type type = (X10Type) newDomain.type();
//		// FIXME: [IP] remove desugaring
//		if (ts.isDistribution(type))
//			newDomain = (Expr) tc.nodeFactory().Field(n.position(), newDomain, tc.nodeFactory().Id(n.position(), "region")).del().typeCheck(tc);
//		return n.domain(newDomain);
//	}

	public boolean condIsConstant() { return false; }
	public boolean condIsConstantTrue() { return false; }

	public String toString() {
		return "for (" + formal + " in " + domain + ")" + body;
	}

	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		w.write("for(");
		printBlock(formal, w, tr);
		w.write(" : ");
		printBlock(domain, w, tr);
		w.write(") ");
		printSubStmt(body, w, tr);
	}
}

