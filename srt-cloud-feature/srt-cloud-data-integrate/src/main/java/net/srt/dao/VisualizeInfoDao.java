package net.srt.dao;

import net.srt.entity.VisualizeInfoEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VisualizeInfoDao extends BaseDao<VisualizeInfoEntity> {
    @Select("SELECT * FROM visualize_info")
    List<VisualizeInfoEntity> selectAll();
}
