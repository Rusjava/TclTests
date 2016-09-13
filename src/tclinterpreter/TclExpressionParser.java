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
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclExpressionParser extends AbstractTclParser {

    /**
     * Constructor
     *
     * @param lexer
     */
    public TclExpressionParser(TclExpressionLexer lexer) {
        super(lexer);
    }

    /**
     * Returning a factor of a binary operation
     *
     * @return
     * @throws tclinterpreter.AbstractTclParser.TclParserError
     */
    protected TclNode getFactor() throws TclParserError {
        TclNode node;
        /*
         * If it begins with a number, return it. If it begins with an opening paranthesis get the expression in paranthesis
         */
        advanceToken(TclTokenType.NUMBER, TclTokenType.LEFTPAR);
        if (currenttoken.type == TclTokenType.NUMBER) {
            node = new TclNode(TclNodeType.NUMBER).setValue(currenttoken.getValue());
        } else {
            node = getExpression();
        }
        return node;
    }

    /**
     * Returning the expression in parentheses
     *
     * @return
     * @throws tclinterpreter.AbstractTclParser.TclParserError
     */
    protected TclNode getExpression() throws TclParserError {
        TclNode arg;
        TclNode op;
        /*
         Is the first token an argument?
         */
        arg = getArgument();
        System.out.println(arg);
        /*
         Cycling over the long expression
         */
        while (currenttoken.type != TclTokenType.EOF && currenttoken.type != TclTokenType.RIGHTPAR) {
            try {
                op = getAddOperation();
                op.getChildren().add(arg);
                arg = getArgument();
                op.getChildren().add(arg);
                arg = op;
            } catch (TclParserError error) {
                if (currenttoken.type != TclTokenType.EOF && currenttoken.type != TclTokenType.RIGHTPAR) {
                    throw error;
                }
            }
        }

        return arg;
    }

    /**
     * Returning an argument of a binary operation
     *
     * @return
     * @throws tclinterpreter.AbstractTclParser.TclParserError
     */
    protected TclNode getArgument() throws TclParserError {
        TclNode fact;
        TclNode op;
        /*
         Is the first token a factor?
         */
        fact = getFactor();
        /*
         Cycling over the long expression
         */
        while (currenttoken.type != TclTokenType.EOF
                && currenttoken.type != TclTokenType.PLUS
                && currenttoken.type != TclTokenType.MINUS
                && currenttoken.type != TclTokenType.RIGHTPAR) {
            try {
                op = getProdOperation();
                op.getChildren().add(fact);
                fact = getFactor();
                op.getChildren().add(fact);
                fact = op;
            } catch (TclParserError error) {
                if (currenttoken.type != TclTokenType.EOF
                        && currenttoken.type != TclTokenType.PLUS
                        && currenttoken.type != TclTokenType.MINUS
                        && currenttoken.type != TclTokenType.RIGHTPAR) {
                    throw error;
                }
            }
        }
        return fact;
    }

    /**
     * Returning a binary additive operation node
     *
     * @return
     * @throws tclinterpreter.AbstractTclParser.TclParserError
     */
    protected TclNode getAddOperation() throws TclParserError {
        TclNode node = new TclNode(TclNodeType.BINARYOP);
        switch (currenttoken.type) {
            case PLUS:
                node.setValue("+");
                break;
            case MINUS:
                node.setValue("-");
                break;
        }
        return node;
    }

    /**
     * Returning a binary product operation node
     *
     * @return
     * @throws tclinterpreter.AbstractTclParser.TclParserError
     */
    protected TclNode getProdOperation() throws TclParserError {
        TclNode node = new TclNode(TclNodeType.BINARYOP);
        advanceToken(TclTokenType.MUL, TclTokenType.DIV);
        switch (currenttoken.type) {
            case MUL:
                node.setValue("*");
                break;
            case DIV:
                node.setValue("/");
                break;
        }
        return node;
    }

    @Override
    public TclNode parse() throws TclParserError {
        /*
        * Returning an expression evaluation result
         */
        TclNode result;
        try {
            result = getExpression();
        } catch (TclParserError error) {
            if (error.ctokentype == TclTokenType.EOF) {
                return new TclNode(TclNodeType.QSTRING).setValue("");
            } else {
                throw error;
            }
        }
        return result;
    }

}
