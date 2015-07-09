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

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.HTMLFactory;

/**
 * A class extending HTMLfactory to create views of additional HTML 4.0 elements
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class OpenHTMLFactory extends HTMLFactory {

    /**
     * Constructor
     */
    public OpenHTMLFactory() {
        super();
    }

    @Override
    public View create(Element element) {
        //Getting tag type
        HTML.Tag kind = (HTML.Tag) (element.getAttributes().getAttribute(StyleConstants.NameAttribute));
        //If button, creating a JButton
        if (kind instanceof HTML.UnknownTag && element.getName().equals("button")) {
            return new ComponentView(element) {
                @Override
                protected Component createComponent() {
                    JButton button = new JButton ();
                    button.setModel((ButtonModel) element.getAttributes().getAttribute(StyleConstants.ModelAttribute));
                    button.setMinimumSize(new Dimension(200, 50));
                    try {
                        int start = getElement().getStartOffset();
                        int end = getElement().getEndOffset();
                        String text = getElement().getDocument().getText(start, end - start);
                        button.setText(text);
                    } catch (BadLocationException e) {
                    }
                    return button;
                }
            };
            //return new BoxView(element, View.Y_AXIS);
        }

        return super.create(element);
    }
}
