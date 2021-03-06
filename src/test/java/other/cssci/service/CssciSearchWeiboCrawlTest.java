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
import team.stephen.sunshine.model.other.bean.Pagination;
import team.stephen.sunshine.model.other.bean.cssci.*;
import team.stephen.sunshine.service.other.parse.impl.cssci.*;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author Stephen
 * @date 2019/04/06 12:18
 */
public class CssciSearchWeiboCrawlTest {
    private CssciCrawlResource resource = mock(CssciCrawlResource.class);
    private String cookie = "PHPSESSID=9dbl0krvk513pkfebj1bdhdf82; _ga=GA1.3.60051673.1555159461; _gid=GA1.3.194878920.1555159461; search_history_ly_a=title%3D%25E6%25B3%2595%25E5%25AD%25A6+++8%26start_year%3D1998%26end_year%3D2018%26nian%3D%26juan%3D%26qi%3D%26xw1%3D%26xw2%3D%26wzlx%3D%26xkfl1%3D%26jj%3D%26pagenum%3D20%26order_type%3Dnian%26order_px%3DDESC";
    private CssciPaperParam param;
    private String qkname = "法学";
    private static final String ENCODE = "utf-8";
    private static String searchResultHtml;
    private static String searchDetailHtml;
    private static String sno;
    private CssciPaperDetailParam detailParam;

    @BeforeClass
    public static void initHtml() {
        try {
            sno = "11G0452017010001";
            InputStream in = Resources.getResource("./data/cssci.html").openStream();
            searchResultHtml = IOUtils.toString(in, ENCODE);
            in = Resources.getResource("./data/cssci.detail.html").openStream();
            searchDetailHtml = IOUtils.toString(in, ENCODE);
            in.close();
        } catch (IOException e) {
            LogRecord.error(e);
        }
    }

    @Before
    public void init() throws IOException {
        doReturn(cookie).when(resource).getCookie();
        param = new CssciPaperParam(resource);

        InputStream in = Resources.getResource("./data/cssci.html").openStream();
        searchResultHtml = IOUtils.toString(in, ENCODE);

        detailParam = new CssciPaperDetailParam(resource);
        detailParam.setSno(sno);
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
        qkname = "社会科学";
//        param.setQkname(qkname);
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
            Pagination total = (Pagination) new CssciPageParser().parse(html).get(0);
            LogRecord.print(papers);
            LogRecord.print(total.getTotal());
        } catch (IOException e) {
            LogRecord.error(e);
        }
        assert param.getUrl().length() > 20;
    }

    private Map<String, String> normalHeaders(CssciPaperParam param) {
        String url = param.getUrl();
        param.getHeaders().put("Referer", url.replace(CssciPaperParam.PREFIX, CssciPaperParam.REFERER_PREFIX).replace("&title", "title"));
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
            writer = new FileWriter((new File(saveFile)), true);
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

    @Test
    public void testCrawlDetailParam() {
        LogRecord.print(detailParam.getUrl());
        assert detailParam.getSno().equals(sno);
        assert detailParam.getUrl().length() > 10;
    }

    @Test
    public void testCrawlDetail() {

        String html = searchDetailHtml;
        CssciArticleDetailParser detailParser = new CssciArticleDetailParser();
        CssciArticleAuthorParser authorParser = new CssciArticleAuthorParser();
        CssciArticleCitationParser citationParser = new CssciArticleCitationParser();

        List<CssciPaper> papers = detailParser.parse(html);
        List<CssciAuthor> authors = authorParser.parse(html);
        List<CssciPaperAuthorRel> paperAuthorRelList = authors.stream().map(au -> this.genReal(au, sno)).collect(Collectors.toList());
        List<CssciCitation> cssciCitations = citationParser.parse(html);
        LogRecord.print(papers);
        LogRecord.print(authors);
        LogRecord.print(cssciCitations);
        LogRecord.print(paperAuthorRelList);

        assert detailParam.getSno().equals(sno);
        assert detailParam.getUrl().length() > 10;
    }

    private CssciPaperAuthorRel genReal(CssciAuthor au, String sno) {
        CssciPaperAuthorRel rel = new CssciPaperAuthorRel();
        rel.setAuthorId(au.getId());
        rel.setSno(sno);
        return rel;
    }

    @Test
    public void testParseUrl() {
        String url = "http://cssci.nju.edu.cn/control/controllers.php?control=search_base&action=search_lysy&title=%25E7%25AE%25A1%25E7%2590%2586%25E7%25A7%2591%25E5%25AD%25A6%252B%252B%252B8&start_year=2008&end_year=2009&order_type=nian&order_px=DESC&pagenow=1&pagesize=50&session_key=479&search_tag=1&rand=0.6675326233034267&nian=&juan=&qi=&xw1=&xw2=&xkfl1=&wzlx=";
        CssciPaperParam paperParam = new CssciPaperParam(resource);
        paperParam.parseUrl(url);
        LogRecord.print(paperParam);
        assert paperParam.getTitle().equals("管理科学");
        assert paperParam.getQkname().equals("管理科学");
        assert paperParam.getStartYear().equals("2008");
        assert paperParam.getEndYear().equals("2009");
        assert paperParam.getPagenow() == 1;
        assert paperParam.getPageSize() == 50;
        LogRecord.print(paperParam.getUrl());
        String preUrl = normalizePreUrl(paperParam.getUrl());
        LogRecord.print(preUrl);

    }

    private String normalizePreUrl(String url) {
        String prefUrl = url.replace(CssciPaperParam.PREFIX, CssciPaperParam.REFERER_PREFIX);
        prefUrl=prefUrl.replaceAll("qkname(.*?)&", "");
        prefUrl=prefUrl.replace("pagesize","pagenum");
        return URLDecoder.decode(prefUrl);
    }
}
