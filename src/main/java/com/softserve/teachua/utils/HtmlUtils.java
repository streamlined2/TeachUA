package com.softserve.teachua.utils;

import com.softserve.teachua.exception.IncorrectInputException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * This class is used to validate html code using JSOUP library.
 * For documentation, go to jsoup.org.
 *
 * @author Roman Klymus
 */
public class HtmlUtils {
    private static final String FORBIDDEN_DESC_TAGS = "You have used forbidden tags or attributes. "
            + "Only allow the following: a, b, blockquote, br, caption, cite, code, col, colgroup, "
            + "dd, div, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, span, "
            + "strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul.";
    public static final Safelist DESC_SAFELIST = Safelist.relaxed()
            .addTags("s", "iframe")
            .addAttributes("span", "class", "style")
            .addAttributes("a", "href", "rel", "target")
            .addAttributes("div", "class")
            .addAttributes("p", "class")
            .addAttributes("li", "class")
            .addAttributes("img", "class")
            .addAttributes("iframe", "class", "allowfullscreen", "src", "frameborder");


    /**
     * This method is used to validate the description of challenges and tasks.
     * Use {@code addTags} and {@code addAttributes} on {@code Safelist} class
     * to add new tags and attributes to whitelist. And don't forget to update
     * error message.
     * Stops if desc is null.
     *
     * @param desc put html code to validate
     * @throws IncorrectInputException throws if the code has forbidden tags and attributes
     * @see Safelist
     */
    public static void validateDescription(String desc) {
        if (desc == null) {
            return;
        }
        System.out.println(desc);
        System.out.println(Jsoup.clean(desc, DESC_SAFELIST)); //Use this to debug which tags are not valid
        if (!Jsoup.isValid(desc, DESC_SAFELIST)) {
            throw new IncorrectInputException(FORBIDDEN_DESC_TAGS);
        }
    }
}
