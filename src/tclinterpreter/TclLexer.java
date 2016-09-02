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
public class TclLexer {
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
     * Flag indicating that the lexer is inside curly brackets
     */
    protected boolean curlyflag;

    /**
     * Constructor
     *
     * @param script a TCL script to interpret
     */
    public TclLexer(String script) {
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
        StringBuilder number = new StringBuilder("");
        /*
         This is a number if didgit, dot and exponetial characters are present     
         */
        while (Character.isDigit(currentchar)
                || currentchar == '.'
                || (Character.toLowerCase(currentchar) == 'e'
                && (peek() == '-') || peek() == '+')
                || currentchar == '\\') {
            if ((currentchar == '\\' && peek() == '\n')
                    || (currentchar == '\\' && peek() == '\r')) {
                skipEOL();
            }
            number.append(currentchar);
            advancePosition();
            if (Character.toLowerCase(currentchar) == 'e') {
                advancePosition();
                number.append(currentchar);
            }
        }
        return number.toString();
    }

    /**
     * Reading alphanumerical names from the script
     *
     * @return
     */
    protected String readName() {
        StringBuilder name = new StringBuilder("");
        while (Character.isDigit(currentchar)
                || Character.isLetter(currentchar)
                || currentchar == '_') {
            if (currentchar == '\\') {
               if (peek() == '\n' || peek() == '\r') {
                   skipEOL();
               } else {
                   advancePosition();
               }
            }
            name.append(currentchar);
            advancePosition();
        }
        return name.toString();
    }

    /**
     * Reading Tcl words
     *
     * @return
     */
    protected String readWord() {
        StringBuilder name = new StringBuilder("");
        while (!Character.isWhitespace(currentchar) && currentchar != '[' 
                && currentchar != ';' && currentchar != '$') {
            if (currentchar == '\\') {
               if (peek() == '\n' || peek() == '\r') {
                   skipEOL();
               } else {
                   advancePosition();
               }
            }
            name.append(currentchar);
            advancePosition();
        }
        return name.toString();
    }

    /**
     * Reading end of line symbol
     */
    protected void readEOL() {
        while (Character.isWhitespace(currentchar)) {
            advancePosition();
        }
    }

    /**
     * Skipping end of line and whitespace after slash
     */
    protected void skipEOL() {
        do {
            advancePosition();
        } while (Character.isWhitespace(currentchar));
    }

    /**
     * Skipping escaped special character
     */
    protected void skipEscaped() {
        do {
            advancePosition();
        } while (Character.isWhitespace(currentchar));
    }

    /**
     * Reading the string between quotes of curly braces with ends of lines
     * skipped
     *
     * @return
     */
    protected String readString() {
        String string = "";
        while ((currentchar != '"' || !qflag) && (currentchar != '}' || !curlyflag)
                && currentchar != ']') {
            if ((currentchar == '\\' && peek() == '\n')
                    || (currentchar == '\\' && peek() == '\r')) {
                skipEOL();
            }
            string += currentchar;
            advancePosition();
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
         What is the next token
         */
        if ((currentchar == '\\' && peek() == '\n')
                || (currentchar == '\\' && peek() == '\r')) {
            /*
             Skipping escape end of line
             */
            skipEOL();
        }
        if (currentchar == '{') {
            /*
             Returning a left brace token
             */
            curlyflag = true;
            advancePosition();
            return new TclToken(TclTokenType.LEFTCURL);
        } else if (currentchar == '}') {
            /*
             Returning a right brace token
             */
            curlyflag = false;
            advancePosition();
            return new TclToken(TclTokenType.RIGHTCURL);
        } else if (currentchar == '"' && !qflag) {
            /*
             Returning a left quote token
             */
            advancePosition();
            qflag = true;
            return new TclToken(TclTokenType.LEFTQ);
        } else if (currentchar == '"' && qflag) {
            /*
             Returning a right quote token
             */
            advancePosition();
            qflag = false;
            return new TclToken(TclTokenType.RIGHTQ);
        } else if (currentchar == ';') {
            /*
             Returning a semi-colon token
             */
            advancePosition();
            return new TclToken(TclTokenType.SEMI);
        } else if (currentchar == '\n' || currentchar == '\r') {
            /*
             Returning an end of line token
             */
            readEOL();
            return new TclToken(TclTokenType.EOL);
        } else if (Character.isWhitespace(currentchar)) {
            /*
             Skipping whitespace and returning a whitespace token
             */
            skipSpace();
            return new TclToken(TclTokenType.WHITESPACE);
        }
        if (currentchar == '[') {
            /*
             Returning a left bracket token
             */
            advancePosition();
            return new TclToken(TclTokenType.LEFTBR);
        } else if (currentchar == ']') {
            /*
             Returning a right bracket token
             */
            advancePosition();
            return new TclToken(TclTokenType.RIGHTBR);
        } else if (currentchar == '$') {
            /*
             Returning a dollar token
             */
            advancePosition();
            return new TclToken(TclTokenType.DOLLAR);
        } else if ((currentchar == '_' || Character.isLetter(currentchar))
                && retropeek() == '$') {
            /*
             Returning a name token
             */
            return new TclToken(TclTokenType.NAME).setValue(readName());
        } else if ((retropeek() == '"' && qflag)
                || retropeek() == '{' || retropeek() == '[') {
            /*
             Reading and returning a string of symbols
             */
            return new TclToken(TclTokenType.STRING).setValue(readString());
        } else if ((currentchar == '_'
                || Character.isDigit(currentchar) || Character.isLetter(currentchar))) {
            /*
             Returning a Tclword token
             */
            return new TclToken(TclTokenType.WORD).setValue(readWord());
        } else if (currentchar == 0) {
            /*
             Returning an end of file token
             */
            return new TclToken(TclTokenType.EOF);
        } else {
            /*
             Returning UNKNOWN token in all other cases
             */
            return new TclToken(TclTokenType.UNKNOWN);
        }
    }

    /**
     * Analysis of numerical expressions
     *
     * @return
     */
    public TclToken getExprToken() {
        if (Character.isDigit(currentchar)) {
            /*
             Returning a real number token
             */
            return new TclToken(TclTokenType.REALNUMBER).setValue(readNumber());
        } else if (currentchar == '+') {
            /*
             Returning a plus op token
             */
            advancePosition();
            return new TclToken(TclTokenType.PLUS);
        } else if (currentchar == '-') {
            /*
             Returning a minus op token
             */
            advancePosition();
            return new TclToken(TclTokenType.MINUS);
        } else if (currentchar == '*') {
            /*
             Returning a multiplication op token
             */
            advancePosition();
            return new TclToken(TclTokenType.MUL);
        } else if (currentchar == '/') {
            /*
             Returning a division op token
             */
            advancePosition();
            return new TclToken(TclTokenType.DIV);
        } else if (currentchar == '(') {
            /*
             Returning a left paranthesis op token
             */
            advancePosition();
            return new TclToken(TclTokenType.LEFTPAR);
        } else if (currentchar == ')') {
            /*
             Returning a right paranthesis op token
             */
            advancePosition();
            return new TclToken(TclTokenType.RIGHTPAR);
        } else {
            /*
             Returning an unknown token
             */
            advancePosition();
            return new TclToken(TclTokenType.UNKNOWN);
        }
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
