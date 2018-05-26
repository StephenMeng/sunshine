package team.stephen.sunshine.dao.sunshine.common;


import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.common.Attachment;
import team.stephen.sunshine.util.common.BaseDao;

/**
 * @author stephen
 * @date 2017/7/15
 */
@Mapper
public interface AttachmentDao extends BaseDao<Attachment> {
}
