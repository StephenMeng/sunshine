package team.stephen.sunshine.service.common;

import com.github.pagehelper.Page;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.common.Attachment;
import team.stephen.sunshine.util.helper.FileHelper;

import java.util.List;

/**
 * @author stephen
 * @date 2018/5/22
 */
public interface AttachmentService {
    int saveUserAttachment(Integer uploaderId, Integer ownerId, List<FileHelper> files) throws NullParamException;

    int saveArticleAttachment(Integer uploaderId, Integer ownerId, List<FileHelper> files) throws NullParamException;

    Page<Attachment> getUploaderAttachments(Integer uploaderId) throws NullParamException;

    Page<Attachment> getOwnerAttachments(Integer ownerId) throws NullParamException;

    Page<Attachment> getArticleAttachments(Integer articleId) throws NullParamException;

}
