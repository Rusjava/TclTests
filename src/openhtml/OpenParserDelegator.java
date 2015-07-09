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
import javax.swing.text.html.parser.ContentModel;
import javax.swing.text.html.parser.DocumentParser;
import javax.swing.text.html.parser.Element;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * A class extending HTMLEditorKit.Parser to create a new version of
 * ParserDelegator
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class OpenParserDelegator extends HTMLEditorKit.Parser {

    //Working DTD

    protected DTD dtd;

    /**
     * Constructor setting an arbitrary DTD
     *
     * @param dtd
     */
    public OpenParserDelegator(DTD dtd) {
        this.dtd = dtd;
    }

    /**
     * Constructor setting default HTML 3.2 DTD
     */
    public OpenParserDelegator() {
        this(getDefaultDTD());
    }

    /**
     * A static method fetching the HTML 3.2 DTD
     *
     * @return
     */
    public static synchronized DTD getDefaultDTD() {
        DTD tdtd;
        String name = "html32";
        InputStream in;
        try {
            tdtd = DTD.getDTD(name);
            String path = name + ".bdtd";
            in = getResourceAsStream(path);
            if (in != null) {
                tdtd.read(new DataInputStream(new BufferedInputStream(in)));
                DTD.putDTDHash(name, tdtd);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        return tdtd;
    }

    /**
     * A static method creating HTML 4.0 DTD
     *
     * @return
     */
    public static DTD getHTML40DTD() {
        //Fetching HTML 3.2 DTD
        DTD dtd4 = getDefaultDTD();
        //Adding HTML 4.0 tags
        insertInlineElement(dtd4, "acronym", "em");
        insertInlineElement(dtd4, "abbr", "acronym");
        insertInlineElement(dtd4, "q", "em");
        insertInlineElement(dtd4, "button", "div");
        //insertElementInContentModel(dtd4.getElement("button"), "div", dtd4);
        return dtd4;
    }

    /**
     * Fetch a resource relative to the OpenParserDelegator class file.
     *
     * @param name the name of * OpenParserDelegator class.
     * @return returning a stream representing the resource
     */
    protected static InputStream getResourceAsStream(final String name) {
        return AccessController.doPrivileged((PrivilegedAction<InputStream>) ()
                -> ParserDelegator.class.getResourceAsStream(name));
    }

    @Override
    public void parse(Reader r, HTMLEditorKit.ParserCallback cb, boolean ignoreCharSet) throws IOException {
        new DocumentParser(dtd).parse(r, cb, ignoreCharSet);
    }

    /**
     * Returning current DTD
     *
     * @return
     */
    public DTD getDTD() {
        return dtd;
    }

    /**
     * Inserts a new inline HTML element into a DTD and updates the Content Model
     *
     * @param inDtd model to insert to
     * @param elemName element name to insert
     * @param sampleElemName next element name
     */
    protected static void insertInlineElement(DTD inDtd, String elemName, String sampleElemName) {
        //Getting the next element
        Element sampleElem = inDtd.getElement(sampleElemName);
        //Adding tag to the DTD
        Element el = inDtd.defineElement(elemName, DTD.MODEL, false, false, sampleElem.getContent(), null, null, sampleElem.atts);
        //Updating content models of all parent elements
        insertElementInContentModel(el, "span", inDtd);
        insertElementInContentModel(el, "em", inDtd);
        insertElementInContentModel(el, "strong", inDtd);
        insertElementInContentModel(el, "dfn", inDtd);
        insertElementInContentModel(el, "code", inDtd);
        insertElementInContentModel(el, "samp", inDtd);
        insertElementInContentModel(el, "kbd", inDtd);
        insertElementInContentModel(el, "var", inDtd);
        insertElementInContentModel(el, "cite", inDtd);
        insertElementInContentModel(el, "tt", inDtd);
        insertElementInContentModel(el, "i", inDtd);
        insertElementInContentModel(el, "b", inDtd);
        insertElementInContentModel(el, "u", inDtd);
        insertElementInContentModel(el, "s", inDtd);
        insertElementInContentModel(el, "strike", inDtd);
        insertElementInContentModel(el, "big", inDtd);
        insertElementInContentModel(el, "small", inDtd);
        insertElementInContentModel(el, "sub", inDtd);
        insertElementInContentModel(el, "sup", inDtd);
        //insertElementInContentModel(el, "bdo", inDtd);
        insertElementInContentModel(el, "font", inDtd);
        insertElementInContentModel(el, "body", inDtd);
        insertElementInContentModel(el, "address", inDtd);
        insertElementInContentModel(el, "div", inDtd);
        insertElementInContentModel(el, "a", inDtd);
        insertElementInContentModel(el, "map", inDtd);
        insertElementInContentModel(el, "object", inDtd);
        insertElementInContentModel(el, "applet", inDtd);
        insertElementInContentModel(el, "p", inDtd);
        insertElementInContentModel(el, "h1", inDtd);
        insertElementInContentModel(el, "h2", inDtd);
        insertElementInContentModel(el, "h3", inDtd);
        insertElementInContentModel(el, "h4", inDtd);
        insertElementInContentModel(el, "h5", inDtd);
        insertElementInContentModel(el, "h6", inDtd);
        insertElementInContentModel(el, "pre", inDtd);
        insertElementInContentModel(el, "blockquote", inDtd);
        insertElementInContentModel(el, "dt", inDtd);
        insertElementInContentModel(el, "dd", inDtd);
        insertElementInContentModel(el, "li", inDtd);
        insertElementInContentModel(el, "form", inDtd);
        //insertElementInContentModel(el, "iframe", inDtd);
    }
    /**
     * Inserts an element into content model
     *
     * @param elem
     * @param parentElemName
     * @param td
     */
    protected static void insertElementInContentModel(Element elem, String parentElemName, DTD td) {
        ContentModel model = td.getElement(parentElemName).getContent();
        ((ContentModel) ((ContentModel) model.content).content).next
                = new ContentModel(0, elem, ((ContentModel) ((ContentModel) model.content).content).next);
    }
    
    /**
     * Inserts a new form HTML element into a DTD and updates the Content Model
     *
     * @param inDtd model to insert to
     * @param elemName element name to insert
     * @param sampleElemName next element name
     */
    protected static void insertFormElement(DTD inDtd, String elemName, String sampleElemName) {
        //Getting the next element
        Element sampleElem = inDtd.getElement(sampleElemName);
        //Adding tag to the DTD
        Element el = inDtd.defineElement(elemName, DTD.MODEL, false, false, sampleElem.getContent(), null, null, sampleElem.atts);
        //Updating content models of all parent elements
        insertElementInContentModel(el, "form", inDtd);
    }
}
