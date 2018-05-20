package team.stephen.sunshine.service.common;

import team.stephen.sunshine.dto.user.UserDto;
import team.stephen.sunshine.model.user.User;

public interface DtoTransformService {

    User userDtoToModel(UserDto userDto);

    UserDto userModelToDto(User user);
}
