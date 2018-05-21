package team.stephen.sunshine.service.common;

import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.web.dto.book.BookDto;
import team.stephen.sunshine.web.dto.front.ChannelDto;
import team.stephen.sunshine.web.dto.front.ColumnDto;
import team.stephen.sunshine.web.dto.user.UserDto;

public interface DtoTransformService {

    User userDtoToModel(UserDto userDto);

    UserDto userModelToDto(User user);

    Book bookDtoToModel(BookDto bookDto);

    Channel channelDtoToModel(ChannelDto channelDto);

    ChannelDto channelModelToDto(Channel channel);

    Column columnDtoToModel(ColumnDto columnDto);

    ColumnDto columnModelToDto(Column column);


}
