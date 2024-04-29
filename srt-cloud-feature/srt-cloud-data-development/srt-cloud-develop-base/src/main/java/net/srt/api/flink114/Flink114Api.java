package net.srt.api.flink114;

import net.srt.api.DevelopFlinkApi;
import net.srt.api.ServerNames;
import net.srt.flink.gateway.result.TestResult;
import net.srt.vo.DataProductionClusterConfigurationVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName DataProductionTaskApi
 * @Author zrx
 * @Date 2022/10/26 11:39
 */
@FeignClient(name = ServerNames.DEVELOP_FLINK_114)
public interface Flink114Api extends DevelopFlinkApi {

}
