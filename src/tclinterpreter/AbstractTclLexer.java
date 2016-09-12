/*
 * Copyright (C) 2015 Ruslan Feshchenko
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
 * Basic abstract class for Tcl lexers
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public abstract class AbstractTclLexer {

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
     * A general constructor of parsers
     *
     * @param script
     */
    public AbstractTclLexer(String script) {
        this.script = script;
        if (script.length() > 0) {
            currentchar = script.charAt(pos);
        } else {
            currentchar = 0;
        }
    }

    /**
     * Advance position by one
     */
    protected void advancePosition() {
        if (++pos < script.length()) {
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
     * Getting the next Tcl token
     *
     * @return
     */
    public abstract TclToken getToken();

    /**
     * Returning the Tcl script
     *
     * @return
     */
    public String getScript() {
        return script;
    }

    /**
     * Skipping white space
     */
    protected void skipWhitespace() {
        while (Character.isWhitespace(currentchar) && currentchar != 0) {
            advancePosition();
        }
    }

}
