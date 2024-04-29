package net.srt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceLabelModelConvert;
import net.srt.dto.CheckSqlRequest;
import net.srt.dto.CheckSqlResult;
import net.srt.entity.DataGovernanceLabelModelEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceLabelModelQuery;
import net.srt.vo.DataGovernanceLabelModelVO;
import net.srt.dao.DataGovernanceLabelModelDao;
import net.srt.service.DataGovernanceLabelModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.ColumnDescription;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.List;

/**
 * 数据治理-标签实体
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-12-24
 */
@Service
@AllArgsConstructor
public class DataGovernanceLabelModelServiceImpl extends BaseServiceImpl<DataGovernanceLabelModelDao, DataGovernanceLabelModelEntity> implements DataGovernanceLabelModelService {

	@Override
	public PageResult<DataGovernanceLabelModelVO> page(DataGovernanceLabelModelQuery query) {
		IPage<DataGovernanceLabelModelEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceLabelModelConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceLabelModelEntity> getWrapper(DataGovernanceLabelModelQuery query) {
		LambdaQueryWrapper<DataGovernanceLabelModelEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataGovernanceLabelModelEntity::getName, query.getName());
		return wrapper;
	}

	@Override
	public void save(DataGovernanceLabelModelVO vo) {
		DataGovernanceLabelModelEntity entity = DataGovernanceLabelModelConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceLabelModelVO vo) {
		DataGovernanceLabelModelEntity entity = DataGovernanceLabelModelConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

	@Override
	public CheckSqlResult checkSql(CheckSqlRequest checkSqlRequest) {
		DataProjectCacheBean project = getProject();
		ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(project.getDbType());
		IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
		CheckSqlResult checkSqlResult = new CheckSqlResult();
		checkSqlResult.setSuccess(true);
		try {
			metaDataService.querySqlColumnMeta(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), checkSqlRequest.getSql());
			return checkSqlResult;
		} catch (Exception e) {
			checkSqlResult.setSuccess(false);
			checkSqlResult.setErrorMsg(e.getMessage());
			return checkSqlResult;
		}
	}

}
