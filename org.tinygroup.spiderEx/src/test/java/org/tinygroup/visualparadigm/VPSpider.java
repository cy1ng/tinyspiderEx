package org.tinygroup.visualparadigm;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.tinyspider.Spider;
import org.tinygroup.tinyspider.Watcher;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.WatcherImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hulk on 2014/10/30.
 */
public class VPSpider {
    static String host = "http://www.visual-paradigm.com";
    public static void main(String[] args) throws Exception {
        Watcher watcher = new WatcherImpl();
        Spider spider = new SpiderImpl();
        watcher.addProcessor(new VPCategoryProcessor());
        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();

        nodeFilter.setNodeName("div");
        nodeFilter.setIncludeAttribute("class", "panes");

        watcher.setNodeFilter(nodeFilter);
        spider.addWatcher(watcher);
        spider.processUrl(host+"/tutorials/");

    }

    public static String processorFileName(String s){
        Pattern p=Pattern.compile("[\"\\?!:'<>]");//增加对应的标点
        Matcher m=p.matcher(s);
        return  m.replaceAll(""); //把英文标点符号替换成空，即去掉英文标点符号
    }
}
