package team.stephen.sunshine.service.common.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.constant.enu.AttachmentEnum;
import team.stephen.sunshine.dao.sunshine.common.AttachmentDao;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.common.Attachment;
import team.stephen.sunshine.service.common.AttachmentService;
import team.stephen.sunshine.util.helper.FileHelper;

import java.util.List;

/**
 * 附件service类
 *
 * @author stephen
 * @date 2018/5/22
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;

    private int add(Attachment attachment) {
        return attachmentDao.insert(attachment);
    }

    @Override
    public int saveUserAttachment(Integer uploaderId, Integer ownerId, List<FileHelper> files) throws NullParamException {
        if (uploaderId == null || ownerId == null || files == null) {
            throw new NullParamException("参数不能为空");
        }
        addAttachments(uploaderId, ownerId, files, AttachmentEnum.USER);
        return 0;
    }

    @Override
    public int saveArticleAttachment(Integer uploaderId, Integer ownerId, List<FileHelper> files) throws NullParamException {
        if (uploaderId == null || ownerId == null || files == null) {
            throw new NullParamException("参数不能为空");
        }
        addAttachments(uploaderId, ownerId, files, AttachmentEnum.ARTICLE);
        return 0;
    }

    @Override
    public Page<Attachment> getUploaderAttachments(Integer uploaderId) throws NullParamException {
        if (uploaderId == null) {
            throw new NullParamException("参数不能为空");
        }
        Attachment condition = new Attachment();
        condition.setUploader(uploaderId);
        return (Page<Attachment>) attachmentDao.select(condition);
    }

    @Override
    public Page<Attachment> getOwnerAttachments(Integer ownerId) throws NullParamException {
        if (ownerId == null) {
            throw new NullParamException("参数不能为空");
        }
        Attachment condition = new Attachment();
        condition.setOwnerId(ownerId);
        condition.setOwnerType(AttachmentEnum.USER.getType());
        return (Page<Attachment>) attachmentDao.select(condition);
    }

    @Override
    public Page<Attachment> getArticleAttachments(Integer articleId) throws NullParamException {
        if (articleId == null) {
            throw new NullParamException("参数不能为空");
        }
        Attachment condition = new Attachment();
        condition.setOwnerId(articleId);
        condition.setOwnerType(AttachmentEnum.ARTICLE.getType());
        return (Page<Attachment>) attachmentDao.select(condition);
    }

    private void addAttachments(Integer uploaderId, Integer ownerId, List<FileHelper> files, AttachmentEnum type) {
        files.forEach(fh -> {
                    Attachment attachment = new Attachment();
                    attachment.setUploader(uploaderId);
                    attachment.setOwnerId(ownerId);
                    switch (type) {
                        case USER:
                            attachment.setOwnerType(AttachmentEnum.USER.getType());
                            break;
                        case ARTICLE:
                            attachment.setOwnerType(AttachmentEnum.ARTICLE.getType());
                        default:
                            break;
                    }
                    attachment.setAttLength(fh.getFileLength());
                    attachment.setAttType(fh.getFileType());
                    attachment.setAttName(fh.getFileName());
                    attachment.setAttUri(fh.getFilePath());
                    add(attachment);
                }
        );
    }


}
