package org.tinygroup.visualparadigm;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.tinyspider.Processor;
import org.tinygroup.tinyspider.Spider;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by Hulk on 2014/10/31.
 */
public class VPPageDetailProcessor implements Processor {

    private static HttpClient httpClient = new HttpClient();
    private String title;
    private String path;

    public VPPageDetailProcessor(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public void process(String url, HtmlNode node, Map<String, Object> parameters) throws Exception {
        FastNameFilter<HtmlNode> filter = new FastNameFilter(node);
        //抓取pdf
        filter.setNodeName("a");
        filter.setIncludeAttribute("class", "pdf notranslate");
        HtmlNode pdfNode = filter.findNode();
        String pdfUrl = pdfNode.getAttribute("href");
        saveUrl(title+".pdf",VPSpider.host +pdfUrl);

        //抓取其他资源文件
        filter.setNodeName("ol");
        filter.setIncludeAttribute("class", "contentPoint");
        HtmlNode olNode = filter.findNode();
        if (olNode != null){
            FastNameFilter<HtmlNode> liFilter = new FastNameFilter(olNode);
            liFilter.setNodeName("li");
            List<HtmlNode> liList = liFilter.findNodeList();
            for (HtmlNode li : liList) {
                HtmlNode a = li.getSubNode("a");
                if (a!=null){
                    String resourceName = a.getPureText();
                    String resourceUrl = a.getAttribute("href");
                    if (resourceUrl.contains(".vpp")||resourceUrl.contains(".bat")){
                        saveUrl(resourceName, resourceUrl);
                    }
                }
            }
        }

    }


    private void saveUrl(String name, String urlAddress) throws IOException {

        urlAddress = URLEncoder.encode(urlAddress,"UTF-8");
//        urlAddress = urlAddress.replace(" ","%20");
        name = VPSpider.processorFileName(name);
        long startTime = System.currentTimeMillis();
        String fileName = path + File.separator+ name.trim();
        System.out.println("begin save file:" + fileName);
        System.out.println("url:"+ urlAddress);
        GetMethod getMethod = new GetMethod(urlAddress);
        int iGetResultCode = httpClient.executeMethod(getMethod);
        if (iGetResultCode == HttpStatus.SC_OK) {
            InputStream inputStream = getMethod.getResponseBodyAsStream();
            OutputStream outputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[4096];
            int n = -1;
            while ((n = inputStream.read(buffer)) != -1) {
                if (n > 0) {
                    outputStream.write(buffer, 0, n);
                }
            }
            inputStream.close();
            outputStream.close();
        }
        getMethod.releaseConnection();
        long endTime = System.currentTimeMillis();
        System.out.println("end save file:" + fileName+"耗时："+ (endTime-startTime));
    }

}
