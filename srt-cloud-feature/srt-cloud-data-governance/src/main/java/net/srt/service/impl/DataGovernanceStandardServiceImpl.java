package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.governance.constant.BuiltInMetamodel;
import net.srt.api.module.data.governance.constant.BuiltInMetamodelProperty;
import net.srt.api.module.data.governance.constant.DbType;
import net.srt.api.module.data.integrate.DataDatabaseApi;
import net.srt.api.module.data.integrate.dto.DataDatabaseDto;
import net.srt.constant.StandardDataType;
import net.srt.convert.DataGovernanceStandardConvert;
import net.srt.dao.DataGovernanceMetadataDao;
import net.srt.dao.DataGovernanceMetadataPropertyDao;
import net.srt.dao.DataGovernanceMetadataStandardRelDao;
import net.srt.dao.DataGovernanceStandardCodeDao;
import net.srt.dao.DataGovernanceStandardDao;
import net.srt.dto.CompareResult;
import net.srt.dto.StandardCheckDto;
import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.entity.DataGovernanceMetadataStandardRelEntity;
import net.srt.entity.DataGovernanceStandardCodeEntity;
import net.srt.entity.DataGovernanceStandardEntity;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceStandardQuery;
import net.srt.service.DataGovernanceStandardService;
import net.srt.vo.DataGovernanceMetamodelPropertyVO;
import net.srt.vo.DataGovernanceStandardVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;
import srt.cloud.framework.dbswitch.common.util.StringUtil;
import srt.cloud.framework.dbswitch.core.model.SchemaTableData;
import srt.cloud.framework.dbswitch.core.service.IMetaDataByJdbcService;
import srt.cloud.framework.dbswitch.core.service.impl.MetaDataByJdbcServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据治理-数据标准
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-05-05
 */
@Service
@AllArgsConstructor
public class DataGovernanceStandardServiceImpl extends BaseServiceImpl<DataGovernanceStandardDao, DataGovernanceStandardEntity> implements DataGovernanceStandardService {


	private final DataGovernanceMetadataStandardRelDao metadataStandardRelDao;
	private final DataGovernanceMetadataDao metadataDao;
	private final DataGovernanceMetadataPropertyDao metadataPropertyDao;
	private final DataGovernanceStandardCodeDao standardCodeDao;
	private final DataDatabaseApi dataDatabaseApi;

	@Override
	public PageResult<DataGovernanceStandardVO> page(DataGovernanceStandardQuery query) {
		IPage<DataGovernanceStandardEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
		List<DataGovernanceStandardEntity> records = page.getRecords();
		List<DataGovernanceStandardVO> dataGovernanceStandardVOS = DataGovernanceStandardConvert.INSTANCE.convertList(records);
		if (query.isIfMeta() && query.getMetadataId() != null) {
			for (DataGovernanceStandardVO standardVO : dataGovernanceStandardVOS) {
				LambdaQueryWrapper<DataGovernanceMetadataStandardRelEntity> wrapper = Wrappers.lambdaQuery();
				wrapper.eq(DataGovernanceMetadataStandardRelEntity::getMetadataId, query.getMetadataId())
						.eq(DataGovernanceMetadataStandardRelEntity::getStandardId, standardVO.getId()).last("limit 1");
				standardVO.setIfStandardRel(metadataStandardRelDao.selectOne(wrapper) != null);
			}
		}
		return new PageResult<>(dataGovernanceStandardVOS, page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceStandardEntity> getWrapper(DataGovernanceStandardQuery query) {
		LambdaQueryWrapper<DataGovernanceStandardEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(query.getCategoryId() != null, DataGovernanceStandardEntity::getCategoryId, query.getCategoryId())
				.like(StringUtil.isNotBlank(query.getEngName()), DataGovernanceStandardEntity::getEngName, query.getEngName())
				.like(StringUtil.isNotBlank(query.getCnName()), DataGovernanceStandardEntity::getCnName, query.getCnName())
				.eq(query.getType() != null, DataGovernanceStandardEntity::getType, query.getType())
				.orderByDesc(DataGovernanceStandardEntity::getCreateTime).orderByDesc(DataGovernanceStandardEntity::getId);
		dataScopeWithoutOrgId(wrapper);
		return wrapper;
	}

	@Override
	public void save(DataGovernanceStandardVO vo) {
		DataGovernanceStandardEntity entity = DataGovernanceStandardConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceStandardVO vo) {
		DataGovernanceStandardEntity entity = DataGovernanceStandardConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			DataGovernanceStandardEntity standardEntity = baseMapper.selectById(id);
			//如果是标准字段，判断是否有元数据与之关联
			if (standardEntity.getType() == 1) {
				LambdaQueryWrapper<DataGovernanceMetadataStandardRelEntity> wrapper = Wrappers.lambdaQuery();
				wrapper.eq(DataGovernanceMetadataStandardRelEntity::getStandardId, id).last("limit 1");
				DataGovernanceMetadataStandardRelEntity standardRelEntity = metadataStandardRelDao.selectOne(wrapper);
				if (standardRelEntity != null) {
					throw new ServerException("存在元数据与该数据标准关联，不可删除！");
				}
			} else {
				//标准码表
				LambdaQueryWrapper<DataGovernanceStandardEntity> wrapper = Wrappers.lambdaQuery();
				wrapper.eq(DataGovernanceStandardEntity::getStandardCodeId, id).last("limit 1");
				DataGovernanceStandardEntity one = baseMapper.selectOne(wrapper);
				if (one != null) {
					throw new ServerException(String.format("存在标准字段【%s】与该码表关联，不可删除！", one.getCnName()));
				}
			}
		}
		removeByIds(idList);

	}

	@Override
	public void online(Long id) {
		DataGovernanceStandardEntity entity = new DataGovernanceStandardEntity();
		entity.setId(id);
		entity.setStatus(1);
		updateById(entity);
	}

	@Override
	public void offline(Long id) {
		DataGovernanceStandardEntity entity = new DataGovernanceStandardEntity();
		entity.setId(id);
		entity.setStatus(0);
		updateById(entity);
	}

	@Override
	public List<DataGovernanceStandardVO> listTableCode() {
		LambdaQueryWrapper<DataGovernanceStandardEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceStandardEntity::getProjectId, getProjectId())
				//标准码表
				.eq(DataGovernanceStandardEntity::getType, 2);
		return DataGovernanceStandardConvert.INSTANCE.convertList(baseMapper.selectList(wrapper));
	}

	@Override
	public StandardCheckDto checkStandard(Long metadataId, Long standardId) {
		//获取元数据属性
		List<DataGovernanceMetamodelPropertyVO> propertyVOS = metadataPropertyDao.listPropertyById(metadataId, BuiltInMetamodel.COLUMN.getId());
		DataGovernanceStandardEntity standardEntity = baseMapper.selectById(standardId);

		StandardCheckDto standardCheckDto = new StandardCheckDto();
		List<CompareResult> compareResults = new ArrayList<>();
		standardCheckDto.setCompareResults(compareResults);
		//检测字段属性是否符合标准
		for (DataGovernanceMetamodelPropertyVO propertyVO : propertyVOS) {
			if (BuiltInMetamodelProperty.COLUMN_DATA_TYPE.getId().equals(propertyVO.getMetamodelPropertyId())) {
				CompareResult compareResult = new CompareResult();
				compareResult.setProperty("数据类型");
				StandardDataType standardDataType = StandardDataType.getByCode(standardEntity.getDataType());
				compareResult.setMetadataVal(propertyVO.getValue());
				compareResult.setStandardVal(standardDataType.getLongValue());
				compareResult.setStandard(standardDataType.getDbDataTypes().contains(compareResult.getMetadataVal()));
				compareResults.add(compareResult);
			} else if (BuiltInMetamodelProperty.COLUMN_DATA_LENGTH.getId().equals(propertyVO.getMetamodelPropertyId())) {
				CompareResult compareResult = new CompareResult();
				compareResult.setProperty("长度");
				compareResult.setMetadataVal(propertyVO.getValue());
				compareResult.setStandardVal(standardEntity.getDataLength() != null ? standardEntity.getDataLength().toString() : null);
				compareResult.setStandard(compareResult.getMetadataVal() == null || compareResult.getMetadataVal().equals(compareResult.getStandardVal()));
				compareResults.add(compareResult);
			} else if (BuiltInMetamodelProperty.COLUMN_DATA_SCALE.getId().equals(propertyVO.getMetamodelPropertyId())) {
				CompareResult compareResult = new CompareResult();
				compareResult.setProperty("精度（小数位数）");
				compareResult.setMetadataVal(propertyVO.getValue());
				compareResult.setStandardVal(standardEntity.getDataPrecision() != null ? standardEntity.getDataPrecision().toString() : null);
				compareResult.setStandard(compareResult.getMetadataVal() == null || compareResult.getMetadataVal().equals(compareResult.getStandardVal()));
				compareResults.add(compareResult);
			} else if (BuiltInMetamodelProperty.COLUMN_NULLABLE.getId().equals(propertyVO.getMetamodelPropertyId())) {
				CompareResult compareResult = new CompareResult();
				compareResult.setProperty("允许为空");
				compareResult.setMetadataVal(propertyVO.getValue());
				compareResult.setStandardVal(standardEntity.getNullable() == 1 ? "是" : "否");
				compareResult.setStandard(compareResult.getMetadataVal().equals(compareResult.getStandardVal()));
				compareResults.add(compareResult);
			}
		}
		//判断是否关联了码表
		if (standardEntity.getStandardCodeId() == null) {
			standardCheckDto.setRelStandardCode(false);
			return standardCheckDto;
		}
		standardCheckDto.setRelStandardCode(true);
		//获取码表数据
		LambdaQueryWrapper<DataGovernanceStandardCodeEntity> codeWrapper = Wrappers.lambdaQuery();
		codeWrapper.eq(DataGovernanceStandardCodeEntity::getStandardId, standardEntity.getStandardCodeId());
		List<DataGovernanceStandardCodeEntity> standardCodes = standardCodeDao.selectList(codeWrapper);
		if (standardCodes.isEmpty()) {
			standardCheckDto.setHasStandardCode(false);
			return standardCheckDto;
		}
		standardCheckDto.setHasStandardCode(true);
		//检测符合标准的数量和不符合标准的数量
		DataGovernanceMetadataEntity cloumnMetadata = metadataDao.selectById(metadataId);
		//获取父级表的元数据
		DataGovernanceMetadataEntity tableMetadata = metadataDao.selectById(cloumnMetadata.getParentId());
		StringBuilder fillNumSql = new StringBuilder(String.format("SELECT COUNT(1) AS c FROM %s WHERE %s IN(", tableMetadata.getCode(), cloumnMetadata.getCode()));
		StringBuilder notFillNumSql = new StringBuilder(String.format("SELECT COUNT(1) AS c FROM %s WHERE %s NOT IN(", tableMetadata.getCode(), cloumnMetadata.getCode()));
		for (DataGovernanceStandardCodeEntity standardCode : standardCodes) {
			fillNumSql.append(String.format("'%s',", standardCode.getDataId()));
			notFillNumSql.append(String.format("'%s',", standardCode.getDataId()));
		}
		fillNumSql.deleteCharAt(fillNumSql.length() - 1);
		notFillNumSql.deleteCharAt(notFillNumSql.length() - 1);
		fillNumSql.append(")");
		notFillNumSql.append(")");
		standardCheckDto.setFillNumSql(fillNumSql.toString());
		standardCheckDto.setNotFillNumSql(notFillNumSql.toString());
		//数据库
		if (DbType.DATABASE.getValue().equals(cloumnMetadata.getDbType())) {
			DataDatabaseDto database = dataDatabaseApi.getById(cloumnMetadata.getDatasourceId()).getData();
			ProductTypeEnum productTypeEnum = ProductTypeEnum.getByIndex(database.getDatabaseType());
			IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(productTypeEnum);
			SchemaTableData schemaTableData = metaDataService.queryTableDataBySql(database.getJdbcUrl(), database.getUserName(), database.getPassword(), fillNumSql.toString(), 100);
			standardCheckDto.setFillNum(schemaTableData.getRows().get(0).get(0));
			schemaTableData = metaDataService.queryTableDataBySql(database.getJdbcUrl(), database.getUserName(), database.getPassword(), notFillNumSql.toString(), 100);
			standardCheckDto.setNotFullNum(schemaTableData.getRows().get(0).get(0));
			//中台库
		} else if (DbType.MIDDLE_DB.getValue().equals(cloumnMetadata.getDbType())) {
			DataProjectCacheBean project = getProject();
			IMetaDataByJdbcService metaDataService = new MetaDataByJdbcServiceImpl(ProductTypeEnum.getByIndex(project.getDbType()));
			SchemaTableData schemaTableData = metaDataService.queryTableDataBySql(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), fillNumSql.toString(), 100);
			standardCheckDto.setFillNum(schemaTableData.getRows().get(0).get(0));
			schemaTableData = metaDataService.queryTableDataBySql(project.getDbUrl(), project.getDbUsername(), project.getDbPassword(), notFillNumSql.toString(), 100);
			standardCheckDto.setNotFullNum(schemaTableData.getRows().get(0).get(0));
		}
		return standardCheckDto;
	}

}
