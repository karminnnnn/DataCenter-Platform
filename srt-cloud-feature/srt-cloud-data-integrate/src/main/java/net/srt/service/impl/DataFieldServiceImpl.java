package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
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
            columnDescriptionVo.setDatatableId(query.getDatatableId().toString());
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
    public void save(DataFieldVO vo) {
        DataFieldEntity entity = DataFieldConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(DataFieldVO vo) {
        DataFieldEntity entity = DataFieldConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
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
                    columnDescriptionVo.setDatatableId(datatableId.toString());
                    columnDescriptionVo.setDatatableName(tableName);
                    return columnDescriptionVo;
                });

    }

    private String getTableNameByTableId(Long tableId) {
        // 使用 dataTableService 根据 tableId 获取 DataTableEntity
        DataTableEntity entity = dataTableService.getById(tableId);
        return entity != null ? entity.getTableName() : null;
    }

}
