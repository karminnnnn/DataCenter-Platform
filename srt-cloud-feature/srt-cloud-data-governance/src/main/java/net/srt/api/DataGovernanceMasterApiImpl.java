package net.srt.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.srt.api.module.data.governance.DataMasterApi;
import net.srt.api.module.data.governance.dto.DataGovernanceMasterColumnDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMasterDistributeDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMasterDistributeLogDto;
import net.srt.api.module.data.governance.dto.DataGovernanceMasterModelDto;
import net.srt.convert.DataGovernanceMasterColumnConvert;
import net.srt.convert.DataGovernanceMasterDistributeConvert;
import net.srt.convert.DataGovernanceMasterDistributeLogConvert;
import net.srt.convert.DataGovernanceMasterModelConvert;
import net.srt.dao.DataGovernanceMasterColumnDao;
import net.srt.dao.DataGovernanceMasterDistributeDao;
import net.srt.dao.DataGovernanceMasterDistributeLogDao;
import net.srt.dao.DataGovernanceMasterModelDao;
import net.srt.entity.DataGovernanceMasterColumnEntity;
import net.srt.entity.DataGovernanceMasterDistributeEntity;
import net.srt.entity.DataGovernanceMasterDistributeLogEntity;
import net.srt.entity.DataGovernanceMasterModelEntity;
import net.srt.framework.common.utils.Result;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DataGovernanceQualityApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DataGovernanceMasterApiImpl implements DataMasterApi {

	private final DataGovernanceMasterDistributeDao masterDistributeDao;
	private final DataGovernanceMasterModelDao masterModelDao;
	private final DataGovernanceMasterColumnDao masterColumnDao;
	private final DataGovernanceMasterDistributeLogDao distributeLogDao;

	@Override
	public Result<DataGovernanceMasterDistributeDto> getDistributeById(Long id) {
		DataGovernanceMasterDistributeEntity masterDistributeEntity = masterDistributeDao.selectById(id);
		return Result.ok(DataGovernanceMasterDistributeConvert.INSTANCE.convertDto(masterDistributeEntity));
	}

	@Override
	public Result<DataGovernanceMasterDistributeLogDto> addDistributeLog(DataGovernanceMasterDistributeLogDto distributeLogDto) {
		DataGovernanceMasterDistributeLogEntity distributeLogEntity = DataGovernanceMasterDistributeLogConvert.INSTANCE.convert(distributeLogDto);
		distributeLogDao.insert(distributeLogEntity);
		distributeLogDto.setId(distributeLogEntity.getId());
		return Result.ok(distributeLogDto);
	}

	@Override
	public void upDistributeLog(DataGovernanceMasterDistributeLogDto distributeLogDto) {
		distributeLogDao.updateById(DataGovernanceMasterDistributeLogConvert.INSTANCE.convert(distributeLogDto));
	}

	@Override
	public Result<DataGovernanceMasterModelDto> getMasterModelById(Long masterModelId) {
		DataGovernanceMasterModelEntity modelEntity = masterModelDao.selectById(masterModelId);
		DataGovernanceMasterModelDto modelDto = DataGovernanceMasterModelConvert.INSTANCE.convertDto(modelEntity);
		LambdaQueryWrapper<DataGovernanceMasterColumnEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataGovernanceMasterColumnEntity::getMasterModelId, masterModelId);
		List<DataGovernanceMasterColumnEntity> columns = masterColumnDao.selectList(wrapper);
		List<DataGovernanceMasterColumnDto> columnDtos = DataGovernanceMasterColumnConvert.INSTANCE.convertDtoList(columns);
		modelDto.setColumns(columnDtos);
		return Result.ok(modelDto);
	}

	@Override
	public Result<String> testDistribute(List<Map<String, Object>> listBody) {
		log.info("receive distribute msg!");
		for (Map<String, Object> map : listBody) {
			log.info(map.toString());
		}
		log.info("receive distribute msg end!");
		return Result.ok();
	}
}
