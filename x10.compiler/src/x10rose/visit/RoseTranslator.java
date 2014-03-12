/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2006-2014.
 */

package x10rose.visit;


import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import polyglot.ast.AbstractBlock_c;
import polyglot.ast.Allocation_c;
import polyglot.ast.AmbAssign_c;
import polyglot.ast.AmbExpr_c;
import polyglot.ast.AmbReceiver_c;
import polyglot.ast.AmbTypeNode_c;
import polyglot.ast.ArrayAccessAssign_c;
import polyglot.ast.ArrayAccess_c;
import polyglot.ast.ArrayInit_c;
import polyglot.ast.Assert_c;
import polyglot.ast.Assign_c;
import polyglot.ast.Block_c;
import polyglot.ast.Branch_c;
import polyglot.ast.Case_c;
import polyglot.ast.Catch_c;
import polyglot.ast.ClassBody_c;
import polyglot.ast.ClassDecl_c;
import polyglot.ast.ClassLit_c;
import polyglot.ast.Do_c;
import polyglot.ast.Empty_c;
import polyglot.ast.Eval_c;
import polyglot.ast.Expr_c;
import polyglot.ast.FieldAssign_c;
import polyglot.ast.For_c;
import polyglot.ast.Import_c;
import polyglot.ast.IntLit_c;
import polyglot.ast.JL;
import polyglot.ast.Labeled_c;
import polyglot.ast.LocalAssign_c;
import polyglot.ast.LocalClassDecl_c;
import polyglot.ast.NewArray_c;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Node_c;
import polyglot.ast.NullLit_c;
import polyglot.ast.PackageNode;
import polyglot.ast.PackageNode_c;
import polyglot.ast.Return_c;
import polyglot.ast.SourceFile;
import polyglot.ast.SourceFile_c;
import polyglot.ast.Stmt_c;
import polyglot.ast.SwitchBlock_c;
import polyglot.ast.Switch_c;
import polyglot.ast.Term_c;
import polyglot.ast.Throw_c;
import polyglot.ast.TopLevelDecl;
import polyglot.ast.Try_c;
import polyglot.ast.TypeNode_c;
import polyglot.ast.While_c;
import polyglot.frontend.Compiler;
import polyglot.frontend.Job;
import polyglot.frontend.Source;
import polyglot.frontend.TargetFactory;
import polyglot.main.Options;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.ErrorInfo;
import polyglot.util.ErrorQueue;
import polyglot.util.StringUtil;
import polyglot.visit.Translator;
import polyglot.visit.PrettyPrinter;
import x10.ExtensionInfo.X10Scheduler.X10Job;
import x10.ast.AmbDepTypeNode_c;
import x10.ast.AnnotationNode_c;
import x10.ast.AssignPropertyCall_c;
import x10.ast.Async_c;
import x10.ast.AtEach_c;
import x10.ast.AtExpr_c;
import x10.ast.AtHomeExpr_c;
import x10.ast.AtHomeStmt_c;
import x10.ast.AtStmt_c;
import x10.ast.Atomic_c;
import x10.ast.ClosureCall_c;
import x10.ast.Closure_c;
import x10.ast.DepParameterExpr_c;
import x10.ast.Finish_c;
import x10.ast.ForLoop_c;
import x10.ast.HasZeroTest_c;
import x10.ast.Here_c;
import x10.ast.LocalTypeDef_c;
import x10.ast.Next_c;
import x10.ast.ParExpr_c;
import x10.ast.PropertyDecl_c;
import x10.ast.SettableAssign_c;
import x10.ast.StmtExpr_c;
import x10.ast.StmtSeq_c;
import x10.ast.SubtypeTest_c;
import x10.ast.Tuple_c;
import x10.ast.TypeDecl_c;
import x10.ast.When_c;
import x10.ast.X10Binary_c;
import x10.ast.X10BooleanLit_c;
import x10.ast.X10Call_c;
import x10.ast.X10CanonicalTypeNode_c;
import x10.ast.X10Cast_c;
import x10.ast.X10CharLit_c;
import x10.ast.X10ClassBody_c;
import x10.ast.X10ClassDecl_c;
import x10.ast.X10ClockedLoop_c;
import x10.ast.X10Conditional_c;
import x10.ast.X10ConstructorCall_c;
import x10.ast.X10ConstructorDecl_c;
import x10.ast.X10Do_c;
import x10.ast.X10FieldDecl_c;
import x10.ast.X10Field_c;
import x10.ast.X10FloatLit_c;
import x10.ast.X10Formal_c;
import x10.ast.X10If_c;
import x10.ast.X10Instanceof_c;
import x10.ast.X10LocalDecl_c;
import x10.ast.X10Local_c;
import x10.ast.X10Loop_c;
import x10.ast.X10MethodDecl_c;
import x10.ast.X10New_c;
import x10.ast.X10Special_c;
import x10.ast.X10StringLit_c;
import x10.ast.X10Unary_c;
import x10.ast.X10While_c;
import x10.visit.X10DelegatingVisitor;

public class RoseTranslator extends Translator {

    public JavaToken createJavaToken(/*ASTNode node*/) {
        JavaSourcePositionInformation pos = null;//this.posFactory.createPosInfo(node);
        // For now we return dummy text
        return new JavaToken("Dummy JavaToken (see createJavaToken)", pos);
    }

/*
    public JavaToken createJavaToken(ASTNode lnode, ASTNode rnode) {
        JavaSourcePositionInformation pos = this.posFactory.createPosInfo(lnode.sourceStart(), rnode.sourceEnd());
        // For now we return dummy text
        return new JavaToken("Dummy JavaToken (see createJavaToken)", pos);
    }
*/
	
	public RoseTranslator(Job job, TypeSystem ts, NodeFactory nf, TargetFactory tf) {
		super(job, ts, nf, tf);
	}
	
	protected boolean translateSource(SourceFile sfn) {
//System.out.println("RoseTranslator.translateSource()");
		TypeSystem ts = typeSystem();
		NodeFactory nf = nodeFactory();
		TargetFactory tf = this.tf;
		ErrorQueue eq = job.compiler().errorQueue();
		Compiler compiler = job.compiler();
		Options options = job.extensionInfo().getOptions();
		
//	System.out.println("job.ast="+job.ast());
		
/*
		try {
//        JNI.cactionTest();
//        JavaParser.cactionTest();
		} catch (Error er) {
//			System.out.println("cactionTest1");
			er.printStackTrace();
//			System.out.println("cactionTest2");
		}
*/

		Source src = sfn.source();
		String in_file_name = sfn.source().path();
		String out_file_name = in_file_name + ".txt";
		
		//MH-20140123
		//	Just confirm that AST node is created
		try {
			SourceFile sfile = (SourceFile)job.ast();
			CodeWriter w = tf.outputCodeWriter(new File(out_file_name), 100);
			sfile.prettyPrint(w, new PrettyPrinter());
//			System.out.println(sfile);
			
			new ToRoseVisitor(w, null).visitAppropriate(sfn);
			
			w.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	

		// Find the public declarations in the file. We'll use these to
		// derive the names of the target files. There will be one
		// target file per public declaration. If there are no public
		// declarations, we'll use the source file name to derive the
		// target file name.
		List<TopLevelDecl> exports = exports(sfn);

		CodeWriter w = null;

		out_file_name = in_file_name + ".Rose.dot";
		try {

			w = tf.outputCodeWriter(new File(out_file_name), 100);
			job.compiler().addOutputFile(in_file_name, out_file_name);

			w.writeln("digraph \"" + in_file_name + "\" {");

			new DV(w, null).visitAppropriate(sfn);

			w.writeln("}");

			w.newline(0);

			return true;

		} catch (IOException e) {

			eq.enqueue(ErrorInfo.IO_ERROR, "I/O error while translating: " + e.getMessage());

			return false;

		} finally {

			if (w != null) {
				try {
					w.close();
					
					String[] cmdline = {"dot", "-Tpng", "-O", out_file_name};
			        try {
			            Runtime runtime = Runtime.getRuntime();
			        	Process proc = runtime.exec(cmdline, null, options.output_directory);

			        	InputStreamReader err = new InputStreamReader(proc.getErrorStream());

			        	String output = null;
			        	try {
			        		char[] c = new char[72];
			        		int len;
			        		StringBuffer sb = new StringBuffer();
			        		while((len = err.read(c)) > 0) {
			        			sb.append(String.valueOf(c, 0, len));
			        		}

			        		if (sb.length() != 0) {
			        			output = sb.toString();
			        		}
			        	}
			        	finally {
			        		err.close();
			        	}

			        	proc.waitFor();

			        	if (!options.keep_output_files) {
			        		String[] rmCmd = new String[] {"rm", out_file_name};
			        		runtime.exec(rmCmd, null, options.output_directory);
			        	}

			        	if (output != null)
			        		eq.enqueue(proc.exitValue()>0 ? ErrorInfo.POST_COMPILER_ERROR : ErrorInfo.WARNING, output);
			        	if (proc.exitValue() > 0) {
			        		eq.enqueue(ErrorInfo.POST_COMPILER_ERROR, "Non-zero return code: " + proc.exitValue());
			        		return false;
			        	}
			        } catch(Exception e) {
			        	eq.enqueue(ErrorInfo.POST_COMPILER_ERROR, e.getMessage() != null ? e.getMessage() : e.toString());
			        	return false;
			        }						
				} catch (IOException e) {
					job.compiler().errorQueue().enqueue(ErrorInfo.IO_ERROR, "I/O error while closing output file: " + e.getMessage());
				}
			}
			

		}
	}

	int counter = 0;

	String getDotNode(Node n) {
		String id;
		if (dotNode.containsKey(n)) {
			id = dotNode.get(n);
		} else {
			id = Integer.toString(counter++);
			dotNode.put(n, id);
		}
		return id;
	}

	Map<Node, String> dotNode = new HashMap<Node, String>();

static int method_index = 0;
	public class ToRoseVisitor extends X10DelegatingVisitor {
		Node parent;
		CodeWriter w;
		
		ToRoseVisitor(CodeWriter w, Node parent) {
			this.parent = parent;
			this.w = w;
		}
		
		void toRose(Node n, String name, String... extra) {
			if (name != null)
				System.out.print(name);
			if (extra != null)
				for (String s : extra) {
					System.out.print(s + " ");
				}
			System.out.println();
/*
*/
		}		
		
		public void previsitAppropriate(JL n) {
			if (n instanceof X10MethodDecl_c) { previsit((X10MethodDecl_c)n); return; }
			if (n instanceof TypeNode_c) { System.out.println(11); return; }
			if (n instanceof TypeDecl_c) { System.out.println(12); return; }		
			if (n instanceof X10ClassDecl_c) {  System.out.println(13); return; }
			if (n instanceof ClassDecl_c) { System.out.println(14); return; }
			if (n instanceof X10ClassBody_c) { previsit((X10ClassBody_c)n); return; }
			if (n instanceof ClassBody_c) { System.out.println(16); return; }
			else {
				System.out.println("RoseTranslater.previsitAppropriate(): Not implemented yet.");
			}
		}
		
		public void previsit(X10ClassBody_c n) {
			// TODO Auto-generated method stub
			toRose(n, "previsit classBody: ", null);
			// don't know what kind of caction* should be invoked here! (2014/01/30 horie)
			previsitChildren(n, n.members());
		}
		

		void previsitChild(Node p, Node n) {
			if (n == null)
				return;
			previsitAppropriate(n);
		}
		
		void previsitChildren(Node p, List<? extends Node> l) {
			if (l == null)
				return;
			for (Node n : l)
				previsitChild(p, n);
		}
		
		void visitChild(Node p, Node n) {
			if (n == null)
				return;
			new ToRoseVisitor(w, p).visitAppropriate(n);
		}

		void visitChildren(Node p, List<? extends Node> l) {
			if (l == null)
				return;
			for (Node n : l)
				visitChild(p, n);
		}

		public void visit(Node_c n) {
			toRose(n, null);
			System.err.println("UNRECOGNISED NODE in DotTranslator: " + n.getClass());
		}

		
		public void visit(SourceFile_c n) {
			toRose(n, n.source().path());
			PackageNode pnode = n.package_();	
/*
			if (pnode == null)
				System.out.println("Package-null");
			else
				System.out.println("Package: " + pnode);
*/
			JNI.cactionCompilationUnitList(1, new String[]{n.source().path()});
//			JNI.cactionCompilationUnitDeclaration(pnode==null ? "":pnode+"", n.source().path());
			visitChildren(n, n.decls());
		}

		public void visit(Import_c n) {
			toRose(n, "Import:", n.kind() + " " + n.name().toString());
		}

		public void visit(PackageNode_c n) {
			toRose(n, "Package:", n.package_().get().toString());
			JNI.cactionPushPackage(n.package_().get().toString());
		}

		public void visit(X10ClassDecl_c n) {
			toRose(n, "X10ClassDecl:", n.name().id().toString());
//			JNI.cactionInsertClassStart(n.name().id().toString());
//			System.out.println(111);
//			JNI.cactionSetupObject();
//			System.out.println(222);
			
//			JNI.cactionTypeDeclaration("", n.name().id().toString(), false, false, false, false, false, false, false, false, false, false);
		
			String class_name = n.name().id().toString();
			
			JNI.cactionInsertClassStart(class_name, createJavaToken());
			// does not consider nested class so far
			JNI.cactionInsertClassEnd(class_name, createJavaToken());
			
	        JNI.cactionBuildClassSupportStart(class_name, "", true, // a user-defined class?
                    false, false, false,	false,	createJavaToken());
	        previsitChild(n, n.body());
	        JNI.cactionBuildClassSupportEnd(class_name, createJavaToken());
			
			// does not care a nested class
			method_index=0;
//			toRose(n, "class", n.name().id().toString());
			visitChildren(n, n.typeParameters());
			visitChildren(n, n.properties());
			visitChild(n, n.classInvariant());
			visitChild(n, n.superClass());
			visitChildren(n, n.interfaces());
			visitChild(n, n.body());
//			JNI.cactionTypeDeclaration("", n.name().id().toString(), false, false, false, false, false, false, false, false, false, false);
		
			
		}

		public void visit(LocalClassDecl_c n) {
			toRose(n, "LocalClassDecl:", null);
			visitChild(n, n.decl());
		}

		public void visit(X10ClassBody_c n) {
			toRose(n, "classBody: ", null);
			// don't know what kind of caction* should be invoked here! (2014/01/30 horie)
			visitChildren(n, n.members());
		}
		
		public void visit(X10MethodDecl_c n) {
			toRose(n, "method decl: ", n.name().id().toString());
			List formals = n.formals();
			System.out.println("ReturnType==" + n.returnType());
			

			String method_name = n.name().id().toString();
//			if (n.returnType().toString().indexOf("{") >= 0) {
//				
//			}

          JNI.cactionBuildMethodSupportStart(method_name, method_index, createJavaToken());
			visitChild(n, n.returnType());
			visitChildren(n, n.formals());
		
/*
			JNI.cactionMethodDeclaration(n.name().id().toString(), method_index++, formals.size(), 
					new JavaToken(n.name().id().toString(), new JavaSourcePositionInformation(n.position().line())), 
					new JavaToken(n.name().id().toString()+"_args", new JavaSourcePositionInformation(n.position().line())));
*/

           JNI.cactionBuildMethodSupportEnd(method_name, method_index, // method index 
												 false, false, false, 0, formals.size(),
                                           true, /* user-defined-method */
					new JavaToken(n.name().id().toString(), new JavaSourcePositionInformation(n.position().line())), 
					new JavaToken(n.name().id().toString()+"_args", new JavaSourcePositionInformation(n.position().line())));

//			visitChild(n, n.guard());
//			visitChild(n, n.offerType());
//			visitChildren(n, n.throwsTypes());
			visitChild(n, n.body());
		}
		
		public void previsit(X10MethodDecl_c n) {
			toRose(n, "method decl: ", n.name().id().toString());
			List formals = n.formals();
			System.out.println("ReturnType==" + n.returnType());
			

			String method_name = n.name().id().toString();
//			if (n.returnType().toString().indexOf("{") >= 0) {
//				
//			}

          JNI.cactionBuildMethodSupportStart(method_name, method_index, createJavaToken());
			visitChild(n, n.returnType());
			visitChildren(n, n.formals());
		
/*
			JNI.cactionMethodDeclaration(n.name().id().toString(), method_index++, formals.size(), 
					new JavaToken(n.name().id().toString(), new JavaSourcePositionInformation(n.position().line())), 
					new JavaToken(n.name().id().toString()+"_args", new JavaSourcePositionInformation(n.position().line())));
*/

           JNI.cactionBuildMethodSupportEnd(method_name, method_index, // method index 
												 false, false, false, 0, formals.size(),
                                           true, /* user-defined-method */
					new JavaToken(n.name().id().toString(), new JavaSourcePositionInformation(n.position().line())), 
					new JavaToken(n.name().id().toString()+"_args", new JavaSourcePositionInformation(n.position().line())));

//			visitChild(n, n.guard());
//			visitChild(n, n.offerType());
//			visitChildren(n, n.throwsTypes());
//			visitChild(n, n.body());
		}

		public void visit(X10Formal_c n) {
			toRose(n, "formal: ", n.name().id().toString());

//                args_location = createJavaToken(args[0], args[args.length - 1]); 
//
//                for (int j = 0; j < args.length; j++) {
//                    Argument arg = args[j];
//                    JavaToken arg_location = createJavaToken(arg);
//                    generateAndPushType(arg.type.resolvedType, arg_location);
//                    String argument_name = new String(arg.name);
//                    JavaParser.cactionBuildArgumentSupport(argument_name,
//                                                           arg.isVarArgs(),
//                                                           arg.binding.isFinal(),
//                                                           arg_location);
//                }
			visitChild(n, n.type());
			// so far, all parameters's modifier are set as final
			JNI.cactionBuildArgumentSupport(n.name().toString(), n.vars().size()>0, false, createJavaToken());
		}

		public void visit(X10Call_c n) {
			toRose(n, "x10call: ", n.name().id().toString());
			visitChild(n, n.target());
			visitChildren(n, n.typeArguments());
			visitChildren(n, n.arguments());
		}

		public void visit(X10ConstructorDecl_c n) {
			toRose(n, "X10ConstructorDecl:", n.name().id().toString());
//			JNI.cactionConstructorDeclaration();
			visitChildren(n, n.formals());
			visitChild(n, n.guard());
			visitChild(n, n.offerType());
			visitChildren(n, n.throwsTypes());
			visitChild(n, n.body());
			
		}

		public void visit(X10ConstructorCall_c n) {
			toRose(n, "X10ConstructorCall:", null);
			visitChild(n, n.target());
			visitChildren(n, n.typeArguments());
			visitChildren(n, n.arguments());
		}

		public void visit(Block_c n) {
			toRose(n, "Block:", null);
			System.out.println("block-->p" + n);
			
//			   if (javaParserSupport.verboseLevel > 0)
//		            System.out.println("Inside of enter (Block,BlockScope)");
//
//		        JavaParser.cactionBlock(javaParserSupport.createJavaToken(node));
//
//		        if (javaParserSupport.verboseLevel > 0)
//		            System.out.println("Leaving enter (Block,BlockScope)");
//
//		        return true; // do nothing by node, keep traversing
			
			
			
			visitChildren(n, n.statements());
		}

		public void visit(StmtSeq_c n) {
			toRose(n, "StmtSeq: ", null);
			visitChildren(n, n.statements());
		}

		public void visit(AssignPropertyCall_c n) {
			toRose(n, "AssignPropertyCall:", null);
			System.out.println("AssignPropppertyCall-->" + n);
			visitChildren(n, n.arguments());
		}

		public void visit(Empty_c n) {
			toRose(n, "Empty:", null);
			
			System.out.println("Empty-->" + n);
		}

		public void visit(X10CanonicalTypeNode_c n) {
			toRose(n, "X10CanonicalTypeNode:", n.nameString());
			String canonicalTypeName = n.nameString();
			if (   canonicalTypeName.equals("void")
				|| canonicalTypeName.equals("boolean")
				|| canonicalTypeName.equals("byte")
				|| canonicalTypeName.equals("char")
				|| canonicalTypeName.equals("int")
				|| canonicalTypeName.equals("short")
				|| canonicalTypeName.equals("float")
				|| canonicalTypeName.equals("long")
				|| canonicalTypeName.equals("double")) {
				JNI.cactionTypeReference("", n.nameString());
			}
			if (n.node().toString().indexOf("self==this") >= 0) { 
				JNI.cactionTypeReference("", n.nameString());
			}
			else {
//				System.out.println(">>>> " + n.node() + ", " + n.nameString() + ", " + n.type());
				int index = n.node().toString().indexOf("[");
				String className = n.node().toString();
				if (index >= 0)
					className = className.substring(0, index);
				int lastDot = className.lastIndexOf('.');
				
				String pkg = "";
				String type = "";
				if (lastDot >= 0) {
					pkg = className.substring(0, lastDot);
					type = className.substring(lastDot+1);
				}
				else {
					type = className;
				}
				
//				System.out.println("className=" + className + ", pkg=" + pkg + ", type=" + type);
				// So far, I use a representation without package name such as "Rail". 
				// If I use a representation such as "x10.lang.Rail", lookupTypeByName() function tries
				// to look for a type name whether the type name is already registered or not. (and, there is
				// no registration for Rail, so ROSE compiler fails.)
//				JNI.cactionTypeReference(pkg, type);
				JNI.cactionTypeReference("", n.nameString());
			}
//			JNI.cactionTypeDeclaration("", n.nameString(), false, false, false, false, false, false, false, false, false, false);
		}

		public void visit(Return_c n) {
			toRose(n, "Return:", null);
			visitChild(n, n.expr());
		}

		public void visit(X10Binary_c n) {
			toRose(n, "X10Binary:", n.operator().toString());
			visitChild(n, n.left());
			visitChild(n, n.right());
			
		}

		public void visit(X10Unary_c n) {
			toRose(n, "X10Unary:", n.operator().toString());
			visitChild(n, n.expr());
		}

		public void visit(ParExpr_c n) { // parentheses
			toRose(n, "( )");
			visitChild(n, n.expr());
		}		

		public void visit(X10Special_c n) {
			toRose(n, "X10Special:", n.kind().toString());
		}

		public void visit(Here_c n) {
			toRose(n, "here");
		}

		public void visit(X10Local_c n) {
			toRose(n, "X10Local :", n.name().id().toString());
//            JavaParser.cactionSingleNameReference(package_name, type_name, varRefName, javaParserSupport.createJavaToken(node));
			JNI.cactionSingleNameReference("", "", n.name().id().toString(), createJavaToken());
			System.out.println("X10Local end");
		}

		public void visit(Eval_c n) {
			toRose(n, "Eval:", null);
			visitChild(n, n.expr());
		}

		public void visit(For_c n) {
			toRose(n, "For:", null);
			visitChildren(n, n.inits());
			visitChild(n, n.cond());
			visitChildren(n, n.iters());
			visitChild(n, n.body());
		}

		public void visit(ForLoop_c n) {
			toRose(n, "ForLoop:", null);
			visitChild(n, n.formal());
			visitChild(n, n.cond());
			visitChild(n, n.domain());
			visitChild(n, n.body());
		}

		public void visit(Branch_c n) {
			toRose(n, "Branch:", n.kind()+(n.labelNode()!=null ? "\\n"+n.labelNode().id().toString() : ""));
		}
		
		public void visit(X10Do_c n) {
			toRose(n, "X10Do:", null);
			visitChild(n, n.cond());
			visitChild(n, n.body());
		}

		public void visit(X10While_c n) {
			toRose(n, "X10While:", null);
			visitChild(n, n.cond());
			visitChild(n, n.body());
		}


		public void visit(Tuple_c n) {
			toRose(n, "Tuple:", null);
			visitChildren(n, n.arguments());
		}

		public void visit(SettableAssign_c n) {
			toRose(n, "SettableAssign:",  null);
			visitChild(n, n.left());
			visitChildren(n, n.index());
			visitChild(n, n.right());
		}

		
		public void visit(FieldAssign_c n) {
			toRose(n, "FieldAssign:", n.name().id().toString());
			visit(n.target());
			visit(n.right());
		}

		public void visit(X10Field_c n) {
			toRose(n, "X10Field:", n.name().id().toString());
			visit(n.target());
		}

		public void visit(X10FieldDecl_c n) {
			toRose(n, "X10FieldDecl:", n.name().id().toString());
			visitChild(n, n.type());
			visitChild(n, n.init());
		}

		public void visit(X10LocalDecl_c n) {
			toRose(n, "X10LocalDecl:", n.name().id().toString());
			visitChild(n, n.type());
			visitChild(n, n.init());
		}

		public void visit(PropertyDecl_c n) {
			toRose(n, "PropertyDecl:", n.name().id().toString());
			visitChild(n, n.type());
			visitChild(n, n.init());
		}

		
		public void visit(X10If_c n) {
			toRose(n, "X10If:", "if");
			visitChild(n, n.cond());
			visitChild(n, n.consequent());
			visitChild(n, n.alternative());
		}

		public void visit(X10Conditional_c n) {
			toRose(n, "X10Conditional:", null);
			visitChild(n, n.cond());
			visitChild(n, n.consequent());
			visitChild(n, n.alternative());
		}

		public void visit(Assert_c n) {
			toRose(n, "Assert:", null);
			visitChild(n, n.cond());
			visitChild(n, n.errorMessage());
		}


		public void visit(Throw_c n) {
			toRose(n, "Throw:", null);
			visitChild(n, n.expr());
		}

		public void visit(Try_c n) {
			toRose(n, "Try:", null);
			visitChild(n, n.tryBlock());
			visitChildren(n, n.catchBlocks());
			visitChild(n, n.finallyBlock());
		}

		public void visit(Catch_c n) {
			toRose(n, "Catch:", null);
			visitChild(n, n.formal());
			visitChild(n, n.body());
		}

		
		public void visit(Labeled_c n) {
			toRose(n, "Labeled:",  n.labelNode().id().toString());
			visitChild(n, n.statement());
		}
		

		
		public void visit(X10BooleanLit_c n) {
			toRose(n, "X10BooleanLit:", Boolean.toString(n.value()));
		}

		public void visit(ClassLit_c n) {
			toRose(n, "ClassLit: ", null);
			visitChild(n, n.typeNode());
		}

		public void visit(X10FloatLit_c n) {
			toRose(n, "X10FloatLit:", Double.toString(n.value()));
		}

		public void visit(NullLit_c n) {
			toRose(n, "NullLit:", "null");
		}

		public void visit(X10CharLit_c n) {
			toRose(n, "X10CharLit:", "" + n.value());
		}

		public void visit(IntLit_c n) {
			toRose(n, "IntLit:", Long.toString(n.value()));
		}

		public void visit(X10StringLit_c n) {
			toRose(n, "X10StringLit:", StringUtil.escape(n.value()));
		}

		
		public void visit(Finish_c n) {
			toRose(n, "Finish:", null);
			visitChild(n, n.body());
		}

		public void visit(AtStmt_c n) {
			toRose(n, "AtStmt:", null);
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtHomeStmt_c n) {
			toRose(n, "AtHomeStmt:", null);
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtExpr_c n) {
			toRose(n, "AtExpr:", null);
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtHomeExpr_c n) {
			toRose(n, "AtHomeExpr:", null);
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtEach_c n) {
			toRose(n, "AtEach:", null);
			visitChild(n, n.formal());
			visitChild(n, n.domain());
			visitChild(n, n.body());
		}

		public void visit(Async_c n) {
			toRose(n, "Async:", null);
			visitChild(n, n.body());
		}
		
		public void visit(Atomic_c n) {
			toRose(n, "Atomic:", null);
			visitChild(n, n.body());
		}

		public void visit(When_c n) {
			toRose(n, "When:", null);
			visitChild(n, n.expr());
			visitChild(n, n.stmt());
		}



		public void visit(X10New_c n) {
			toRose(n, "X10New:", null);
			visitChildren(n, n.typeArguments());
			visitChildren(n, n.arguments());
			visitChild(n, n.objectType());
			visitChild(n, n.body());
		}

		public void visit(Allocation_c n) {
			toRose(n, "Allocation:", null);
			visitChild(n, n.objectType());
		}
				
		public void visit(LocalAssign_c n) {
			toRose(n, "LocalAssign:", null);
			visitChild(n, n.local());
			visitChild(n, n.right());
		}

		public void visit(X10Cast_c n) {
			toRose(n, "X10Cast:", null);
			visitChild(n, n.castType());
			visitChild(n, n.expr());
		}

		public void visit(X10Instanceof_c n) {
			toRose(n, "X10Instanceof:", null);
			visitChild(n, n.compareType());
			visitChild(n, n.expr());
		}

		public void visit(SubtypeTest_c n) {
			toRose(n, "SubtypeTest:", null);
			visitChild(n, n.subtype());
			visitChild(n, n.supertype());
		}

		public void visit(DepParameterExpr_c n) {
			toRose(n, "DepParameterExpr:", null);
			visitChildren(n, n.formals());
			visitChildren(n, n.condition());
		}

		public void visit(HasZeroTest_c n) {
			toRose(n, "HasZeroTest:", null);
			visitChild(n, n.parameter());
		}

		public void visit(Closure_c n) {
			toRose(n, "Closure:", null);
			visitChildren(n, n.formals());
			visitChild(n, n.body());
		}

		public void visit(ClosureCall_c n) {
			toRose(n, "ClosureCall:", null);
			visitChild(n, n.target());
			visitChildren(n, n.arguments());
		}

		public void visit(StmtExpr_c n) {
			toRose(n, "StmtExpr:", null);
			visitChildren(n, n.statements());
		}

		
		public void visit(AmbReceiver_c n) {
			toRose(n, "AmbReceiver:", n.nameNode().id().toString());
		}

		
		public void visit(Switch_c n) {
			toRose(n, "Switch:", null);
			visitChild(n, n.expr());
			visitChildren(n, n.elements());
		}

		public void visit(SwitchBlock_c n) {
			toRose(n, "SwitchBlock:", null);
			visitChildren(n, n.statements());
		}

		public void visit(Case_c n) {
			toRose(n, "Case:", null);
			visitChild(n, n.expr());
		}

		public void visit(LocalTypeDef_c n) {
			toRose(n, "LocalTypeDef:", null);
			visitChild(n, n.typeDef());
		}

		public void visit(Next_c n) {
			toRose(n, "Next:", "here");
		}

		public void visit(TypeDecl_c n) {
			toRose(n, "TypeDecl: ", n.name().id().toString());
			visitChild(n, n.type());
		}
		
	}
	
	public class DV extends X10DelegatingVisitor {

		Node parent;
		CodeWriter w;

		DV(CodeWriter w, Node parent) {
			this.parent = parent;
			this.w = w;
		}

		void createDotNode(Node n, String name, String... extra) {
			w.write(getDotNode(n) + " [");
			Map<String, String> extraMap = new HashMap<String, String>();
			// defaults
			extraMap.put("style", "filled");
			extraMap.put("penwidth", "2.0");
			extraMap.put("label", n.getClass().toString() + (name != null ? "\\n" + name : ""));
			extraMap.put("shape", "box");
			extraMap.put("color", "#000000");
			extraMap.put("fillcolor", "#FFFFFF");
			// override defaults here
			for (String s : extra) {
				if (!s.contains("=")) {
					System.err.println("Unrecognised dot node option: " + s);
					continue;
				}
				String before = s.substring(0, s.indexOf("="));
				String after = s.substring(s.indexOf("=") + 1);
				extraMap.put(before, after);
			}
			for (String key : extraMap.keySet()) {
				w.writeln(key + "=\"" + extraMap.get(key) + "\", ");
			}
			w.writeln("];");
			
			if (parent != null)
				w.writeln(getDotNode(parent) + " -> " + getDotNode(n));
			
			try {
				w.flush();
			} catch (IOException e) {
				throw new Error(e);
			}
		}

		void visitChild(Node p, Node n) {
			if (n == null)
				return;
			new DV(w, p).visitAppropriate(n);
		}

		void visitChildren(Node p, List<? extends Node> l) {
			if (l == null)
				return;
			for (Node n : l)
				visitChild(p, n);
		}

		public void visit(Node_c n) {
			createDotNode(n, null, "fillcolor=#FF0000", "fontcolor=#ffffff", "shape=Mdiamond");
			System.err.println("UNRECOGNISED NODE in DotTranslator: " + n.getClass());
		}

		
		public void visit(SourceFile_c n) {
			createDotNode(n, n.source().path(), "shape=folder", "fillcolor=#000040", "fontcolor=#ffffff");
			visitChildren(n, n.decls());
		}

		public void visit(Import_c n) {
			createDotNode(n, n.kind() + " " + n.name().toString());
		}

		public void visit(PackageNode_c n) {
			createDotNode(n, n.package_().get().toString());
		}

		public void visit(X10ClassDecl_c n) {
			createDotNode(n, n.name().id().toString(), "shape=house", "fillcolor=#004000", "fontcolor=#ffffff");
			visitChildren(n, n.typeParameters());
			visitChildren(n, n.properties());
			visitChild(n, n.classInvariant());
			visitChild(n, n.superClass());
			visitChildren(n, n.interfaces());
			visitChild(n, n.body());
		}

		public void visit(LocalClassDecl_c n) {
			createDotNode(n, null);
			visitChild(n, n.decl());
		}

		public void visit(X10ClassBody_c n) {
			createDotNode(n, null, "shape=house");
			visitChildren(n, n.members());
		}

		public void visit(X10MethodDecl_c n) {
			createDotNode(n, n.name().id().toString(), "shape=trapezium", "fillcolor=#FFC0A0");
			visitChildren(n, n.formals());
			visitChild(n, n.guard());
			visitChild(n, n.offerType());
			visitChildren(n, n.throwsTypes());
			visitChild(n, n.body());
		}

		public void visit(X10Formal_c n) {
			createDotNode(n, n.name().id().toString(), "shape=trapezium", "fillcolor=#FFC0A0");
			visitChild(n, n.type());
		}

		public void visit(X10Call_c n) {
			createDotNode(n, n.name().id().toString(), "fillcolor=#FFC0A0", "shape=oval");
			visitChild(n, n.target());
			visitChildren(n, n.typeArguments());
			visitChildren(n, n.arguments());
		}

		public void visit(X10ConstructorDecl_c n) {
			createDotNode(n, n.name().id().toString(), "shape=trapezium", "fillcolor=#FFFFA0");
			visitChildren(n, n.formals());
			visitChild(n, n.guard());
			visitChild(n, n.offerType());
			visitChildren(n, n.throwsTypes());
			visitChild(n, n.body());
		}

		public void visit(X10ConstructorCall_c n) {
			createDotNode(n, null, "fillcolor=#FFFFC0", "shape=oval");
			visitChild(n, n.target());
			visitChildren(n, n.typeArguments());
			visitChildren(n, n.arguments());
		}

		public void visit(Block_c n) {
			createDotNode(n, null, "fillcolor=#000000", "fontcolor=#ffffff");
			visitChildren(n, n.statements());
		}

		public void visit(StmtSeq_c n) {
			createDotNode(n, null, "fillcolor=#000000", "fontcolor=#ffffff");
			visitChildren(n, n.statements());
		}

		public void visit(AssignPropertyCall_c n) {
			createDotNode(n, null);
			visitChildren(n, n.arguments());
		}

		public void visit(Empty_c n) {
			createDotNode(n, null, "fillcolor=#000000", "fontcolor=#ffffff");
		}

		public void visit(X10CanonicalTypeNode_c n) {
			createDotNode(n, n.nameString(), "shape=oval", "fillcolor=#C0FFC0");

		}

		public void visit(Return_c n) {
			createDotNode(n, null, "fillcolor=#000000", "fontcolor=#ffffff");
			visitChild(n, n.expr());
		}

		public void visit(X10Binary_c n) {
			createDotNode(n, n.operator().toString(), "shape=oval");
			visitChild(n, n.left());
			visitChild(n, n.right());
		}

		public void visit(X10Unary_c n) {
			createDotNode(n, n.operator().toString(), "shape=oval");
			visitChild(n, n.expr());
		}

		public void visit(ParExpr_c n) { // parentheses
			createDotNode(n, "( )", "shape=oval");
			visitChild(n, n.expr());
		}		

		public void visit(X10Special_c n) {
			createDotNode(n, n.kind().toString(), "shape=oval", "fillcolor=#C0C0FF");
		}

		public void visit(Here_c n) {
			createDotNode(n, "here", "shape=oval", "fillcolor=#C0C0FF");
		}

		public void visit(X10Local_c n) {
			createDotNode(n, n.name().id().toString(), "shape=oval", "fillcolor=#C0C0C0");
		}

		public void visit(Eval_c n) {
			createDotNode(n, null);
			visitChild(n, n.expr());
		}

		public void visit(For_c n) {
			createDotNode(n, null, "shape=parallelogram", "fillcolor=#000000", "fontcolor=#ffffff");
			visitChildren(n, n.inits());
			visitChild(n, n.cond());
			visitChildren(n, n.iters());
			visitChild(n, n.body());
		}

		public void visit(ForLoop_c n) {
			createDotNode(n, null, "shape=parallelogram", "fillcolor=#000000", "fontcolor=#ffffff");
			visitChild(n, n.formal());
			visitChild(n, n.cond());
			visitChild(n, n.domain());
			visitChild(n, n.body());
		}

		public void visit(Branch_c n) {
			createDotNode(n, n.kind()+(n.labelNode()!=null ? "\\n"+n.labelNode().id().toString() : ""), "fillcolor=#000000", "fontcolor=#ffffff");
		}
		
		public void visit(X10Do_c n) {
			createDotNode(n, null, "shape=parallelogram", "fillcolor=#000000", "fontcolor=#ffffff");
			visitChild(n, n.cond());
			visitChild(n, n.body());
		}

		public void visit(X10While_c n) {
			createDotNode(n, null, "shape=parallelogram", "fillcolor=#000000", "fontcolor=#ffffff");
			visitChild(n, n.cond());
			visitChild(n, n.body());
		}


		public void visit(Tuple_c n) {
			createDotNode(n, null, "shape=oval");
			visitChildren(n, n.arguments());
		}

		public void visit(SettableAssign_c n) {
			createDotNode(n, null, "shape=oval");
			visitChild(n, n.left());
			visitChildren(n, n.index());
			visitChild(n, n.right());
		}

		
		public void visit(FieldAssign_c n) {
			createDotNode(n, n.name().id().toString(), "shape=oval", "fillcolor=#008000", "fontcolor=#ffffff");
			visit(n.target());
			visit(n.right());
		}

		public void visit(X10Field_c n) {
			createDotNode(n, n.name().id().toString(), "shape=oval", "fillcolor=#008000", "fontcolor=#ffffff");
			visit(n.target());
		}

		public void visit(X10FieldDecl_c n) {
			createDotNode(n, n.name().id().toString(), "fillcolor=#008000", "fontcolor=#ffffff");
			visitChild(n, n.type());
			visitChild(n, n.init());
		}

		public void visit(X10LocalDecl_c n) {
			createDotNode(n, n.name().id().toString());
			visitChild(n, n.type());
			visitChild(n, n.init());
		}

		public void visit(PropertyDecl_c n) {
			createDotNode(n, n.name().id().toString());
			visitChild(n, n.type());
			visitChild(n, n.init());
		}

		
		public void visit(X10If_c n) {
			createDotNode(n, null, "fillcolor=#000000", "fontcolor=#ffffff");
			visitChild(n, n.cond());
			visitChild(n, n.consequent());
			visitChild(n, n.alternative());
		}

		public void visit(X10Conditional_c n) {
			createDotNode(n, "? :", "shape=oval");
			visitChild(n, n.cond());
			visitChild(n, n.consequent());
			visitChild(n, n.alternative());
		}

		public void visit(Assert_c n) {
			createDotNode(n, null, "color=#FF0000");
			visitChild(n, n.cond());
			visitChild(n, n.errorMessage());
		}


		public void visit(Throw_c n) {
			createDotNode(n, null, "fillcolor=#400000", "fontcolor=#ffffff");
			visitChild(n, n.expr());
		}

		public void visit(Try_c n) {
			createDotNode(n, null, "fillcolor=#400000", "fontcolor=#ffffff");
			visitChild(n, n.tryBlock());
			visitChildren(n, n.catchBlocks());
			visitChild(n, n.finallyBlock());
		}

		public void visit(Catch_c n) {
			createDotNode(n, null, "fillcolor=#400000", "fontcolor=#ffffff");
			visitChild(n, n.formal());
			visitChild(n, n.body());
		}

		
		public void visit(Labeled_c n) {
			createDotNode(n, n.labelNode().id().toString(), "color=#A0A0A0");
			visitChild(n, n.statement());
		}
		

		
		public void visit(X10BooleanLit_c n) {
			createDotNode(n, Boolean.toString(n.value()), "shape=invtriangle", "fillcolor=#A0A0FF");
		}

		public void visit(ClassLit_c n) {
			createDotNode(n, null, "shape=invtriangle", "fillcolor=#A0A0FF");
			visitChild(n, n.typeNode());
		}

		public void visit(X10FloatLit_c n) {
			createDotNode(n, Double.toString(n.value()), "shape=invtriangle", "fillcolor=#A0A0FF");
		}

		public void visit(NullLit_c n) {
			createDotNode(n, "null", "shape=invtriangle", "fillcolor=#A0A0FF");
		}

		public void visit(X10CharLit_c n) {
			createDotNode(n, "" + n.value(), "shape=invtriangle", "fillcolor=#A0A0FF");
		}

		public void visit(IntLit_c n) {
			createDotNode(n, Long.toString(n.value()), "shape=invtriangle", "fillcolor=#A0A0FF");
		}

		public void visit(X10StringLit_c n) {
			createDotNode(n, StringUtil.escape(n.value()), "shape=invtriangle", "fillcolor=#A0A0FF");
		}

		
		public void visit(Finish_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.body());
		}

		public void visit(AtStmt_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtHomeStmt_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtExpr_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtHomeExpr_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.place());
			visitChild(n, n.body());
		}

		public void visit(AtEach_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.formal());
			visitChild(n, n.domain());
			visitChild(n, n.body());
		}

		public void visit(Async_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.body());
		}
		
		public void visit(Atomic_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.body());
		}

		public void visit(When_c n) {
			createDotNode(n, null, "fillcolor=#004040", "fontcolor=#ffffff");
			visitChild(n, n.expr());
			visitChild(n, n.stmt());
		}



		public void visit(X10New_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#FFFFC0");
			visitChildren(n, n.typeArguments());
			visitChildren(n, n.arguments());
			visitChild(n, n.objectType());
			visitChild(n, n.body());
		}

		public void visit(Allocation_c n) {
			createDotNode(n, null, "shape=oval");
			visitChild(n, n.objectType());
		}
				
		public void visit(LocalAssign_c n) {
			createDotNode(n, null, "shape=oval");
			visitChild(n, n.local());
			visitChild(n, n.right());
		}

		public void visit(X10Cast_c n) {
			createDotNode(n, null, "shape=oval");
			visitChild(n, n.castType());
			visitChild(n, n.expr());
		}

		public void visit(X10Instanceof_c n) {
			createDotNode(n, null, "shape=oval");
			visitChild(n, n.compareType());
			visitChild(n, n.expr());
		}

		public void visit(SubtypeTest_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#C0FFC0");
			visitChild(n, n.subtype());
			visitChild(n, n.supertype());
		}

		public void visit(DepParameterExpr_c n) {
			createDotNode(n, null, "shape=oval");
			visitChildren(n, n.formals());
			visitChildren(n, n.condition());
		}

		public void visit(HasZeroTest_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#C0FFC0");
			visitChild(n, n.parameter());
		}

		public void visit(Closure_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#FFA0A0");
			visitChildren(n, n.formals());
			visitChild(n, n.body());
		}

		public void visit(ClosureCall_c n) {
			createDotNode(n, null, "shape=oval", "fillcolor=#FFA0A0");
			visitChild(n, n.target());
			visitChildren(n, n.arguments());
		}

		public void visit(StmtExpr_c n) {
			createDotNode(n, null, "shape=oval");
			visitChildren(n, n.statements());
		}

		
		public void visit(AmbReceiver_c n) {
			createDotNode(n, n.nameNode().id().toString());
		}

		
		public void visit(Switch_c n) {
			createDotNode(n, null);
			visitChild(n, n.expr());
			visitChildren(n, n.elements());
		}

		public void visit(SwitchBlock_c n) {
			createDotNode(n, null);
			visitChildren(n, n.statements());
		}

		public void visit(Case_c n) {
			createDotNode(n, null);
			visitChild(n, n.expr());
		}

		public void visit(LocalTypeDef_c n) {
			createDotNode(n, null);
			visitChild(n, n.typeDef());
		}

		public void visit(Next_c n) {
			createDotNode(n, "here", "shape=oval", "fillcolor=#C0C0FF");
		}

		public void visit(TypeDecl_c n) {
			createDotNode(n, n.name().id().toString(), "fillcolor=#C0FFC0");
			visitChild(n, n.type());
		}

	}
}
