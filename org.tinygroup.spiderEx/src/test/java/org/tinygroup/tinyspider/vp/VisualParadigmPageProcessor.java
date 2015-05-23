package org.tinygroup.tinyspider.vp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.tinyspider.Processor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by luoguo on 2014/10/29.
 */
public class VisualParadigmPageProcessor implements Processor {
    static HttpClient httpClient = new HttpClient();
    private final String title;

    public VisualParadigmPageProcessor(String title) {
        this.title = title;
    }

    public void process(String url, HtmlNode node, Map<String, Object> parameters) throws Exception {
        NameFilter<HtmlNode> titleUrlFilter = new NameFilter(node);
        titleUrlFilter.setNodeName("title");
        HtmlNode titleNode = titleUrlFilter.findNode();

        NameFilter<HtmlNode> pdfUrlFilter = new NameFilter(node);
        pdfUrlFilter.setNodeName("a");
        pdfUrlFilter.setIncludeAttribute("class", "pdf notranslate");
        HtmlNode pdfNode = pdfUrlFilter.findNode();
        NameFilter<HtmlNode> ol = new NameFilter(node);
        pdfUrlFilter.setNodeName("ol");
        pdfUrlFilter.setIncludeAttribute("class", "contentPoint");
        HtmlNode olNode = pdfUrlFilter.findNode();
        System.out.println();
        if (pdfNode != null) {
            String pdfUrl = "http://www.visual-paradigm.com" + pdfNode.getAttribute("href");
            saveUrl(titleNode.getPureText() + ".pdf", pdfUrl);
        }
        if (olNode != null && olNode.getSubNodes("a") != null) {
            for (HtmlNode aNode : olNode.getSubNodes("a")) {
                String vppUrl = "http://www.visual-paradigm.com" + aNode.getAttribute("href");
                saveUrl(aNode.getPureText(), vppUrl);
            }
        }
    }

    private void saveUrl(String name, String urlAddress) throws IOException {
        String fileName = "E:\\临时\\spider\\" + title + "\\" + name;
        System.out.println("begin save file:" + fileName);
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
        System.out.println("end save file:" + fileName);
    }
}