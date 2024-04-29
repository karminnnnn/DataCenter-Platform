package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterColumnConvert;
import net.srt.dao.DataGovernanceMasterColumnDao;
import net.srt.entity.DataGovernanceMasterColumnEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataGovernanceMasterColumnService;
import net.srt.vo.DataGovernanceMasterColumnVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.ColumnDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据治理-主数据模型字段
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@Service
@AllArgsConstructor
public class DataGovernanceMasterColumnServiceImpl extends BaseServiceImpl<DataGovernanceMasterColumnDao, DataGovernanceMasterColumnEntity> implements DataGovernanceMasterColumnService {

	@Override
	public void save(DataGovernanceMasterColumnVO vo) {
		DataGovernanceMasterColumnEntity entity = DataGovernanceMasterColumnConvert.INSTANCE.convert(vo);

		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMasterColumnVO vo) {
		DataGovernanceMasterColumnEntity entity = DataGovernanceMasterColumnConvert.INSTANCE.convert(vo);

		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

	@Override
	public List<DataGovernanceMasterColumnVO> middleDbClumnInfo(String tableName) {
		DataProjectCacheBean project = getProject();
		ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(project.getDbType());
		IMetaDataByJdbcService service = new MetaDataByJdbcServiceImpl(productTypeEnum);
		List<ColumnDescription> columnDescriptions = service.queryTableColumnMeta(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName);
		List<String> pks = service.queryTablePrimaryKeys(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), project.getDbSchema(), tableName);
		List<DataGovernanceMasterColumnVO> columnVOS = new ArrayList<>(10);
		for (ColumnDescription columnDescription : columnDescriptions) {
			if (pks.contains(columnDescription.getFieldName())) {
				columnDescription.setPk(true);
			}
			DataGovernanceMasterColumnVO columnVO = new DataGovernanceMasterColumnVO(columnDescription);
			columnVOS.add(columnVO);
		}
		return columnVOS;
	}

}
