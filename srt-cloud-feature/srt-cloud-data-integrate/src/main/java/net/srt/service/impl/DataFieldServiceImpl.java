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
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataFieldQuery;
import net.srt.query.DataTableQuery;
import net.srt.service.DataFieldService;
import net.srt.vo.DataFieldVO;
import net.srt.vo.DataTableVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.TableDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataFieldServiceImpl extends BaseServiceImpl<DataFieldDao, DataFieldEntity> implements DataFieldService {
    private final DataFieldDao dataFieldDao;

    @Override
    public PageResult<DataFieldVO> page(DataFieldQuery query) {
        // 获取该数据库的所有表
        DataProjectCacheBean project = getProject();
        IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
        List<TableDescription> tableList = metaDataService.queryTableList(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema())
                .stream().filter(item -> item.getTableName().startsWith(DataHouseLayer.ODS.getTablePrefix()))
                .filter(item -> !StringUtil.isNotBlank(query.getDatatableName()) || item.getTableName().contains(query.getDatatableName()))
                //.filter(item -> !StringUtil.isNotBlank(query.getRemarks()) || item.getRemarks().contains(query.getRemarks()))
                .collect(Collectors.toList());

        int startIndex = (query.getPage() - 1) * query.getLimit();
        int endIndex = Math.min(query.getPage() * query.getLimit(), tableList.size());
        List<TableDescription> pageList = tableList.subList(startIndex, endIndex);
        List<DataFieldVO> dataFieldVOS = new ArrayList<>(10);
        for (TableDescription tableDescription : pageList) {
            DataFieldVO dataFieldVO = new DataFieldVO();
            dataFieldVO.setDatatableName(tableDescription.getTableName());
            dataFieldVO.setRemarks(tableDescription.getRemarks());
            dataFieldVO.setDatatableId(query.getDatatableId());

            // 查询 data_ods，补充数据
            LambdaQueryWrapper<DataFieldEntity> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(DataFieldEntity::getFieldName, dataFieldVO.getDatatableName()).eq(DataFieldEntity::getDatatableId, dataFieldVO.getDatatableId()).last("LIMIT 1");
            DataFieldEntity dataFieldEntity = baseMapper.selectOne(wrapper);
            if (dataFieldEntity != null) {
                dataFieldVO.setFieldId(dataFieldEntity.getId());
                dataFieldVO.setFieldName(dataFieldEntity.getFieldName());
                dataFieldVO.setFieldTypeName(dataFieldEntity.getFieldTypeName());
                //dataFieldVO.setRecentlySyncTime(dataFieldEntity.getRecentlySyncTime());
            }
            dataFieldVOS.add(dataFieldVO);
        }
        return new PageResult<>(dataFieldVOS, tableList.size());
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

}
