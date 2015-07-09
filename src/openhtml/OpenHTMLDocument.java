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
import java.util.List;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * A class extending HTMLEditorKit.Parser to create a new version of
 * ParserDelegator
 *
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
        return new OpenHTMLReader(pos);
    }

    /**
     * New implementation of HTMLReader with additional HTML 4.0 tags
     */
    public class OpenHTMLReader extends HTMLReader {

        /**
         * Single parametric constructor
         *
         * @param offset
         */
        public OpenHTMLReader(int offset) {
            super(offset);
        }

        /**
         * Two parametric constructor with additional functionality It registers
         * a set of new tags
         *
         * @param offset
         * @param newTags a List of additional tags
         */
        public OpenHTMLReader(int offset, List<HTML.Tag> newTags) {
            super(offset);
            /*
             * Registering additional tags
             */
            newTags.stream().forEach(tag -> registerTag(tag, new CharacterAction()));
        }

        @Override
        public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            CharacterAction ca = new CharacterAction();
            switch (t.toString()) {
                case "acronym":
                    registerTag(t, ca);
                    break;
                case "abbr":
                    registerTag(t, ca);
                    break;
                case "q":
                    registerTag(t, ca);
                    addContent(new char[] {34}, 0, 1);
                    break;
                case "button":
                    registerTag(t, new ButtonAction());
                    break;
            }
            super.handleStartTag(t, a, pos);
        }
        
        @Override
        public void handleEndTag(HTML.Tag t, int pos) {
            super.handleEndTag(t, pos);
            switch (t.toString()) {
                case "q":
                    addContent(new char[] {34}, 0, 1);
                    break;
            }
        }
        
        /**
         * New action for BUTTON HTML tags
         */
        public class ButtonAction extends BlockAction {
            ButtonAction() {
                super();
            }           
            /**
             * Start method
             * @param t
             * @param attr
             */
            @Override
            public void start(HTML.Tag t, MutableAttributeSet attr) {
                ButtonModel model = new DefaultButtonModel();
                model.setArmed(true);
                attr.addAttribute(StyleConstants.ModelAttribute, model);
                super.start(t, attr);
            }           
            /**
             * End method
             * @param t
             */
            @Override
            public void end(HTML.Tag t) {
                super.end(t);
            }
        }
    }
}
