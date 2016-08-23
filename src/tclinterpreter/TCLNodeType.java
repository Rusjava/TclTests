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

import java.util.List;
import java.util.ArrayList;

/**
 * An enumeration for Tcl node types
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public enum TCLNodeType {

    BINARYOP("BO"),
    UNARYOP("UO"),
    PROGRAM("PROG"),
    COMMAND("CMD"),
    QSTRING("QS"),
    BSTRING("BS"),
    OPERAND("OP");

    private final String type;
    private String value = "";
    private final List<TCLNodeType> children;

    /**
     * Constructor
     *
     * @param value
     */
    private TCLNodeType(String type) {
        this.type = type;
        this.children = new ArrayList<>();
    }

    /**
     * Returning value of the node
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Setting the node's value
     *
     * @param value
     * @return
     */
    public TCLNodeType setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Returning a list with node's children
     *
     * @return
     */
    public List getChildren() {
        return children;
    }

    @Override
    public String toString() {
        String str = "nodeType: " + type + ", " + "nodeValue: " + getValue() + " (";
        List<TCLNodeType> chld = getChildren();
        for (TCLNodeType node : chld) {
            str += node + "; ";
        }
        str += ")";
        return str;
    }
}
