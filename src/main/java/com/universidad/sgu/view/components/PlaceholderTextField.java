package com.universidad.sgu.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * A JTextField that shows a placeholder text when empty.
 */
public class PlaceholderTextField extends JTextField {

    private String placeholder;

    public PlaceholderTextField() {
    }

    public PlaceholderTextField(final String pText) {
        super();
        this.placeholder = pText;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String s) {
        placeholder = s;
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                .getMaxAscent() + getInsets().top);
    }
}
