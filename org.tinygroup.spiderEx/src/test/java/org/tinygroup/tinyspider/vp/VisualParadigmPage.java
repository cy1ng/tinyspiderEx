package org.tinygroup.tinyspider.vp;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.tinyspider.Spider;
import org.tinygroup.tinyspider.Watcher;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.WatcherImpl;

/**
 * Created by luoguo on 2014/10/29.
 */
public class VisualParadigmPage {
    public static void process(String title,String url) throws Exception {
        System.out.println(System.currentTimeMillis()+"-"+title+","+url);
        Spider spider = new SpiderImpl("UTF-8");
        Watcher watcher = new WatcherImpl();
        watcher.addProcessor(new VisualParadigmPageProcessor(title));
        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();
        nodeFilter.setNodeName("html");
        watcher.setNodeFilter(nodeFilter);
        spider.addWatcher(watcher);
        spider.processUrl("http://www.visual-paradigm.com" + url);
        System.out.println(System.currentTimeMillis()+"-"+url);
    }

}
