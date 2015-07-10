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

import javax.swing.text.Document;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.html.parser.DTD;

/**
 * A class extending HTMLEditorKit to create a new version of
 * HTMLEditorKit with support for HTML 4.0
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
 public class OpenHTMLEditorKit extends HTMLEditorKit {
    protected final ViewFactory factory;
    protected final DTD dtd;
            
    /**
     * Constructor creating HTML 4.0 DTD and a view factory
     */
    public OpenHTMLEditorKit () {
        factory = new OpenHTMLFactory();
        dtd = OpenParserDelegator.getHTML40DTD();
    }
    @Override
    protected HTMLEditorKit.Parser getParser() {
                return new OpenParserDelegator(dtd);
    }           
    @Override
    public Document createDefaultDocument() {
        StyleSheet styles = getStyleSheet();
        StyleSheet ss = new StyleSheet();
        ss.addStyleSheet(styles);
        HTMLDocument doc = new OpenHTMLDocument(ss);
        doc.setParser(getParser());
        doc.setAsynchronousLoadPriority(4);
        doc.setTokenThreshold(100);
        return doc;
    }          
    @Override
    public ViewFactory getViewFactory () {
        return factory;
    }
}
