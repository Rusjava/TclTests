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
 * A class for the Tcl parser which converts the token stream into a tree
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclParser {

    /**
     * The current Tcl token
     */
    protected TCLTokenType currenttoken;
    /**
     * The associated TclLexer
     */

    protected TCLLexer lexer;

    /**
     * Constructor
     *
     * @param lexer
     */
    public TclParser(TCLLexer lexer) {
        this.lexer = lexer;
        currenttoken = lexer.getToken();
    }

    /**
     * Advancing to the next token. Throwing and exception if wrong token
     *
     * @param token
     * @throws tclinterpreter.TclParser.TclParserError
     */
    protected void advanceToken(TCLTokenType token) throws TclParserError {
        if (lexer.getToken() != token) {
            throw new TclParserError ("Parser error");
        }
    }

    /**
     * A class for Tcl parser errors
     */
    static class TclParserError extends Exception {

        String message;

        /**
         * Constructor
         *
         * @param message
         */
        public TclParserError(String message) {
            super();
            this.message = message;
        }
    }
}
