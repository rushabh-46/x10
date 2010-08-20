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

package x10.parser;

public interface X10Parsersym {
    public final static int
      TK_IntegerLiteral = 75,
      TK_LongLiteral = 76,
      TK_FloatingPointLiteral = 77,
      TK_DoubleLiteral = 78,
      TK_CharacterLiteral = 79,
      TK_StringLiteral = 80,
      TK_MINUS_MINUS = 85,
      TK_OR = 116,
      TK_MINUS = 89,
      TK_MINUS_EQUAL = 122,
      TK_NOT = 87,
      TK_NOT_EQUAL = 117,
      TK_REMAINDER = 107,
      TK_REMAINDER_EQUAL = 123,
      TK_AND = 118,
      TK_AND_AND = 120,
      TK_AND_EQUAL = 124,
      TK_LPAREN = 1,
      TK_RPAREN = 72,
      TK_MULTIPLY = 104,
      TK_MULTIPLY_EQUAL = 125,
      TK_COMMA = 84,
      TK_DOT = 91,
      TK_DIVIDE = 108,
      TK_DIVIDE_EQUAL = 126,
      TK_COLON = 98,
      TK_SEMICOLON = 83,
      TK_QUESTION = 127,
      TK_AT = 2,
      TK_LBRACKET = 71,
      TK_RBRACKET = 105,
      TK_XOR = 119,
      TK_XOR_EQUAL = 128,
      TK_LBRACE = 92,
      TK_OR_OR = 121,
      TK_OR_EQUAL = 129,
      TK_RBRACE = 95,
      TK_TWIDDLE = 93,
      TK_PLUS = 90,
      TK_PLUS_PLUS = 86,
      TK_PLUS_EQUAL = 130,
      TK_LESS = 109,
      TK_LEFT_SHIFT = 110,
      TK_LEFT_SHIFT_EQUAL = 131,
      TK_RIGHT_SHIFT = 111,
      TK_RIGHT_SHIFT_EQUAL = 132,
      TK_UNSIGNED_RIGHT_SHIFT = 112,
      TK_UNSIGNED_RIGHT_SHIFT_EQUAL = 133,
      TK_LESS_EQUAL = 113,
      TK_EQUAL = 94,
      TK_EQUAL_EQUAL = 101,
      TK_GREATER = 114,
      TK_GREATER_EQUAL = 115,
      TK_ELLIPSIS = 137,
      TK_RANGE = 136,
      TK_ARROW = 88,
      TK_DARROW = 134,
      TK_SUBTYPE = 97,
      TK_SUPERTYPE = 103,
      TK_abstract = 7,
      TK_as = 28,
      TK_assert = 45,
      TK_async = 46,
      TK_at = 47,
      TK_ateach = 36,
      TK_atomic = 18,
      TK_await = 48,
      TK_break = 49,
      TK_case = 24,
      TK_catch = 29,
      TK_class = 12,
      TK_clocked = 30,
      TK_const = 50,
      TK_continue = 51,
      TK_def = 13,
      TK_default = 25,
      TK_do = 37,
      TK_else = 38,
      TK_extends = 31,
      TK_extern = 19,
      TK_false = 52,
      TK_final = 8,
      TK_finally = 32,
      TK_finish = 96,
      TK_for = 39,
      TK_foreach = 40,
      TK_future = 100,
      TK_global = 16,
      TK_goto = 53,
      TK_here = 54,
      TK_if = 55,
      TK_implements = 41,
      TK_import = 33,
      TK_in = 106,
      TK_incomplete = 20,
      TK_instanceof = 26,
      TK_interface = 10,
      TK_local = 56,
      TK_native = 14,
      TK_new = 57,
      TK_next = 58,
      TK_nonblocking = 21,
      TK_null = 59,
      TK_offer = 60,
      TK_offers = 42,
      TK_operator = 99,
      TK_or = 43,
      TK_package = 34,
      TK_private = 3,
      TK_property = 17,
      TK_protected = 4,
      TK_proto = 22,
      TK_public = 5,
      TK_return = 61,
      TK_safe = 11,
      TK_self = 62,
      TK_sequential = 23,
      TK_shared = 27,
      TK_static = 6,
      TK_struct = 15,
      TK_super = 74,
      TK_switch = 63,
      TK_this = 73,
      TK_throw = 64,
      TK_throws = 44,
      TK_true = 65,
      TK_try = 66,
      TK_type = 9,
      TK_val = 67,
      TK_var = 68,
      TK_when = 69,
      TK_while = 35,
      TK_EOF_TOKEN = 135,
      TK_IDENTIFIER = 70,
      TK_SlComment = 138,
      TK_MlComment = 139,
      TK_DocComment = 140,
      TK_UnsignedIntegerLiteral = 81,
      TK_UnsignedLongLiteral = 82,
      TK_ErrorId = 102,
      TK_TypeModifier = 141,
      TK_ERROR_TOKEN = 142;

    public final static String orderedTerminalSymbols[] = {
                 "",
                 "LPAREN",
                 "AT",
                 "private",
                 "protected",
                 "public",
                 "static",
                 "abstract",
                 "final",
                 "type",
                 "interface",
                 "safe",
                 "class",
                 "def",
                 "native",
                 "struct",
                 "global",
                 "property",
                 "atomic",
                 "extern",
                 "incomplete",
                 "nonblocking",
                 "proto",
                 "sequential",
                 "case",
                 "default",
                 "instanceof",
                 "shared",
                 "as",
                 "catch",
                 "clocked",
                 "extends",
                 "finally",
                 "import",
                 "package",
                 "while",
                 "ateach",
                 "do",
                 "else",
                 "for",
                 "foreach",
                 "implements",
                 "offers",
                 "or",
                 "throws",
                 "assert",
                 "async",
                 "at",
                 "await",
                 "break",
                 "const",
                 "continue",
                 "false",
                 "goto",
                 "here",
                 "if",
                 "local",
                 "new",
                 "next",
                 "null",
                 "offer",
                 "return",
                 "self",
                 "switch",
                 "throw",
                 "true",
                 "try",
                 "val",
                 "var",
                 "when",
                 "IDENTIFIER",
                 "LBRACKET",
                 "RPAREN",
                 "this",
                 "super",
                 "IntegerLiteral",
                 "LongLiteral",
                 "FloatingPointLiteral",
                 "DoubleLiteral",
                 "CharacterLiteral",
                 "StringLiteral",
                 "UnsignedIntegerLiteral",
                 "UnsignedLongLiteral",
                 "SEMICOLON",
                 "COMMA",
                 "MINUS_MINUS",
                 "PLUS_PLUS",
                 "NOT",
                 "ARROW",
                 "MINUS",
                 "PLUS",
                 "DOT",
                 "LBRACE",
                 "TWIDDLE",
                 "EQUAL",
                 "RBRACE",
                 "finish",
                 "SUBTYPE",
                 "COLON",
                 "operator",
                 "future",
                 "EQUAL_EQUAL",
                 "ErrorId",
                 "SUPERTYPE",
                 "MULTIPLY",
                 "RBRACKET",
                 "in",
                 "REMAINDER",
                 "DIVIDE",
                 "LESS",
                 "LEFT_SHIFT",
                 "RIGHT_SHIFT",
                 "UNSIGNED_RIGHT_SHIFT",
                 "LESS_EQUAL",
                 "GREATER",
                 "GREATER_EQUAL",
                 "OR",
                 "NOT_EQUAL",
                 "AND",
                 "XOR",
                 "AND_AND",
                 "OR_OR",
                 "MINUS_EQUAL",
                 "REMAINDER_EQUAL",
                 "AND_EQUAL",
                 "MULTIPLY_EQUAL",
                 "DIVIDE_EQUAL",
                 "QUESTION",
                 "XOR_EQUAL",
                 "OR_EQUAL",
                 "PLUS_EQUAL",
                 "LEFT_SHIFT_EQUAL",
                 "RIGHT_SHIFT_EQUAL",
                 "UNSIGNED_RIGHT_SHIFT_EQUAL",
                 "DARROW",
                 "EOF_TOKEN",
                 "RANGE",
                 "ELLIPSIS",
                 "SlComment",
                 "MlComment",
                 "DocComment",
                 "TypeModifier",
                 "ERROR_TOKEN"
             };

    public final static int numTokenKinds = orderedTerminalSymbols.length;
    public final static boolean isValidForParser = true;
}
