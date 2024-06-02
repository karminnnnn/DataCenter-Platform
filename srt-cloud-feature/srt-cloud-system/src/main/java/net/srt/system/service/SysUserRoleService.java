package net.srt.system.service;

import net.srt.framework.mybatis.service.BaseService;
import net.srt.system.entity.SysUserRoleEntity;

import java.util.List;

/**
* @description: 用户角色
* @author PatrickLi 373595331@qq.com
* @date 2024/6/2
*/
public interface SysUserRoleService extends BaseService<SysUserRoleEntity> {

    /**
     * 保存或修改
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void saveOrUpdate(Long userId, Long roleId);

    /**
     * 分配角色给用户列表
     * @param roleId      角色ID
     * @param userIdList  用户ID列表
     */
    void saveUserList(Long roleId, List<Long> userIdList);

    /**
     * 根据角色id列表，删除用户角色关系
     * @param roleIdList 角色id
     */
    void deleteByRoleIdList(List<Long> roleIdList);

    /**
     * 根据用户id列表，删除用户角色关系
     * @param userIdList 用户id列表
     */
    void deleteByUserIdList(List<Long> userIdList);

    /**
     * 根据角色id、用户id列表，删除用户角色关系
     * @param roleId 角色id
     * @param userIdList 用户id列表
     */
    void deleteByUserIdList(Long roleId, List<Long> userIdList);

    /**
     * 角色ID
     *
     * @param userId 用户ID
     */
    Long getRoleId(Long userId);
}
