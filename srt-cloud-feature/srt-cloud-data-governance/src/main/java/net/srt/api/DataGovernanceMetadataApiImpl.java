package net.srt.api;

import lombok.RequiredArgsConstructor;
import net.srt.api.module.data.governance.DataMetadataApi;
import net.srt.api.module.data.governance.dto.DataGovernanceMetadataDto;
import net.srt.convert.DataGovernanceMetadataConvert;
import net.srt.framework.common.utils.Result;
import net.srt.service.DataGovernanceMetadataService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DataAccessApiImpl
 * @Author zrx
 * @Date 2022/10/26 11:50
 */
@RestController
@RequiredArgsConstructor
public class DataGovernanceMetadataApiImpl implements DataMetadataApi {

	private final DataGovernanceMetadataService metadataService;

	@Override
	public Result<DataGovernanceMetadataDto> getById(Integer id) {
		return Result.ok(DataGovernanceMetadataConvert.INSTANCE.convertDto(metadataService.getById(id)));
	}
}
