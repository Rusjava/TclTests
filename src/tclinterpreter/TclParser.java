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
    protected TclToken currenttoken = null;

    /**
     * The previous Tcl token
     */
    protected TclToken previoustoken = null;

    /**
     * The associated TclLexer
     */
    protected TclLexer lexer;

    /**
     * Constructor
     *
     * @param lexer
     */
    public TclParser(TclLexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Advancing to the next token. Throwing and exception if a wrong token
     *
     * @param type
     * @throws tclinterpreter.TclParser.TclParserError
     */
    protected void advanceToken(TclTokenType type) throws TclParserError {
        previoustoken = currenttoken;
        currenttoken = lexer.getToken();
        if (currenttoken.type != type) {
            throw new TclParserError("Parser error", currenttoken);
        }
    }

    /**
     * Advancing to the next token. Throwing and exception if a wrong token
     *
     * @param type1
     * @param type2
     * @throws tclinterpreter.TclParser.TclParserError
     */
    protected void advanceToken(TclTokenType type1, TclTokenType type2) throws TclParserError {
        currenttoken = lexer.getToken();
        if (currenttoken.type != type1 && currenttoken.type != type2) {
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
    protected TclNode getCommand() throws TclParserError {
        TclNode node = new TclNode(TclNodeType.COMMAND);
        advanceToken(TclTokenType.NAME);
        node.setValue(currenttoken.getValue());
        while (currenttoken.type != TclTokenType.EOL && currenttoken.type != TclTokenType.SEMI) {
            try {
                advanceToken(TclTokenType.EOL, TclTokenType.SEMI);
            } catch (TclParserError error) {
                /*
                    A name as an operand
                */
                if (currenttoken.type == TclTokenType.NAME) {
                    node.getChildren().add(new TclNode(TclNodeType.OPERAND).
                            setValue(currenttoken.getValue()));
                /*
                    A string in curly brackets
                    */   
                } else if (currenttoken.type == TclTokenType.LEFTCURL) {
                    try {
                        advanceToken(TclTokenType.STRING);
                        node.getChildren().add(new TclNode(TclNodeType.OPERAND).
                                setValue(currenttoken.getValue()));
                        advanceToken(TclTokenType.RIGHTCURL);
                    } catch (TclParserError innererror) {
                        if (currenttoken.type == TclTokenType.RIGHTCURL) {
                            node.getChildren().add(new TclNode(TclNodeType.OPERAND).
                                    setValue(""));
                            break;
                        } else {
                            throw innererror;
                        }
                    }  
                /*
                    A command in brackets
                    */    
                } else if (currenttoken.type == TclTokenType.LEFTBR) {
                    advanceToken(TclTokenType.STRING);
                /*
                    A string in quotes
                    */    
                } else if (currenttoken.type == TclTokenType.LEFTQ) {
                     try {
                        advanceToken(TclTokenType.STRING);
                        node.getChildren().add(new TclNode(TclNodeType.OPERAND).
                                setValue(processstring(currenttoken.getValue())));
                        advanceToken(TclTokenType.RIGHTQ);
                    } catch (TclParserError innererror) {
                        if (currenttoken.type == TclTokenType.RIGHTQ) {
                            node.getChildren().add(new TclNode(TclNodeType.OPERAND).
                                    setValue(""));
                            break;
                        } else {
                            throw innererror;
                        }
                    }
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
    public TclNode parse() throws TclParserError {
        TclNode node = new TclNode(TclNodeType.PROGRAM);
        try {
            while (true) {
                node.getChildren().add(getCommand());
            }
        } catch (TclParserError error) {
            if (currenttoken.type == TclTokenType.EOF) {
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
        TclToken token;

        /**
         * Constructor
         *
         * @param message
         * @param token
         */
        public TclParserError(String message, TclToken token) {
            super();
            this.message = message;
            this.token = token;
        }

        @Override
        public String toString() {
            return message + " (token: " + token.type + ");";
        }
    }
}
