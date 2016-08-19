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
        currenttoken = lexer.getToken();
        if (currenttoken != token) {
            throw new TclParserError("Parser error");
        }
    }

    /**
     * Advancing to the next token. Returning false if wrong token
     *
     * @param token
     * @return
     */
    protected boolean advanceAndCompareToken(TCLTokenType token) {
        return lexer.getToken() == token;
    }

    /**
     * Reading the command and creating the corresponding node
     *
     * @return
     * @throws TclParserError
     */
    protected TCLNodeType getCommand() throws TclParserError {
        TCLNodeType node = TCLNodeType.COMMAND;
        advanceToken(TCLTokenType.NAME);
        node.setValue(currenttoken.getValue());
        while (currenttoken != TCLTokenType.EOL) {
            try {
                advanceToken(TCLTokenType.EOL);
            } catch (TclParserError error) {
                if (currenttoken == TCLTokenType.NAME) {
                    node.getChildren().add(TCLNodeType.OPERAND.
                            setValue(currenttoken.getValue()));
                } else if (currenttoken == TCLTokenType.STRING) {
                    node.getChildren().add(TCLNodeType.OPERAND.
                            setValue(currenttoken.getValue()));
                }
            }
        }
        return node;
    }

    /**
     * Parsing the script and creating the node tree consisting of commands
     *
     * @return
     * @throws tclinterpreter.TclParser.TclParserError
     */
    public TCLNodeType parse() throws TclParserError {
        TCLNodeType node = TCLNodeType.PROGRAM;
        try {
            while (true) {
                node.getChildren().add(getCommand());
                advanceToken(TCLTokenType.EOL);
            }
        } catch (TclParserError error) {
            if (currenttoken == TCLTokenType.EOF) {
                return node;
            }
            throw error;
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
