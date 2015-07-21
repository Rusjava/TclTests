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

/*
 * This class interpretes Tcl scripts
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclInterpreter {

    /**
     * Tcl keywords
     */
    public static final String[] KEY_WORDS = {"set", "unset", "puts", "expr"};

    /**
     * Current script 
     */
    protected String script = "";
    
    /**
     * Default constructor
     */
    public TclInterpreter () {
        super();
    }
    
    /**
     * Constructor, which sets up script
     * @param script
     */
    public TclInterpreter(String script) {
        super();
        this.script=script;
    }
    
    /**
     * Method, which sets up script
     * @param script
     */
    public void setScript (String script) {
        this.script=script;
        System.out.println(script);
    }
}
