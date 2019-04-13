package team.stephen.sunshine.model.other.bean;

/**
 * @author Stephen
 * @date 2019/04/13 22:04
 */
public class Pagination {
    private Integer total;
    private Integer totalPage;
    private Integer pageIndex;
    private Integer pageSize;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
