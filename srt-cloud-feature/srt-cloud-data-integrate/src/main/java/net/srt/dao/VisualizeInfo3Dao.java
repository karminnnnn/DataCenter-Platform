package net.srt.dao;

import net.srt.entity.VisualizeInfo3Entity;
import net.srt.entity.VisualizeInfoEntity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VisualizeInfo3Dao extends BaseDao<VisualizeInfo3Entity>{
    @Select("SELECT * FROM visualize_info3")
    List<VisualizeInfo3Entity> selectAll();
}
