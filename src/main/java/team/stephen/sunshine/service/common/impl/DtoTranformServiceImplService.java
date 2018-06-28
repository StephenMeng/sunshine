package team.stephen.sunshine.service.common.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.article.StandardArticleDto;
import team.stephen.sunshine.web.dto.book.BookDto;
import team.stephen.sunshine.web.dto.front.FrontChannelDto;
import team.stephen.sunshine.web.dto.front.FrontColumnDto;
import team.stephen.sunshine.web.dto.front.StandardChannelDto;
import team.stephen.sunshine.web.dto.front.StandardColumnDto;
import team.stephen.sunshine.web.dto.user.UserDto;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.DtoTransformService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class DtoTranformServiceImplService implements DtoTransformService {


    @Override
    public User userDtoToModel(UserDto orig) {
        if (orig == null) {
            return null;
        }
        User user = new User();
        copyProperties(user, orig);
        return user;
    }


    @Override
    public UserDto userModelToDto(User orig) {
        if (orig == null) {
            return null;
        }
        UserDto dto = new UserDto();
        copyProperties(dto, orig);
        return dto;
    }

    @Override
    public Book bookDtoToModel(BookDto orig) {
        if (orig == null) {
            return null;
        }
        Book book = new Book();
        copyProperties(book, orig);
        if (orig.getTags() != null) {
            try {
                JSONArray jsonArray = JSONObject.parseArray(orig.getTags());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    sb.append(jsonObject.getString("name")).append(";");
                }
                book.setTags(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return book;
    }

    @Override
    public Channel channelDtoToModel(FrontChannelDto orig) {
        if (orig == null) {
            return null;
        }
        Channel channel = new Channel();
        copyProperties(channel, orig);
        return channel;
    }

    @Override
    public StandardChannelDto channelModelToDto(Channel orig) {
        if (orig == null) {
            return null;
        }
        StandardChannelDto dto = new StandardChannelDto();
        copyProperties(dto, orig);
        return dto;
    }

    @Override
    public Column columnDtoToModel(FrontColumnDto orig) {
        if (orig == null) {
            return null;
        }
        Column column = new Column();
        copyProperties(column, orig);
        return column;
    }

    @Override
    public StandardColumnDto columnModelToDto(Column orig) {
        if (orig == null) {
            return null;
        }
        StandardColumnDto dto = new StandardColumnDto();
        copyProperties(dto, orig);
        return dto;
    }

    @Override
    public StandardArticleDto articleModelToDto(Article orig) {
        if (orig == null) {
            return null;
        }
        StandardArticleDto dto = new StandardArticleDto();
        copyProperties(dto, orig);
        if (StringUtils.isNotNull(orig.getArticleTag())) {
            Iterable its = Splitter.on(team.stephen.sunshine.constant.Splitter.SEMICOLON).split(orig.getArticleTag());
            List<String> tags = Lists.newArrayList(its);
            dto.setTags(tags);
        }
        return dto;
    }

    @Override
    public void copyProperties(Object dest, Object orig) {
        if (orig == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
