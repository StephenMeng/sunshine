package team.stephen.sunshine.service.common.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dto.user.UserDto;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.DtoTransformService;

import java.lang.reflect.InvocationTargetException;

@Service
public class DtoTranformServiceImplService implements DtoTransformService {

    @Override
    public User userDtoToModel(UserDto userDto) {
        User user = new User();
        copyProperties(user, userDto);
        return user;
    }

    private void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDto userModelToDto(User user) {
        UserDto dto = new UserDto();
        copyProperties(dto, user);
        return dto;
    }
}
