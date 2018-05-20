package team.stephen.sunshine.service.common.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dto.book.BookDto;
import team.stephen.sunshine.dto.user.UserDto;
import team.stephen.sunshine.model.book.Book;
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

    @Override
    public Book bookDtoToModel(BookDto bookDto) {
        Book book = new Book();
        copyProperties(book, bookDto);
        if (bookDto.getTags() != null) {
            try {
                JSONArray jsonArray = JSONObject.parseArray(bookDto.getTags());
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
}
