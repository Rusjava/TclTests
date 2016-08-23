/*
 * Copyright (C) 2016 Ruslan Feshchenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tclinterpreter;

/**
 * A class for TCL lexer
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TCLLexer {

    /**
     * Tcl keywords
     */
    public static final String[] KEY_WORDS = {"set", "unset", "puts", "expr"};

    /**
     * TCL script
     */
    protected String script = null;
    /**
     * Current position in script
     */
    protected int pos = 0;
    /**
     * Current symbol at position of pos
     */
    protected char currentchar = 0;
    
    /**
     * Flag indicating that the lexer is inside quotation
     */
    protected boolean qflag;

    /**
     * Constructor
     *
     * @param script a TCL script to interpret
     */
    public TCLLexer(String script) {
        this.script = script;
        currentchar = script.charAt(pos);
    }

    /**
     * Advance position by one
     */
    protected void advancePosition() {
        pos++;
        if (pos < script.length()) {
            currentchar = script.charAt(pos);
        } else {
            currentchar = 0;
        }
    }

    /**
     * What is the next character?
     *
     * @return the next character
     */
    protected char peek() {
        if (pos < script.length() - 1) {
            return script.charAt(pos + 1);
        } else {
            return 0;
        }
    }

    /**
     * What was the previous character?
     *
     * @return the previous character
     */
    protected char retropeek() {
        if (pos > 0) {
            return script.charAt(pos - 1);
        } else {
            return 0;
        }
    }

    /**
     * Skipping white space
     */
    protected void skipSpace() {
        while (Character.isWhitespace(currentchar) && currentchar != 0) {
            advancePosition();
        }
    }

    /**
     * Reading a real number from the script
     *
     * @return
     */
    protected String readNumber() {
        String number = "";
        number += currentchar;
        /*
         This is a number if didgit, dot and exponetial characters are present     
         */
        while (Character.isDigit(currentchar)
                || currentchar == '.'
                || (Character.toLowerCase(currentchar) == 'e'
                && (peek() == '-') || peek() == '+')) {
            advancePosition();
            number += currentchar;
            if (Character.toLowerCase(currentchar) == 'e') {
                advancePosition();
                number += currentchar;
            }
        }
        return number;
    }

    /**
     * Reading alphanumerical names from the script
     *
     * @return
     */
    protected String readName() {
        String name = "";
        while (Character.isDigit(currentchar)
                || Character.isLetter(currentchar)
                || currentchar == '_') {
            name += currentchar;
            advancePosition();    
        }
        return name;
    }

    /**
     * Reading end of line symbol
     */
    protected void readEOL() {
        while (currentchar == '\n' || currentchar == '\t') {
            advancePosition();
        }
    }

    /**
     * Reading the string between quotes of curly braces with ends of lines skipped
     *
     * @return
     */
    protected String readString() {
        String string = "";
        string += currentchar;
        while (currentchar != '"' && currentchar != '}' && currentchar != '\n') {
            advancePosition();
            if ((currentchar !='\\' || peek() !='\n')
                 && (currentchar !='\n' || retropeek() !='\\')
                 && (currentchar !='\t' || retropeek() !='\n')   ) {
                string += currentchar;
            }        
        }
        return string;
    }

    /**
     * Getting the next Tcl token
     *
     * @return
     */
    public TclToken getToken() {
        /*
         Skipping any leading white space
         */
        if (Character.isWhitespace(currentchar)) {
            skipSpace();
        }
        /*
         What is the next token
         */
        if (Character.isDigit(currentchar)) {
            return new TclToken(TclTokenType.REALNUMBER).setValue(readNumber());
        } else if (Character.isLetter(currentchar) || currentchar == '_') {
            return new TclToken(TclTokenType.NAME).setValue(readName());
        } else if (currentchar == '+') {
            advancePosition();
            return new TclToken(TclTokenType.PLUS);
        } else if (currentchar == '-') {
            advancePosition();
            return new TclToken(TclTokenType.MINUS);
        } else if (currentchar == '*') {
            advancePosition();
            return new TclToken(TclTokenType.MUL);
        } else if (currentchar == '/') {
            advancePosition();
            return new TclToken(TclTokenType.DIV);
        } else if (currentchar == '(') {
            advancePosition();
            return new TclToken(TclTokenType.LEFTPAR);
        } else if (currentchar == ')') {
            advancePosition();
            return new TclToken(TclTokenType.RIGHTPAR);
        } else if (currentchar == '[') {
            advancePosition();
            return new TclToken(TclTokenType.LEFTBR);
        } else if (currentchar == ']') {
            advancePosition();
            return new TclToken(TclTokenType.RIGHTBR);
        } else if (currentchar == '+') {
            advancePosition();
            return new TclToken(TclTokenType.PLUS);
        } else if (currentchar == '{') {
            advancePosition();
            return new TclToken(TclTokenType.LEFTCURL);
        } else if (currentchar == '}') {
            advancePosition();
            return new TclToken(TclTokenType.RIGHTCURL);
        } else if (currentchar == '"' && !qflag) {
            advancePosition();
            qflag=true;
            return new TclToken(TclTokenType.LEFTQ);
        } else if (currentchar == '"' && qflag) {
            advancePosition();
            qflag=false;
            return new TclToken(TclTokenType.RIGHTQ);
        } else if (currentchar == ';') {
            advancePosition();
            return new TclToken(TclTokenType.SEMI);
        } else if (currentchar == '\n') {
            readEOL();
            return new TclToken(TclTokenType.EOL);
        } else if ((retropeek() == '"' && qflag) || retropeek() == '{') {
            return new TclToken(TclTokenType.STRING).setValue(readString());
        } else if (currentchar == 0) {
            return new TclToken(TclTokenType.EOF);
        }
        return null;
    }

    /**
     * Returning the Tcl script
     *
     * @return
     */
    public String getScript() {
        return script;
    }
}
