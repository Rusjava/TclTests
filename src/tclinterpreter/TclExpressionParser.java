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
    public TclExpressionParser(AbstractTclLexer lexer) {
        super(lexer);
    }

    @Override
    public TclNode parse() throws TclParserError {
        /*
         Is the next token an openning paranthesis or number?
         */
        advanceToken(TclTokenType.RIGHTPAR, TclTokenType.REALNUMBER);
        if (currenttoken.type == TclTokenType.REALNUMBER) {
            
        }
    }

}
