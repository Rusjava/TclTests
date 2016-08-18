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
     * Constructor
     *
     * @param script a TCL script to interpret
     */
    public void TCLLexer(String script) {
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
        return script.charAt(pos + 1);
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
     * @return 
     */
    protected String readNumber () {
        String number="";
                number+=currentchar;
         /*
          This is a number if didgit, dot and exponetial characters are present     
         */     
        while (Character.isDigit(currentchar) || 
                currentchar=='.' || 
                (Character.toLowerCase(currentchar)=='e' && 
                (peek()=='-') || peek()=='+')) {
            advancePosition();  
            number+=currentchar;
         if (Character.toLowerCase(currentchar)=='e') {
            advancePosition();  
            number+=currentchar; 
         }        
        }
        return number;
    }
    
    /**
     * Getting the next TCL token
     *
     * @return
     */
    public TCLTokenType getToken() {
        /*
        Skipping any leading white space
        */
        if (Character.isWhitespace(currentchar)) {
            skipSpace();
        }
        /*
        What is the next token
        */
        if (currentchar == 0) {
            return TCLTokenType.EOF;
        } else if (Character.isDigit(currentchar)) {
            advancePosition();
            return TCLTokenType.NUMBER.setValue(readNumber());
        }
        return null;
    }
}
