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
public abstract class AbstractTclInterpreter {

    /**
     * A string for the script output
     */
    protected StringBuilder output = new StringBuilder("Tcl> ");

    /**
     * Current parser
     */
    protected AbstractTclParser parser;

    /**
     * Constructor taking a parser as an argument
     *
     * @param parser
     */
    public AbstractTclInterpreter(AbstractTclParser parser) {
        this.parser = parser;
    }

    /**
     * Method, which sets up script
     *
     * @param parser
     */
    public void setParser(AbstractTclParser parser) {
        this.parser = parser;
    }

    /**
     * Returning Tcl parser used
     *
     * @return
     */
    public AbstractTclParser getParser() {
        return parser;
    }

    /**
     * Running the script
     *
     * @return
     * @throws tclinterpreter.TclParser.TclParserError
     */
    public abstract String run() throws AbstractTclParser.TclParserError;

    /**
     * Getting the script output string
     *
     * @return
     */
    public String getOutput() {
        return output.toString();
    }

}
