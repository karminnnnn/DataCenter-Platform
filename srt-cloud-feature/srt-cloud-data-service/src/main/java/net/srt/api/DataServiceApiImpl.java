package net.srt.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.service.DataServiceApi;
import net.srt.api.module.data.service.dto.DataServiceApiAuthDto;
import net.srt.api.module.data.service.dto.DataServiceApiConfigDto;
import net.srt.convert.DataServiceApiAuthConvert;
import net.srt.convert.DataServiceApiConfigConvert;
import net.srt.dao.DataServiceApiAuthDao;
import net.srt.entity.DataServiceApiAuthEntity;
import net.srt.entity.DataServiceApiConfigEntity;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataServiceApiConfigService;
import net.srt.service.DataServiceAppService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DataAccessApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataServiceApiImpl implements DataServiceApi {

	private final DataServiceApiConfigService apiConfigService;
	private final DataServiceApiAuthDao apiAuthDao;

	@Override
	public Result<DataServiceApiConfigDto> getById(Long id) {
		return Result.ok(DataServiceApiConfigConvert.INSTANCE.convertDto(apiConfigService.getById(id)));
	}

	@Override
	public Result<String> auth(DataServiceApiAuthDto apiAuthDto) {
		DataServiceApiConfigEntity apiConfigEntity = apiConfigService.getById(apiAuthDto.getApiId());
		apiAuthDto.setGroupId(apiConfigEntity.getGroupId());
		//判断是否存在
		LambdaQueryWrapper<DataServiceApiAuthEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(DataServiceApiAuthEntity::getAppId, apiAuthDto.getAppId()).eq(DataServiceApiAuthEntity::getApiId, apiAuthDto.getApiId()).last("LIMIT 1");
		DataServiceApiAuthEntity apiAuthEntity = apiAuthDao.selectOne(wrapper);
		if (apiAuthEntity != null) {
			if (!apiAuthDto.getHasActiveApply()) {
				//如果已经没有有效的申请，则删除该授权
				apiAuthDao.deleteById(apiAuthEntity.getId());
			} else {
				apiAuthDto.setId(apiAuthEntity.getId());
				apiAuthDao.updateById(DataServiceApiAuthConvert.INSTANCE.convertDto(apiAuthDto));
			}
		} else {
			apiAuthDao.insert(DataServiceApiAuthConvert.INSTANCE.convertDto(apiAuthDto));
		}
		return Result.ok();
	}
}
