package net.srt.system.dao;

import net.srt.framework.mybatis.dao.BaseDao;
import net.srt.system.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @description: 角色管理
* @author PatrickLi 373595331@qq.com
* @date 2024/5/23
*/
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    /**
     * 根据用户ID，获取用户最大的数据范围
     */
    Integer getDataScopeByUserId(@Param("userId") Long userId);

}
