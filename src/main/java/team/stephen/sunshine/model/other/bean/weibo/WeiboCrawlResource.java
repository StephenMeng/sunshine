package team.stephen.sunshine.model.other.bean.weibo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import team.stephen.sunshine.model.other.bean.BaseCrawlResource;

/**
 * @author Stephen
 * @date 2019/04/06 11:40
 */
@Component
@PropertySource("crawler-config/crawl.properties")
@ConfigurationProperties(prefix = "weibo")
public class WeiboCrawlResource extends BaseCrawlResource {
}
