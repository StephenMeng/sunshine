package team.stephen.sunshine.service.common;

import java.util.List;

public interface PermissionService {
    List<String> getUserRoles(Integer userId);

    List<String> getUserRoles(String userNo);
}
