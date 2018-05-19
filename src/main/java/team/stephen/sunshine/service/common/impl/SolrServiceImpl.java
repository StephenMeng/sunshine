package team.stephen.sunshine.service.common.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.util.LogRecod;

import java.io.IOException;
import java.util.List;

@Service
public class SolrServiceImpl implements SolrService {
    @Autowired
    private HttpSolrClient solrClient;

    private static final String ARTICLE_ID = "article_id";
    private static final String ARTICLE_CONTENT = "article_content";
    private static final String ARTICLE_TAG = "article_tag";

    /**
     * 往索引库添加文档
     *
     * @throws IOException
     * @throws SolrServerException
     */
    public void addDoc(Long articleId, String articleContent, String atricleTag) {
        //构造一篇文档
        SolrInputDocument document = new SolrInputDocument();
        //往doc中添加字段,在客户端这边添加的字段必须在服务端中有过定义
        document.addField(ARTICLE_ID, articleId);
        document.addField(ARTICLE_CONTENT, articleContent);
        document.addField(ARTICLE_TAG, atricleTag);
        //获得一个solr服务端的请求，去提交  ,选择具体的某一个solr core
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据id从索引库删除文档
     */
    public void deleteDocumentById(Long id) throws Exception {
        //删除文档
        solrClient.deleteByQuery("article_id:" + id);
        //删除所有的索引
        solrClient.commit();
    }

    /**
     * 查询
     *
     * @throws Exception
     */
    public void querySolr(String qString) {
        SolrQuery query = new SolrQuery();
        //下面设置solr查询参数
        //query.set("q", "*:*");// 参数q  查询所有
        query.set("q", qString);//相关查询，比如某条数据某个字段含有周、星、驰三个字  将会查询出来 ，这个作用适用于联想查询

        //参数fq, 给query增加过滤查询条件
        query.addFilterQuery("id:[0 TO 9]");//id为0-4

        //给query增加布尔过滤条件
        //query.addFilterQuery("description:演员");  //description字段中含有“演员”两字的数据

        //参数df,给query设置默认搜索域
        query.set("df", "name");

        //参数sort,设置返回结果的排序规则
        query.setSort("id", SolrQuery.ORDER.desc);

        //设置分页参数
        query.setStart(0);
        query.setRows(10);//每一页多少值

        //参数hl,设置高亮
        query.setHighlight(true);
        //设置高亮的字段
        query.addHighlightField("name");
        //设置高亮的样式
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");

        //获取查询结果
        QueryResponse response = null;
        try {
            response = solrClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //两种结果获取：得到文档集合或者实体对象

        //查询得到文档的集合
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println("通过文档集合获取查询的结果");
        System.out.println("查询结果的总数量：" + solrDocumentList.getNumFound());
        //遍历列表
        for (SolrDocument doc : solrDocumentList) {
            System.out.println("id:" + doc.get("id") + "   name:" + doc.get("name") + "    description:" + doc.get("description"));
        }

        //得到实体对象
        List<String> tmpLists = response.getBeans(String.class);
        if (tmpLists != null && tmpLists.size() > 0) {
            LogRecod.info(tmpLists);
        }
    }
}
