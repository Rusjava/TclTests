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

package openhtml;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.DocumentParser;
/*
 * A class extending HTMLEditorKit.Parser to create a new version of ParserDelegator
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class OpenParserDelegator extends HTMLEditorKit.Parser {
    
    protected DTD dtd;
    
    /**
     * Constructor setting an arbitrary DTD
     * @param dtd
     */
    public OpenParserDelegator (DTD dtd) {
        this.dtd = dtd;
    }
    
    /**
     * Constructor setting default HTML 3.2 DTD
     */
    public OpenParserDelegator () {
        this(getDefaultDTD());
    }
    
    /**
     * A static method fetching the HTML 3.2 DTD
     * @return
     */
    public static synchronized DTD getDefaultDTD() {
        DTD tdtd = null;
        String name = "html32";
        try {
                tdtd = DTD.getDTD(name);
        } catch (IOException e) {
                
        }
        
        InputStream in = null;
        try {
            String path = name + ".bdtd";
            in = getResourceAsStream(path);
            if (in != null) {
                tdtd.read(new DataInputStream(new BufferedInputStream(in)));
                tdtd.putDTDHash(name, tdtd);
            }
        } catch (Exception e) {
            System.out.println("No such a DTD!");
        }
        return tdtd;
    }
    
    /**
     * Fetch a resource relative to the OpenParserDelegator class file.
     *
     * @param name the name of the resource, relative to the
     * OpenParserDelegator class.
     * @returns a stream representing the resource
     */
    public static InputStream getResourceAsStream(final String name) {
        return AccessController.doPrivileged((PrivilegedAction<InputStream>) () -> 
                OpenParserDelegator.class.getResourceAsStream(name));
    }
    
    @Override
    public void parse(Reader r, HTMLEditorKit.ParserCallback cb, boolean ignoreCharSet) throws IOException {
        new DocumentParser(dtd).parse(r, cb, ignoreCharSet);
    }
    
    /**
     * Returning current DTD
     * @return
     */
    public DTD getDTD () {
        return dtd;
    }
}
