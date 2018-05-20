package kafka;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.dto.user.UserDto;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.util.LogRecod;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CacheServiceTest {
    @Autowired
    private CacheService cacheService;

    @Test
    public void testUserId() {
        UserDto userDto = cacheService.findUserDtoByUserId(1);
        LogRecod.print(userDto.getUserName());
    }

    @Test
    public void testUserNo() {
        UserDto userDto = cacheService.findUserDtoByUserNo("MG1514017");
        LogRecod.print(userDto);
    }

    @Test
    public void testUserNull() {
        UserDto userDto = cacheService.findUserDtoByUserNo(null);
        LogRecod.print(userDto);
        userDto = cacheService.findUserDtoByUserId(null);
        LogRecod.print(userDto);
    }

    @Test
    public void testUpdateUser() {
        UserDto userDto = cacheService.findUserDtoByUserId(1);
        userDto.setEmail("1581284397@qq.com");
        cacheService.addOrUpdateUserCache(userDto);
        userDto = cacheService.findUserDtoByUserId(1);
        LogRecod.print(userDto.getEmail());
    }
}
