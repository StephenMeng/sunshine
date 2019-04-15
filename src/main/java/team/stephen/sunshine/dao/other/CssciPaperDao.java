package team.stephen.sunshine.dao.other;

import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.other.bean.cssci.CssciPaper;
import team.stephen.sunshine.util.common.BaseDao;

import java.util.List;

/**
 * @author stephen
 * @date 2018/5/26
 */
@Mapper
public interface CssciPaperDao extends BaseDao<CssciPaper> {
    List<String> selectToCrawl();
}
