package net.srt.system.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.srt.framework.mybatis.dao.BaseDao;
import net.srt.system.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @description: 系统用户DAO
* @author PatrickLi 373595331@qq.com
* @date 2024/5/23
*/
/*
* 使用 @Mapper 注解标识，表明它是一个 MyBatis 的 Mapper 接口。
* 这些方法会被 MyBatis 框架自动解析并映射成对应的 SQL 语句，实现与数据库的交互。
* 实现在resources的mapper文件夹里，当然也可以直接写在代码里
*
*/
@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {

	List<SysUserEntity> getList(Map<String, Object> params);

	SysUserEntity getById(@Param("id") Long id);

	List<SysUserEntity> getRoleUserList(Map<String, Object> params);

	default SysUserEntity getByUsername(String username){
		return this.selectOne(new QueryWrapper<SysUserEntity>().eq("username", username));
	}

	default SysUserEntity getByMobile(String mobile){
		return this.selectOne(new QueryWrapper<SysUserEntity>().eq("mobile", mobile));
	}

	List<SysUserEntity> getProjectList(Map<String, Object> params);

	void deleteProject(@Param("projectId") Long projectId, @Param("userId") Long userId);

	// 根据userid获取项目列表(项目status为enable)
	List<Long> getProjectIds(@Param("userId") Long userId);

	Long getByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
