package tla.web.model.mappings;

import java.io.StringWriter;

import java.awt.geom.Rectangle2D;

import org.apache.commons.lang3.RegExUtils;
import org.qenherkhopeshef.graphics.svg.SVGGraphics2D;

import jsesh.mdcDisplayer.draw.MDCDrawingFacade;
import jsesh.utils.DoubleDimensions;
import lombok.extern.slf4j.Slf4j;

/**
 * Util
 */
@Slf4j
public class Util {

    public static final String SERIF_FONT_MARKUP_REGEX = "\\$([^$]+)\\$";
    public static final String SERIF_FONT_MARKUP_REPLACEMENT = "<span class=\"bbaw-libertine\">$1</span>";

    private static final String XML_HEAD = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>";

    /**
     * Tries to use JSesh in order to render an MdC hieroglyph encoding
     * into an SVG vector graphic.
     * @param mdc hieroglyph sequence in Manuel de Codage (MdC)
     * @return textual serialization of SVG vector graphic or null
     */
    public static String jseshRender(String mdc) {
        if (mdc != null) {
            MDCDrawingFacade facade = new MDCDrawingFacade();
            StringWriter writer = new StringWriter();
            try {
                Rectangle2D boundingBox = facade.getBounds(
                    mdc, 0, 0
                );
                facade.draw(
                    mdc,
                    new SVGGraphics2D(
                        writer,
                        new DoubleDimensions(
                            boundingBox.getWidth(),
                            boundingBox.getHeight()
                        )
                    ),
                    0, 0
                );
            } catch (Exception e) {
                log.warn(
                    "Jsesh could not render hieroglyph encoding '{}': {}",
                    mdc,
                    e.toString()
                );
            }
            String jsesh = writer.toString();
            if (jsesh.length() > XML_HEAD.length()) {
                jsesh = String.format(
                    "%s</svg>",
                    jsesh.substring(XML_HEAD.length())
                );
            }
            return jsesh;
        } else {
            return null;
        }
    }

    /**
     * Parses the input and replaces <code>$nfr$</code> markup
     * with HTML tags.
     */
    public static String escapeMarkup(String text) {
        String escaped = RegExUtils.replacePattern(
            text,
            SERIF_FONT_MARKUP_REGEX,
            SERIF_FONT_MARKUP_REPLACEMENT
        );
        if (escaped != null) {
            return escaped.replaceAll("\\n", "<br/>");
        }
        return escaped;
    }

}