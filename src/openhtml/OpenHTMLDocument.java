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

import java.net.URL;
import java.util.Hashtable;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * A class extending HTMLEditorKit.Parser to create a new version of ParserDelegator
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class OpenHTMLDocument extends HTMLDocument {

    /**
     * Default constructor
     */
    public OpenHTMLDocument() {
        super();
    }

    /**
     * Constructor with Content and StyleSheet parameters
     *
     * @param c
     * @param styles
     */
    public OpenHTMLDocument(AbstractDocument.Content c, StyleSheet styles) {
        super(c, styles);
    }

    /**
     * Constructor with StyleSheet parameter
     *
     * @param styles
     */
    public OpenHTMLDocument(StyleSheet styles) {
        super(styles);
    }

    @Override
    public HTMLEditorKit.ParserCallback getReader(int pos) {
        Object desc = getProperty(Document.StreamDescriptionProperty);
        if (desc instanceof URL) {
            setBase((URL) desc);
        }
        OpenHTMLReader reader = new OpenHTMLReader(pos);
        return reader;
    }

    public class OpenHTMLReader extends HTMLReader {

        /**
         * Single parametric constructor
         * @param offset
         */
        public OpenHTMLReader(int offset) {
            super(offset);
        }
        
        /**
         * Four parametric constructor with additional functionality
         * @param offset
         * @param popDepth
         * @param pushDepth
         * @param insertTag
         */
        public OpenHTMLReader(int offset, int popDepth, int pushDepth,
                          HTML.Tag insertTag) {
            super(offset, popDepth, pushDepth, insertTag);
            
        }
        
    }
}
