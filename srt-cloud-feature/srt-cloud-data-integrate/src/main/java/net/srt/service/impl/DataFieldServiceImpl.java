package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.DataAccessApiImpl;
import net.srt.api.DataDatabaseApiImpl;
import net.srt.api.module.data.integrate.dto.DataAccessDto;
import net.srt.api.module.data.integrate.dto.DataSourceDto;
import net.srt.api.module.quartz.QuartzDataAccessApi;
import net.srt.constants.DataHouseLayer;
import net.srt.convert.DataFieldConvert;
import net.srt.convert.DataOdsConvert;
import net.srt.dao.DataFieldDao;
import net.srt.entity.DataFieldEntity;
import net.srt.entity.DataTableEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataFieldQuery;
import net.srt.query.DataTableQuery;
import net.srt.service.DataFieldService;
import net.srt.service.DataTableService;
import net.srt.vo.ColumnDescriptionVo;
import net.srt.vo.DataFieldVO;
import net.srt.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.ColumnDescription;
import srt.cloud.framework.dbswitch.core.model.TableDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.column;

@Service
@AllArgsConstructor
public class DataFieldServiceImpl extends BaseServiceImpl<DataFieldDao, DataFieldEntity> implements DataFieldService {
    private final DataFieldDao dataFieldDao;
    @Autowired
    private DataTableService dataTableService;
    private DataAccessApiImpl dataAccessApi;
    private DataDatabaseApiImpl dataDatabaseApi;
    private final QuartzDataAccessApi quartzDataAccessApi;

    @Override
    public PageResult<ColumnDescriptionVo> page(DataFieldQuery query) {
        DataTableEntity tableEntity = dataTableService.getById(query.getDatatableId());
        if (tableEntity == null) {
            return new PageResult<>(new ArrayList<>(), 0);
        }

        DataProjectCacheBean project = getProject();
        IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));

        List<ColumnDescription> columnDescriptions = metaDataService.queryTableColumnMeta(
                project.getDbUrl(), project.getDbUsername(), project.getDbPassword(),
                project.getDbSchema(), tableEntity.getTableName()
        );

        List<ColumnDescriptionVo> columnDescriptionVos = columnDescriptions.stream().map(columnDescription -> {
            ColumnDescriptionVo columnDescriptionVo = new ColumnDescriptionVo();
            columnDescriptionVo.setFieldName(columnDescription.getFieldName());
            columnDescriptionVo.setRemarks(columnDescription.getRemarks());
            columnDescriptionVo.setFieldTypeName(columnDescription.getFieldTypeName());
            columnDescriptionVo.setDisplaySize(columnDescription.getDisplaySize());
            columnDescriptionVo.setScaleSize(columnDescription.getScaleSize());
            columnDescriptionVo.setDefaultValue(columnDescription.getDefaultValue());
            columnDescriptionVo.setNullable(columnDescription.isNullable());
            columnDescriptionVo.setPk(columnDescription.isPk());
            columnDescriptionVo.setAutoIncrement(columnDescription.isAutoIncrement());
            columnDescriptionVo.setDatatableId(query.getDatatableId());
            columnDescriptionVo.setDatatableName(tableEntity.getTableName());
            return columnDescriptionVo;
        }).collect(Collectors.toList());

        // Pagination
        int startIndex = (query.getPage() - 1) * query.getLimit();
        int endIndex = Math.min(query.getPage() * query.getLimit(), columnDescriptionVos.size());
        List<ColumnDescriptionVo> pageList = columnDescriptionVos.subList(startIndex, endIndex);

        return new PageResult<>(pageList, columnDescriptionVos.size());
    }

/*    @Override
    @Transactional(readOnly = true)
    public DataFieldVO getFieldInfo(Long fieldId) {
        DataFieldEntity entity = dataFieldDao.selectById(fieldId);
        return DataFieldConvert.INSTANCE.convert(entity);
    }*/

    @Override
    public void save(ColumnDescriptionVo vo) {
        //DataFieldEntity entity = DataFieldConvert.INSTANCE.convert(vo);
        newDataField(vo);
        quartzDataAccessApi.handRun(dataTableService.getaccessidbydatabaseid(vo.getDatatableId()));
       // baseMapper.insert(entity);
    }

    @Override
    public void update(ColumnDescriptionVo vo,String oldfieldname) {
        //DataFieldEntity entity = DataFieldConvert.INSTANCE.convert(vo);
        modifyField(vo,oldfieldname);
        quartzDataAccessApi.handRun(dataTableService.getaccessidbydatabaseid(vo.getDatatableId()));
        //updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String fieldname,Long datatableid) {
       // removeByIds(idList);
        deleteDataField(fieldname,datatableid);
        quartzDataAccessApi.handRun(dataTableService.getaccessidbydatabaseid(datatableid));
    }

    @Override
    public Optional<ColumnDescriptionVo> getColumnInfo(String fieldName,Long datatableId) {
        //DataOdsEntity dataOdsEntity = baseMapper.selectById(id);
        DataProjectCacheBean project = getProject();//获取project
        String tableName = getTableNameByTableId(datatableId);
        if (tableName == null) {
            return Optional.empty();
        }
        IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));//初始化元数据服务,总之得到service对象
        List<ColumnDescription> columnDescriptions = service.queryTableColumnMeta(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName);//查询表字段元数据，保存在columnDescriptions对象中
        List<String> pks = service.queryTablePrimaryKeys(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName);//查询表的主键，pks
        // 过滤出特定字段的信息
        return columnDescriptions.stream()
                .filter(column -> fieldName.equals(column.getFieldName())) // 使用 getFieldName() 方法
                .findFirst()
                .map(columnDescription -> {
                    ColumnDescriptionVo columnDescriptionVo = new ColumnDescriptionVo();
                    columnDescriptionVo.setFieldName(columnDescription.getFieldName());
                    columnDescriptionVo.setRemarks(columnDescription.getRemarks());
                    columnDescriptionVo.setRemarks(columnDescription.getLabelName());
                    columnDescriptionVo.setFieldTypeName(columnDescription.getFieldTypeName());
                    columnDescriptionVo.setDisplaySize(columnDescription.getDisplaySize());
                    columnDescriptionVo.setScaleSize(columnDescription.getScaleSize());
                    columnDescriptionVo.setDefaultValue(columnDescription.getDefaultValue());
                    columnDescriptionVo.setNullable(columnDescription.isNullable());
                    columnDescriptionVo.setPk(pks.contains(columnDescription.getFieldName()));
                    columnDescriptionVo.setAutoIncrement(columnDescription.isAutoIncrement());
                    columnDescriptionVo.setDatatableId(datatableId);
                    columnDescriptionVo.setDatatableName(tableName.replace("ods_",""));
                    return columnDescriptionVo;
                });

    }

    private String getTableNameByTableId(Long tableId) {
        // 使用 dataTableService 根据 tableId 获取 DataTableEntity
        DataTableEntity entity = dataTableService.getById(tableId);
        return entity != null ? entity.getTableName() : null;
    }

    public void modifyField(ColumnDescriptionVo query,String oldfieldname) {
        // 获取数据接入信息
        Long datatableid=query.getDatatableId();
        Long dataaccessid=dataTableService.getaccessidbydatabaseid(datatableid);//可能要两手中转，由table找base在找accsee再找dto
        DataAccessDto dataAccess = dataAccessApi.getById(dataaccessid).getData();
        if (dataAccess == null) {
            throw new RuntimeException("Data access not found");
        }
        // 获取源端数据库信息
        Long datasourceId = dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
        DataSourceDto dataSource = dataDatabaseApi.getById(datasourceId).getData();
        if (dataSource == null) {
            throw new RuntimeException("Data Source not found");
        }
        // 修改源端数据库中的字段
        modifySourceDatabaseField(dataSource, query,oldfieldname);
    }

    private void modifySourceDatabaseField(DataSourceDto dataSource, ColumnDescriptionVo query,String oldfieldname) {
        Long dataaccessid=dataTableService.getaccessidbydatabaseid(query.getDatatableId());
        String databasename=dataDatabaseApi.getDataBaseBamebyId(dataAccessApi.getById(dataaccessid).getData().getSourceDatabaseId()).getData();
        // 创建字段修改SQL
        StringBuilder alterSql = new StringBuilder();
        alterSql.append(String.format(
                "ALTER TABLE `%s`.`%s` CHANGE `%s` `%s` %s",
                databasename,
                query.getDatatableName(),
                oldfieldname,
                query.getFieldName(),
                query.getFieldTypeName()
        ));


        // 处理字段长度和小数位数
        if (query.getDisplaySize() != null) {
            if (query.getScaleSize() != null && query.getFieldTypeName().matches("DECIMAL|NUMERIC|FLOAT|DOUBLE")) {
                alterSql.append(String.format("(%d, %d)", query.getDisplaySize(), query.getScaleSize()));
            } else {
                alterSql.append(String.format("(%d)", query.getDisplaySize()));
            }
        }


        // 处理默认值
        if (query.getDefaultValue() != null) {
            alterSql.append(String.format(" DEFAULT '%s'", query.getDefaultValue()));
        }

        // 处理是否可为空
        if (Boolean.TRUE.equals(query.isNullable())) {
            alterSql.append(" NULL");
        } else {
            alterSql.append(" NOT NULL");
        }

        // 处理字段注释
        if (query.getRemarks() != null) {
            alterSql.append(String.format(" COMMENT '%s'", query.getRemarks()));
        }

        // 处理是否自增
        if (Boolean.TRUE.equals(query.isAutoIncrement())) {
            alterSql.append(" AUTO_INCREMENT");
        }

        // 创建数据库连接并执行SQL
        try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword())) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(alterSql.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to modify source database field", e);
        }
    }

    public void newDataField(ColumnDescriptionVo query) {
        // 获取数据接入信息
        Long datatableid=query.getDatatableId();
        Long dataaccessid=dataTableService.getaccessidbydatabaseid(datatableid);
        DataAccessDto dataAccess = dataAccessApi.getById(dataaccessid).getData();
        if (dataAccess == null) {
            throw new RuntimeException("Data access not found");
        }
        // 获取源端数据库信息
        Long datasourceid = dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
        DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
        if (dataSource == null) {
            throw new RuntimeException("Data Source not found");
        }

        createSourceDatabaseField(dataSource, query);
    }

    public void createSourceDatabaseField(DataSourceDto dataSource, ColumnDescriptionVo query) {
        Long dataaccessid=dataTableService.getaccessidbydatabaseid(query.getDatatableId());
        String databaseName=dataDatabaseApi.getDataBaseBamebyId(dataAccessApi.getById(dataaccessid).getData().getSourceDatabaseId()).getData();
        String tableName = query.getDatatableName();
        String fieldName = query.getFieldName();
        String fieldType = query.getFieldTypeName();
        Integer displaySize = query.getDisplaySize();
        Integer scaleSize = query.getScaleSize();
        String defaultValue = query.getDefaultValue();
        Boolean nullable =query.isNullable();
        String comment = query.getRemarks();
        Boolean autoIncrement = query.isAutoIncrement();

        // 构建 ADD COLUMN SQL 语句
        StringBuilder addFieldSql = new StringBuilder();
        addFieldSql.append(String.format("ALTER TABLE `%s`.`%s` ADD `%s` %s", databaseName, tableName, fieldName, fieldType));

        // 处理字段长度和小数位数
        if (displaySize != null) {
            if (scaleSize != null && fieldType.matches("DECIMAL|NUMERIC|FLOAT|DOUBLE")) {
                addFieldSql.append(String.format("(%d, %d)", displaySize, scaleSize));
            } else {
                addFieldSql.append(String.format("(%d)", displaySize));
            }
        }

        // 处理默认值
        if (defaultValue != null) {
            addFieldSql.append(String.format(" DEFAULT '%s'", defaultValue));
        }

        // 处理是否可为空
        if (Boolean.TRUE.equals(nullable)) {
            addFieldSql.append(" NULL");
        } else {
            addFieldSql.append(" NOT NULL");
        }

        // 处理字段注释
        if (comment != null) {
            addFieldSql.append(String.format(" COMMENT '%s'", comment));
        }

        // 处理是否自增
        if (Boolean.TRUE.equals(autoIncrement)) {
            addFieldSql.append(" AUTO_INCREMENT");
        }

        // 创建数据库连接并执行 SQL 语句
        try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword())) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(addFieldSql.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add new field to source database table", e);
        }
    }

    public void deleteDataField(String fieldName, Long datatableId) {
        // 获取数据接入信息
        Long dataaccessid = dataTableService.getaccessidbydatabaseid(datatableId);
        DataAccessDto dataAccess = dataAccessApi.getById(dataaccessid).getData();
        if (dataAccess == null) {
            throw new RuntimeException("Data access not found");
        }
        // 获取源端数据库信息
        Long datasourceid = dataDatabaseApi.getDatasourceIdbyDatabaseId(dataAccess.getSourceDatabaseId()).getData();
        DataSourceDto dataSource = dataDatabaseApi.getById(datasourceid).getData();
        if (dataSource == null) {
            throw new RuntimeException("Data Source not found");
        }

        deleteSourceDatabaseField(dataSource, fieldName,datatableId);
    }

    public void deleteSourceDatabaseField(DataSourceDto dataSource,String thefieldName, Long datatableId) {
        Long dataacessid=dataTableService.getaccessidbydatabaseid(datatableId);
        String databaseName = dataDatabaseApi.getDataBaseBamebyId(dataAccessApi.getById(dataacessid).getData().getSourceDatabaseId()).getData();
        String tableName = dataTableService.getById(datatableId).getTableName().replace("ods_","");
        String fieldName = thefieldName;

        // 构建 DROP COLUMN SQL 语句
        String dropFieldSql = String.format("ALTER TABLE `%s`.`%s` DROP COLUMN `%s`", databaseName, tableName, fieldName);

        // 创建数据库连接并执行 SQL 语句
        try (Connection connection = DriverManager.getConnection(dataSource.getJdbcUrl(), dataSource.getUserName(), dataSource.getPassword())) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(dropFieldSql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete field from source database table", e);
        }
    }

}
