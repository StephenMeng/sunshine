package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.service.common.PermissionService;
import team.stephen.sunshine.util.common.LogRecod;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class PermissionServiceTest {
    @Autowired
    private PermissionService permissionService;

    @Test
    public void testRole() {
        List<String> userRoles = permissionService.getUserRoles(1);
        LogRecod.print(userRoles);
        userRoles = permissionService.getUserRoles("MG1514018");
        LogRecod.print(userRoles);
    }
}
