package team.stephen.sunshine.model.other.bean.weibo;

import io.swagger.models.auth.In;

import java.util.Date;

/**
 * @author Stephen
 * @date 2019/03/18 23:46
 */
public class WeiboSearchParamBuilder {
    private WeiboSearchParam item;

    public WeiboSearchParamBuilder(WeiboCrawlResource resource) {
        item = new WeiboSearchParam(resource);
    }

    public WeiboSearchParamBuilder setKeyword(String keyword) {
        item.keyword = keyword;
        return this;
    }

    public WeiboSearchParamBuilder setStartDate(String startDate) {
        item.startDate = startDate;
        return this;
    }


    public WeiboSearchParamBuilder setEndDate(String endDate) {
        item.endDate = endDate;
        return this;
    }


    public WeiboSearchParamBuilder setType(AdvanceType type) {
        item.type = type;
        return this;
    }

    public WeiboSearchParamBuilder setEndoce(String encode) {
        item.encode = encode;
        return this;
    }

    public WeiboSearchParamBuilder setPage(Integer page) {
        item.page = page;
        return this;
    }

    public WeiboSearchParam build() {
        return item.clone();
    }
}
