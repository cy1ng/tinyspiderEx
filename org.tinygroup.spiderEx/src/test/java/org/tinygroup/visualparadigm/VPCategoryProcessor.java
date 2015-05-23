package org.tinygroup.visualparadigm;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.tinyspider.OSchinaSpider;
import org.tinygroup.tinyspider.Processor;
import org.tinygroup.tinyspider.Spider;
import org.tinygroup.tinyspider.Watcher;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.WatcherImpl;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 栏目处理器
 * Created by Hulk on 2014/10/30.
 */
public class VPCategoryProcessor implements Processor {

    static String path = "F:"+File.separator+"VPTutorials";

    public void process(String url, HtmlNode node, Map<String, Object> parameters) throws Exception {

        FastNameFilter<HtmlNode> filter = new FastNameFilter<HtmlNode>(node);

        filter.setNodeName("li");
        filter.setIncludeAttribute("class","tutorialLeftMenuItem");
        filter.setIncludeNode("a");
        List<HtmlNode> liList = filter.findNodeList();
        for (HtmlNode li : liList) {
            HtmlNode a = li.getSubNode("a");
            String categoryName = a.getContent();

            System.out.println("栏目名名称："+a.getContent());
            String href = a.getAttribute("href");
            System.out.println("栏目url："+href);
            getContentListSpider(VPSpider.host + href, categoryName);
        }

    }

    /**
     * 获取某一个栏目的内容列表
     */
    private void getContentListSpider(String url,String categoryName) throws Exception {
        System.out.println("进入栏目--》"+categoryName);
        Watcher watcher = new WatcherImpl();
        Spider spider = new SpiderImpl();
        watcher.addProcessor(new VPItemListProcessor(path + File.separator + categoryName));
        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();

        nodeFilter.setNodeName("ol");
        nodeFilter.setIncludeAttribute("class", "content ");

        watcher.setNodeFilter(nodeFilter);
        spider.addWatcher(watcher);
        spider.processUrl(url);

    }


    public static void main(String[] args) {
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
    }

}
