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
     * The previous Tcl token
     */
    protected TCLTokenType previoustoken = null;

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
     * Advancing to the next token. Throwing and exception if a wrong token
     *
     * @param token
     * @throws tclinterpreter.TclParser.TclParserError
     */
    protected void advanceToken(TCLTokenType token) throws TclParserError {
        previoustoken = currenttoken;
        currenttoken = lexer.getToken();
        if (currenttoken != token) {
            throw new TclParserError("Parser error", currenttoken);
        }
    }

    /**
     * Advancing to the next token. Throwing and exception if a wrong token
     *
     * @param token1
     * @param token2
     * @throws tclinterpreter.TclParser.TclParserError
     */
    protected void advanceToken(TCLTokenType token1, TCLTokenType token2) throws TclParserError {
        currenttoken = lexer.getToken();
        if (currenttoken != token1 || currenttoken != token2) {
            throw new TclParserError("Parser error", currenttoken);
        }
    }

    /**
     * Making necessary substitutions in a quote enclosed string
     * 
     * @param str
     * @return
     */
    protected String processstring (String str) {
         return str;
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
        while (currenttoken != TCLTokenType.EOL || currenttoken != TCLTokenType.SEMI) {
            try {
                advanceToken(TCLTokenType.EOL, TCLTokenType.SEMI);
            } catch (TclParserError error) {
                if (currenttoken == TCLTokenType.NAME) {
                    node.getChildren().add(TCLNodeType.OPERAND.
                            setValue(currenttoken.getValue()));
                /*
                    A string in curly brackets
                    */   
                } else if (currenttoken == TCLTokenType.LEFTCURL) {
                    try {
                        advanceToken(TCLTokenType.STRING);
                        node.getChildren().add(TCLNodeType.OPERAND.
                                setValue(currenttoken.getValue()));
                    } catch (TclParserError innererror) {
                        if (currenttoken == TCLTokenType.RIGHTCURL) {
                            node.getChildren().add(TCLNodeType.OPERAND.
                                    setValue(""));
                            break;
                        } else {
                            throw innererror;
                        }
                    }
                    advanceToken(TCLTokenType.RIGHTCURL);
                } else if (currenttoken == TCLTokenType.LEFTBR) {
                    advanceToken(TCLTokenType.STRING);
                /*
                    A string in quotes
                    */    
                } else if (currenttoken == TCLTokenType.LEFTQ) {
                     try {
                        advanceToken(TCLTokenType.STRING);
                        node.getChildren().add(TCLNodeType.OPERAND.
                                setValue(processstring(currenttoken.getValue())));
                    } catch (TclParserError innererror) {
                        if (currenttoken == TCLTokenType.RIGHTQ) {
                            node.getChildren().add(TCLNodeType.OPERAND.
                                    setValue(""));
                            break;
                        } else {
                            throw innererror;
                        }
                    }
                    advanceToken(TCLTokenType.RIGHTQ);
                } else {
                    throw error;
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
    public static class TclParserError extends Exception {

        String message;
        TCLTokenType token;

        /**
         * Constructor
         *
         * @param message
         * @param token
         */
        public TclParserError(String message, TCLTokenType token) {
            super();
            this.message = message;
            this.token = token;
        }

        @Override
        public String toString() {
            return message + " (token: " + token.getValue() + ");";
        }
    }
}
