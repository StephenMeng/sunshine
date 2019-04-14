package other.cssci.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import team.stephen.sunshine.model.other.bean.cssci.CssciPaper;
import team.stephen.sunshine.model.other.bean.cssci.CssciArticleParam;
import team.stephen.sunshine.model.other.bean.cssci.CssciCrawlResource;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciPageParser;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleOverViewParser;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author Stephen
 * @date 2019/04/06 12:18
 */
public class CssciSearchWeiboCrawlTest {
    private CssciCrawlResource resource = mock(CssciCrawlResource.class);
    private String cookie = "PHPSESSID=9dbl0krvk513pkfebj1bdhdf82; _ga=GA1.3.60051673.1555159461; _gid=GA1.3.194878920.1555159461; search_history_ly_a=title%3D%25E6%25B3%2595%25E5%25AD%25A6+++8%26start_year%3D1998%26end_year%3D2018%26nian%3D%26juan%3D%26qi%3D%26xw1%3D%26xw2%3D%26wzlx%3D%26xkfl1%3D%26jj%3D%26pagenum%3D20%26order_type%3Dnian%26order_px%3DDESC";
    private CssciArticleParam param;
    private String qkname = "法学";
    private static final String ENCODE = "utf-8";
    private static String searchResultHtml;

    @BeforeClass
    public static void initHtml() {
        try {
            InputStream in = Resources.getResource("./data/cssci.html").openStream();
            searchResultHtml = IOUtils.toString(in, ENCODE);
        } catch (IOException e) {
            LogRecord.error(e);
        }
    }

    @Before
    public void init() throws IOException {
        doReturn(cookie).when(resource).getCookie();
        param = new CssciArticleParam(resource);

        InputStream in = Resources.getResource("./data/cssci.html").openStream();
        searchResultHtml = IOUtils.toString(in, ENCODE);
    }

    @Test
    public void testCrawlSimpleHtml() {
        param.setQkname(urlEncode("法学"));
        param.setStartYear("1998");
        param.setEndYear("2018");
        param.setPagenow(20);
        LogRecord.print(param.getUrl());
        assert param.getUrl().length() > 20;
    }

    public String urlEncode(String input) {
        String result = null;
        try {
            result = URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Test
    public void testCrawl() {
        param.setQkname((qkname));
        param.setPagenow(1);
        param.setStartYear("1998");
        param.setEndYear("1999");
        param.setPageSize(20);
        LogRecord.print(param.getUrl());
        try {
            String html = HttpUtils.okrHttpGet(param.getUrl(), param.getHeaders());
            LogRecord.print(StringUtils.decodeUnicode(html));
        } catch (IOException e) {
            LogRecord.error(e);
        }
        assert param.getUrl().length() > 20;
    }

    @Test
    public void testCompleteCrawlTask() {
        qkname="社会科学";
        param.setQkname(qkname);
        param.setTitle(qkname);
        param.setPagenow(1);
        param.setStartYear("2017");
        param.setEndYear("2017");
        param.setPageSize(50);
        LogRecord.print(param.getUrl());
        try {
            String html = IOUtils.toString(HttpUtils.httpGet(param.getUrl(), normalHeaders(param)).getEntity().getContent());
            CssciArticleOverViewParser parser = new CssciArticleOverViewParser();
            List<CssciPaper> papers = parser.parse(html);
            Integer total = (Integer) new CssciPageParser().parse(html).get(0);
            LogRecord.print(papers);
            LogRecord.print(total);
        } catch (IOException e) {
            LogRecord.error(e);
        }
        assert param.getUrl().length() > 20;
    }
    private Map<String, String> normalHeaders(CssciArticleParam param) {
        String url = param.getUrl();
        param.getHeaders().put("Referer", url.replace(CssciArticleParam.PREFIX, CssciArticleParam.REFERER_PREFIX).replace("&title","title"));
        LogRecord.print(param.getHeaders().get("Referer"));
        return param.getHeaders();
    }
    @Test
    public void testParse() {
        LogRecord.print(searchResultHtml);
        LogRecord.print(searchResultHtml);
        JSONObject jsonObject = JSONObject.parseObject(searchResultHtml);
        JSONArray jsonArray = jsonObject.getJSONArray("contents");
        if (jsonArray != null) {
            List<CssciPaper> journalList = jsonArray.toJavaList(CssciPaper.class);
            journalList.forEach(this::normalize);
            journalList.forEach(LogRecord::print);
        }
        assert searchResultHtml.length() > 20;
    }

    private void normalize(CssciPaper cssciPaper) {
        cssciPaper.setAuthors(normalizeItem(cssciPaper.getAuthors()));
        cssciPaper.setByc(normalizeItem(cssciPaper.getByc()));
    }

    private String normalizeItem(String item) {
        String res = item.replaceAll("aaa", ";").replaceAll(";;;", ";").replaceAll(";;", ";");
        if (res.startsWith(";")) {
            return res.substring(1);
        }
        return res;
    }

    @Test
    public void testSplitPage() {
        LogRecord.print(searchResultHtml);
        int start = 1999;
        int end = 2009;
        if (end > start) {
            JSONObject jsonObject = JSONObject.parseObject(searchResultHtml);
            String totalStr = jsonObject.getString("totalfound");
            Integer total = Integer.parseInt(totalStr);
            LogRecord.print(total);
            assert total.equals("429");
            if (total < 1000) {

            } else {
                int mid = (start + end) >> 1;
                LogRecord.print(mid);
            }
        } else {

        }
    }

    @Test
    public void testHandleFile() {
        String filepath = "C:\\USERS\\STEPHEN\\Desktop\\sunshine\\cssci\\journal.txt";
        try {
            List<String> lines = Files.readLines(new File(filepath), Charsets.UTF_8);
            lines.forEach(this::parseLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        String[] tmp = line.split("\t");
        String journal = tmp[0];

        int startYear = getYear(tmp[1], true);
        int endYear = getYear(tmp[1], false);
        LogRecord.print(journal + "\t" + startYear + "\t" + endYear);
        String dir = "C:\\USERS\\STEPHEN\\Desktop\\sunshine\\cssci\\";

        String saveFile = dir + "crawled.txt";
        FileWriter writer = null;
        try {
            writer = new FileWriter((new File(saveFile)),true);
            writer.append(journal + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getYear(String s, boolean startYear) {
        if (s.contains("-")) {
            if (startYear) {
                return Integer.parseInt(s.substring(0, s.indexOf("-")));
            } else {
                return Integer.parseInt(s.substring(s.indexOf("-") + 1));
            }
        }
        return Integer.parseInt(s);
    }

}
