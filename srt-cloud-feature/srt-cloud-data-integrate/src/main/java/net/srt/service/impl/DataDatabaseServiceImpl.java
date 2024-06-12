package net.srt.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
// import net.srt.api.UserLogApiImpl;
import net.srt.api.module.system.constant.UserActionEnum;
import net.srt.constants.YesOrNo;
import net.srt.convert.DataDatabaseConvert;
import net.srt.convert.DataSourceConvert;
import net.srt.dao.DataAccessTaskDao;
import net.srt.dao.DataDatabaseDao;
import net.srt.dao.DataSourceDao;
import net.srt.entity.DataAccessTaskEntity;
import net.srt.entity.DataDatabaseEntity;
import net.srt.entity.DataSourceEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.cache.TokenStoreCache;
import net.srt.framework.security.user.UserDetail;
import net.srt.query.DataDatabaseQuery;
import net.srt.query.DataSourceQuery;
import net.srt.service.DataAccessTaskService;
import net.srt.service.DataDatabaseService;
import net.srt.vo.DataDatabaseVO;
import net.srt.vo.DataSourceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataDatabaseServiceImpl extends BaseServiceImpl<DataDatabaseDao, DataDatabaseEntity> implements DataDatabaseService {
    private final DataDatabaseDao dataDatabaseDao;
    private final DataSourceDao dataSourceDao;
    //private final UserLogApiImpl userLogApi;
    public PageResult<DataDatabaseVO> page(DataDatabaseQuery query,String accessToken) {
        IPage<DataDatabaseEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        //userLogApi.save(accessToken, UserActionEnum.SELECT.getValue());
        return new PageResult<>(DataDatabaseConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<DataDatabaseEntity> getWrapper(DataDatabaseQuery query) {
        LambdaQueryWrapper<DataDatabaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(query.getName()), DataDatabaseEntity::getDatabaseName, query.getName());
        wrapper.eq( query.getStatus() != null,DataDatabaseEntity::getStatus, query.getStatus());
        wrapper.eq( StrUtil.isNotBlank(query.getCreatorName()),DataDatabaseEntity::getCreator, query.getCreatorName());
        wrapper.eq( DataDatabaseEntity::getDatasourceId, query.getDatasourceId());
        //dataScopeWithOrgId(wrapper);  // 加限制条件，不能对权限范围之外的数据库进行操作
        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long databaseId) {
        /*
        目前还没有看数据接入的部分，按道理来说一个数据库是要和一个接入任务相关联的，删除的时候要判断是否有接入任务
         */
        removeById(databaseId);
        String databaseName = dataDatabaseDao.getDatabaseNameById(databaseId);
        Integer datasourceId = dataDatabaseDao.getDatasourceIdById(databaseId);
        dropDatabase(datasourceId,databaseName);
    }

    @Override
    public void save(DataDatabaseVO vo){
        /*
        获取到前端传来的数据直接插入，目前没有别的判断条件
         */
        DataDatabaseEntity entity = DataDatabaseConvert.INSTANCE.convert(vo);
        Integer datasourceId = vo.getDatasourceId();
        String databaseName = vo.getDatabaseName();
        baseMapper.insert(entity);
        createDatabase(datasourceId,databaseName);
    }

    @Override
    public void update(DataDatabaseVO vo){
        /*
        获取到前端传来的数据直接修改，目前没有别的判断条件
        updateById的条件是version和id要和数据库的数据一样，并且deleted为0
         */
        DataDatabaseEntity entity = DataDatabaseConvert.INSTANCE.convert(vo);
        updateById(entity);
    }

    @Override
    public DataDatabaseVO get(Long id){
        /*
        creatorName和datasourceName这两个字段是要从别的表拿的，并未保存在DataDatabaseEntity中
         */
        DataDatabaseEntity entity = getById(id);
        String creatorName = dataDatabaseDao.getUsernameById(entity.getCreator());
        String datasourceName = dataSourceDao.getNameById( entity.getDatasourceId());
        String updaterName = dataDatabaseDao.getUsernameById(entity.getUpdater());
        DataDatabaseVO vo = DataDatabaseConvert.INSTANCE.convert(entity);
        vo.setCreatorName(creatorName);
        vo.setDatasourceName(datasourceName);
        vo.setUpdaterName(updaterName);
        return vo;
    }


    @Override
    public void testOnline(Integer datasourceId,String databaseName){
        ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(1);  // 目前只用到MYSQL数据库
        IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
        DataSourceEntity dataSourceEntity = dataSourceDao.selectById(datasourceId);

        String jdbcUrl = productTypeEnum.getUrl();
        jdbcUrl = jdbcUrl.replace("{host}",dataSourceEntity.getDatabaseIp())
                .replace("{port}",dataSourceEntity.getDatabasePort())
                .replace("{database}",databaseName);

        metaDataService.testQuerySQL(
                jdbcUrl,
                dataSourceEntity.getUserName(),
                dataSourceEntity.getPassword(),
                productTypeEnum.getTestSql()
        );

        if(!databaseName.isEmpty()){
            dataDatabaseDao.changeStatusByName(databaseName,1); // 更新状态
        }


    }

    // 删除主机上的数据库
    public void dropDatabase(Integer datasourceId,String databaseName){
        ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(1);  // 目前只用到MYSQL数据库
        IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
        DataSourceEntity dataSourceEntity = dataSourceDao.selectById(datasourceId);

        String jdbcUrl = productTypeEnum.getUrl();
        jdbcUrl = jdbcUrl.replace("{host}",dataSourceEntity.getDatabaseIp())
                .replace("{port}",dataSourceEntity.getDatabasePort())
                .replace("{database}",databaseName);

        String dropStr = "drop database "+databaseName;
        metaDataService.sqlExecute(
                jdbcUrl,
                dataSourceEntity.getUserName(),
                dataSourceEntity.getPassword(),
                dropStr
        );

    }

    // 在主机上创建数据库
    public void createDatabase(Integer datasourceId,String databaseName){
        ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(1);  // 目前只用到MYSQL数据库
        IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
        DataSourceEntity dataSourceEntity = dataSourceDao.selectById(datasourceId);

        String jdbcUrl = productTypeEnum.getUrl();
        jdbcUrl = jdbcUrl.replace("{host}",dataSourceEntity.getDatabaseIp())
                .replace("{port}",dataSourceEntity.getDatabasePort())
                .replace("{database}","");

        String createStr = "create database "+databaseName;
        metaDataService.sqlExecute(
                jdbcUrl,
                dataSourceEntity.getUserName(),
                dataSourceEntity.getPassword(),
                createStr
        );

    }

    public List<Map<String, Object>> getDatabaseInfoByDataSourceIds(List<Long> dataSourceIds){
        if (dataSourceIds == null || dataSourceIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 根据 datasource_id 列表查询 data_database 表
        LambdaQueryWrapper<DataDatabaseEntity> dataDatabaseWrapper = Wrappers.lambdaQuery();
        dataDatabaseWrapper.in(DataDatabaseEntity::getDatasourceId, dataSourceIds);

        List<DataDatabaseEntity> dataDatabaseEntities = baseMapper.selectList(dataDatabaseWrapper);


        // 转换为 Map<String, Object> 对象
        return dataDatabaseEntities.stream()
                .map(entity -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", entity.getId());
                    map.put("name", entity.getDatabaseName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Integer getDatasourceIdByDatabaseId(Long databaseId) {
        DataDatabaseEntity dataDatabaseEntity=baseMapper.selectById(databaseId);
        if (dataDatabaseEntity == null) {
            // 处理未找到的情况
            throw new IllegalArgumentException("Database not found for id: " + databaseId);
        }
        return dataDatabaseEntity.getDatasourceId();
    }

    public String getDatabasenameByID(Long databaseId){
        DataDatabaseEntity dataDatabaseEntity=baseMapper.selectById(databaseId);
        if (dataDatabaseEntity == null) {
            // 处理未找到的情况
            throw new IllegalArgumentException("Database not found for id: " + databaseId);
        }
        return dataDatabaseEntity.getDatabaseName();
    }



}


