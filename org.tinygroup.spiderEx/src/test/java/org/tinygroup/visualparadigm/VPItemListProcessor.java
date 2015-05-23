package org.tinygroup.visualparadigm;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.tinyspider.Processor;
import org.tinygroup.tinyspider.Spider;
import org.tinygroup.tinyspider.Watcher;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.WatcherImpl;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 内容列表处理器
 * Created by Hulk on 2014/10/31.
 */
public class VPItemListProcessor implements Processor {

    private String path;

    public VPItemListProcessor(String path){
        this.path = path;
    }

    public void process(String url, HtmlNode node, Map<String, Object> parameters) throws Exception {
        FastNameFilter<HtmlNode> filter = new FastNameFilter<HtmlNode>(node);
        filter.setNodeName("a");
        filter.setIncludeAttribute("class","tutorial-link");
//        filter.setIncludeNode("a");
        List<HtmlNode> aList = filter.findNodeList();
        for (HtmlNode a : aList) {
            String pageTitle = a.getContent();
            System.out.println("\t文章名称："+pageTitle);
            String href = VPSpider.host + a.getAttribute("href");
            System.out.println("\t文章url：" + href);
            getPageDetail(href,pageTitle);
        }

    }

    private void getPageDetail(String url, String pageTitle) throws Exception {
        System.out.println("\t\t处理文章start-->"+pageTitle);
        pageTitle = VPSpider.processorFileName(pageTitle);
        // 给一篇文章创建一个目录
        String curPath = path + File.separator + pageTitle;
        File file = new File(curPath);
        if (!file.exists()){
            System.out.println("创建目录："+curPath);
            file.mkdirs();
        }

        Watcher watcher = new WatcherImpl();
        Spider spider = new SpiderImpl();
        watcher.addProcessor(new VPPageDetailProcessor(pageTitle,curPath));

        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();
        nodeFilter.setNodeName("html");
        watcher.setNodeFilter(nodeFilter);

        spider.addWatcher(watcher);
        spider.processUrl(url);

        System.out.println("\t\t处理文章end<--"+pageTitle);
    }
}
