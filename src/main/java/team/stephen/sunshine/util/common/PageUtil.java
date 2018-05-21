package team.stephen.sunshine.util.common;

import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageUtil {
    public static <T> PageInfo<T> listToPageInfo(List<T> papers, Long total, Integer pageNo, Integer pageSize) {
        PageInfo<T> pageInfo = new PageInfo<T>(papers);
        initPageInfo(pageInfo, total, pageNo, pageSize);
        return pageInfo;
    }

    private static <T> void initPageInfo(PageInfo<T> pageInfo, Long total, Integer pageNo, Integer pageSize) {
        int pages = (int) (total / pageSize);
        if (total % pageSize != 0) {
            pages++;
        }
        pageInfo.setPages(pages);
        pageInfo.setHasNextPage(pageNo < pages);
        pageInfo.setHasPreviousPage(pageNo > 1);
        pageInfo.setIsFirstPage(pageNo == 1);
        pageInfo.setIsLastPage(pageNo == pages);
        pageInfo.setNextPage(pageInfo.isIsLastPage() ? pageNo : pageNo + 1);
        pageInfo.setPrePage(pageInfo.isIsFirstPage() ? 1 : pageNo - 1);
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setSize(pageInfo.getList().size());
        pageInfo.setFirstPage(1);
        pageInfo.setLastPage(pages);

    }
}
