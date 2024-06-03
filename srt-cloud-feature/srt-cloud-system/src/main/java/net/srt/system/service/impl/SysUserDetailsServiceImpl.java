package net.srt.system.service.impl;

import lombok.AllArgsConstructor;
import net.srt.framework.security.user.UserDetail;
import net.srt.system.convert.SysUserConvert;
import net.srt.system.dao.SysRoleDao;
import net.srt.system.dao.SysRoleDataScopeDao;
import net.srt.system.entity.SysUserEntity;
import net.srt.system.enums.DataScopeEnum;
import net.srt.system.enums.UserStatusEnum;
import net.srt.system.service.SysMenuService;
import net.srt.system.service.SysOrgService;
import net.srt.system.service.SysUserDetailsService;
import net.srt.system.service.SysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SysUserDetailsServiceImpl implements SysUserDetailsService {
    private final SysMenuService sysMenuService;
    private final SysOrgService sysOrgService;
    private final SysUserService sysUserService;
    private final SysRoleDao sysRoleDao;
    private final SysRoleDataScopeDao sysRoleDataScopeDao;

    @Override
    public UserDetails getUserDetails(SysUserEntity userEntity) {
        // 转换成UserDetail对象，一一对应
        UserDetail userDetail = SysUserConvert.INSTANCE.convertDetail(userEntity);

        // 设置账号是否可用
        if (userEntity.getStatus() == UserStatusEnum.DISABLE.getValue()) {
            userDetail.setEnabled(false);
        }

        // 数据权限范围
        List<Long> dataScopeList = getDataScope(userDetail);
        userDetail.setDataScopeList(dataScopeList);

        // 用户权限列表（menu）
        Set<String> authoritySet = sysMenuService.getUserAuthority(userDetail);
        userDetail.setAuthoritySet(authoritySet);

        // 获取user对应的项目列表
		List<Long> projectIds = sysUserService.getProjectIds(userDetail);
		userDetail.setProjectIds(projectIds);

		return userDetail;
    }

    /**
     * 根据用户详情获取数据权限范围。
     */
    private List<Long> getDataScope(UserDetail userDetail) {
        // 通过用户ID获取数据权限范围
        Integer dataScope = sysRoleDao.getDataScopeByUserId(userDetail.getId());
		userDetail.setDataScope(dataScope); // 设置用户的数据权限范围

        // 用户未关联角色或角色未设置dataScope
        if (dataScope == null) {
            return new ArrayList<>(); // 当数据权限为空时，返回空列表
        }

        // 处理不同的数据权限范围
        if (dataScope.equals(DataScopeEnum.ALL.getValue())) {
            // 全部数据权限，则返回null
            return null;
        }else if (dataScope.equals(DataScopeEnum.DEPT_ONLY.getValue())) {
            // 本部门数据
            List<Long> dataScopeList = new ArrayList<>();
            dataScopeList.add(userDetail.getOrgId()); // 添加本部门ID
            return dataScopeList;
        } else if (dataScope.equals(DataScopeEnum.CUSTOM.getValue())) {
            // 自定义数据权限范围
            return sysRoleDataScopeDao.getDataScopeList(userDetail.getId());
        }

        return new ArrayList<>(); // 异常，返回空列表
    }

}
