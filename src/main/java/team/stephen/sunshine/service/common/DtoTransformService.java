package team.stephen.sunshine.service.common;

import team.stephen.sunshine.dto.book.BookDto;
import team.stephen.sunshine.dto.user.UserDto;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.model.user.User;

public interface DtoTransformService {

    User userDtoToModel(UserDto userDto);

    UserDto userModelToDto(User user);

    Book bookDtoToModel(BookDto bookDto);
}
