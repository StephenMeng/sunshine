package team.stephen.sunshine.service.common;

import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.web.dto.article.StandardArticleDto;
import team.stephen.sunshine.web.dto.book.BookDto;
import team.stephen.sunshine.web.dto.front.FrontChannelDto;
import team.stephen.sunshine.web.dto.front.FrontColumnDto;
import team.stephen.sunshine.web.dto.front.StandardChannelDto;
import team.stephen.sunshine.web.dto.front.StandardColumnDto;
import team.stephen.sunshine.web.dto.user.UserDto;

public interface DtoTransformService {

    User userDtoToModel(UserDto userDto);
//
    UserDto userModelToDto(User user);

    Book bookDtoToModel(BookDto bookDto);

    Channel channelDtoToModel(FrontChannelDto frontChannelDto);

    StandardChannelDto channelModelToDto(Channel channel);

    Column columnDtoToModel(FrontColumnDto frontColumnDto);

    StandardColumnDto columnModelToDto(Column column);

    StandardArticleDto articleModelToDto(Article article);

    void copyProperties(Object dest, Object source);
}
