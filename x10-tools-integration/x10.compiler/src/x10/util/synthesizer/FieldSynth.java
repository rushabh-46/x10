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
package x10.util.synthesizer;

import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.FieldDecl;
import polyglot.ast.FlagsNode;
import polyglot.ast.Id;
import polyglot.types.ClassDef;
import polyglot.types.Flags;
import polyglot.types.Name;
import polyglot.types.Ref;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.Types;
import polyglot.util.Position;
import x10.ast.X10FieldDecl;
import x10.ast.X10NodeFactory;
import x10.types.X10Context;
import x10.types.X10FieldDef;

/**
 * Synthesizer to construct a field for a class
 * 
 */
public class FieldSynth extends AbstractStateSynth implements IClassMemberSynth {

    X10FieldDef fieldDef;
    X10FieldDecl fieldDecl;

    public FieldSynth(X10NodeFactory xnf, X10Context xct, Position pos, ClassDef classDef, Name name, Flags flags,
            Type type) {
        super(xnf, xct, pos);

        fieldDef = (X10FieldDef) xts.fieldDef(pos, Types.ref(classDef.asType()), flags, Types.ref(type), name);
        classDef.addField(fieldDef);

    }

    public FieldSynth(X10NodeFactory xnf, X10Context xct, ClassDef classDef, String name, Type type) {
        this(xnf, xct, compilerPos, classDef, name, type);
    }

    public FieldSynth(X10NodeFactory xnf, X10Context xct, Position pos, ClassDef classDef, String name, Type type) {
        this(xnf, xct, pos, classDef, Name.make(name), Flags.NONE, type);
    }

    public void setType(Type type){
        try {
            checkClose();
            fieldDef.setType(Types.ref(type));
        } catch (StateSynthClosedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setFlags(Flags flags) {
        try {
            checkClose();
            fieldDef.setFlags(flags);
        } catch (StateSynthClosedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public X10FieldDef getDef() {
        return fieldDef;
    }

    public FieldDecl close() throws SemanticException {
        if (closed) {
            return fieldDecl; // just return the field
        }
        closed = true;
        
        Name name = fieldDef.name();
        Flags flags = fieldDef.flags();
        Ref<? extends Type> type = fieldDef.type();

        Id id = xnf.Id(pos, name);
        CanonicalTypeNode tnode = xnf.CanonicalTypeNode(pos, type);
        FlagsNode fnode = xnf.FlagsNode(pos, flags);
        fieldDecl = (X10FieldDecl) xnf.FieldDecl(pos, fnode, tnode, id).fieldDef(fieldDef);
        return fieldDecl;
    }

}
