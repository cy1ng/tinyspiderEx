package org.tinygroup.tinyspider.vp;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.tinyspider.Processor;

import java.util.Map;

/**
 * Created by luoguo on 2014/10/29.
 */
public class VisualParadigmListProcessor implements Processor {
    public void process(String url, HtmlNode node, Map<String, Object> parameters) throws Exception {
        HtmlNode a = node.getSubNode("a");
        VisualParadigmPage.process(a.getPureText(), a.getAttribute("href"));
    }
}
