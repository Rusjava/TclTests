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
 * A special lexer for quoted enclosed strings
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclStringLexer extends AbstractTclLexer {

    public TclStringLexer(String script) {
        super(script);
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
                advancePosition();
            }
            name.append(currentchar);
            advancePosition();
        }
        return name.toString();
    }

    @Override
    public TclToken getToken() {
        /*
         What is the next token
         */
        if (currentchar == '[') {
            advancePosition();
            return new TclToken(TclTokenType.LEFTBR);
        } else if (currentchar == ']') {
            advancePosition();
            return new TclToken(TclTokenType.RIGHTBR);
        } else if (currentchar == '$') {
            advancePosition();
            return new TclToken(TclTokenType.DOLLAR);
        } else if ((currentchar == '_' || Character.isLetter(currentchar))
                && retropeek() == '$') {
            /*
             Returning a name token
             */
            return new TclToken(TclTokenType.NAME).setValue(readName());
        } else {
            return new TclToken(TclTokenType.STRING);
        }
    }
}
