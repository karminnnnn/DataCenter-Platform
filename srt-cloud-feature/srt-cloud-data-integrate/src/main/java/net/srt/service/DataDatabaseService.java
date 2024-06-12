package net.srt.service;

import net.srt.entity.DataDatabaseEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataDatabaseQuery;
import net.srt.vo.DataDatabaseVO;

import java.util.List;
import java.util.Map;


public interface DataDatabaseService extends BaseService<DataDatabaseEntity> {
    PageResult<DataDatabaseVO> page(DataDatabaseQuery query);

    void delete(Long databaseId);

    void save(DataDatabaseVO vo);

    void update(DataDatabaseVO vo);

    DataDatabaseVO get(Long id);

    void testOnline(Integer databaseId,String databaseName);

    public List<Map<String, Object>> getDatabaseInfoByDataSourceIds(List<Long> dataSourceIds);

    public Integer getDatasourceIdByDatabaseId(Long databaseId);

    public String getDatabasenameByID(Long databaseId);



}
