package net.srt.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.governance.DataMetadataCollectApi;
import net.srt.api.module.data.governance.constant.DbType;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataCollectDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataCollectRecordDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataPropertyDto;
import net.srt.convert.DataGovernanceMetadataCollectConvert;
import net.srt.convert.DataGovernanceMetadataCollectRecordConvert;
import net.srt.convert.DataGovernanceMetadataConvert;
import net.srt.convert.DataGovernanceMetadataPropertyConvert;
import net.srt.entity.DataGovernanceMetadataCollectEntity;
import net.srt.entity.DataGovernanceMetadataCollectRecordEntity;
import net.srt.entity.DataGovernanceMetadataEntity;
import net.srt.entity.DataGovernanceMetadataPropertyEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMetadataCollectRecordService;
import net.srt.service.DataGovernanceMetadataCollectService;
import net.srt.service.DataGovernanceMetadataPropertyService;
import net.srt.service.DataGovernanceMetadataService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DataAccessApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataGovernanceMetadataCollectApiImpl implements DataMetadataCollectApi {

	private final DataGovernanceMetadataService metadataService;
	private final DataGovernanceMetadataPropertyService metadataPropertyService;
	private final DataGovernanceMetadataCollectService metadataCollectService;
	private final DataGovernanceMetadataCollectRecordService collectRecordService;

	@Override
	public Result<DataGovernanceMetadataCollectDto> getById(Long id) {
		return Result.ok(DataGovernanceMetadataCollectConvert.INSTANCE.convertDto(metadataCollectService.getById(id)));
	}

	@Override
	public DataGovernanceMetadataCollectRecordDto addCollectRecord(DataGovernanceMetadataCollectRecordDto collectRecordDto) {
		DataGovernanceMetadataCollectRecordEntity entity = DataGovernanceMetadataCollectRecordConvert.INSTANCE.convert(collectRecordDto);
		collectRecordService.save(entity);
		collectRecordDto.setId(entity.getId());
		return collectRecordDto;
	}

	@Override
	public void upCollectRecord(DataGovernanceMetadataCollectRecordDto collectRecordDto) {
		collectRecordService.updateById(DataGovernanceMetadataCollectRecordConvert.INSTANCE.convert(collectRecordDto));
	}

	@Override
	public Result<DataGovernanceMetadataDto> getByParentIdAndDatasourceId(Long parnetId, Long datasourceId) {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataEntity::getParentId, parnetId).eq(DataGovernanceMetadataEntity::getDatasourceId, datasourceId).last("limit 1");
		return Result.ok(DataGovernanceMetadataConvert.INSTANCE.convertDto(metadataService.getOne(wrapper)));
	}

	@Override
	public Result<DataGovernanceMetadataDto> getMetadataById(Long metadataId) {
		return Result.ok(DataGovernanceMetadataConvert.INSTANCE.convertDto(metadataService.getById(metadataId)));
	}

	@Override
	public Result<DataGovernanceMetadataDto> getByParentIdAndOtherInfo(Long parnetId, Long datasourceId, String code, Long metamodelId) {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataEntity::getParentId, parnetId).eq(datasourceId != null, DataGovernanceMetadataEntity::getDatasourceId, datasourceId)
				.eq(DataGovernanceMetadataEntity::getCode, code)
				.eq(datasourceId == null, DataGovernanceMetadataEntity::getDbType, DbType.MIDDLE_DB.getValue())
				.eq(DataGovernanceMetadataEntity::getMetamodelId, metamodelId)
				.last("limit 1");
		return Result.ok(DataGovernanceMetadataConvert.INSTANCE.convertDto(metadataService.getOne(wrapper)));
	}

	@Override
	public DataGovernanceMetadataDto addOrUpdateMetadata(DataGovernanceMetadataDto metadataDto) {
		DataGovernanceMetadataEntity entity = DataGovernanceMetadataConvert.INSTANCE.convert(metadataDto);
		if (metadataDto.getId() != null) {
			metadataService.updateById(entity);
		} else {
			metadataService.save(entity);
		}
		metadataDto.setId(entity.getId());
		return metadataDto;
	}

	@Override
	public Result<DataGovernanceMetadataPropertyDto> getByPropertyIdAndMetadataId(Long propertyId, Long metadataId) {
		LambdaQueryWrapper<DataGovernanceMetadataPropertyEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataPropertyEntity::getMetamodelPropertyId, propertyId).eq(DataGovernanceMetadataPropertyEntity::getMetadataId, metadataId).last("limit 1");
		return Result.ok(DataGovernanceMetadataPropertyConvert.INSTANCE.convertDto(metadataPropertyService.getOne(wrapper)));
	}

	@Override
	public void addOrUpdateMetadataProperty(DataGovernanceMetadataPropertyDto metadataPropertyDto) {
		if (metadataPropertyDto.getId() == null) {
			metadataPropertyService.save(DataGovernanceMetadataPropertyConvert.INSTANCE.convert(metadataPropertyDto));
		} else {
			metadataPropertyService.updateById(DataGovernanceMetadataPropertyConvert.INSTANCE.convert(metadataPropertyDto));
		}

	}

	@Override
	public Result<List<DataGovernanceMetadataDto>> listParentIdAndDatasourceId(Long parentId, Long datasourceId, Long metamodelId) {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataEntity::getParentId, parentId).eq(DataGovernanceMetadataEntity::getDatasourceId, datasourceId).eq(DataGovernanceMetadataEntity::getMetamodelId, metamodelId);
		return Result.ok(DataGovernanceMetadataConvert.INSTANCE.convertDtoList(metadataService.list(wrapper)));
	}

	@Override
	public void deleteMetadata(Long id) {
		metadataService.deleteAll(id);
	}

	@Override
	public Result<DataGovernanceMetadataCollectDto> getByDatasourceId(Long id) {
		LambdaQueryWrapper<DataGovernanceMetadataCollectEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataCollectEntity::getDatabaseId, id).last("limit 1");
		return Result.ok(DataGovernanceMetadataCollectConvert.INSTANCE.convertDto(metadataCollectService.getOne(wrapper)));
	}

	@Override
	public Result<DataGovernanceMetadataDto> getMetadataByDatasourceId(Long id) {
		LambdaQueryWrapper<DataGovernanceMetadataEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMetadataEntity::getDatasourceId, id).last("limit 1");
		return Result.ok(DataGovernanceMetadataConvert.INSTANCE.convertDto(metadataService.getOne(wrapper)));
	}
}
