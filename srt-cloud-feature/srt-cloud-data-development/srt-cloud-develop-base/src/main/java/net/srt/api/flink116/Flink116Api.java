package net.srt.api.flink116;

import net.srt.api.DevelopFlinkApi;
import net.srt.api.ServerNames;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName DataProductionTaskApi
 * @Author zrx
 * @Date 2022/10/26 11:39
 */
@FeignClient(name = ServerNames.DEVELOP_FLINK_116)
public interface Flink116Api extends DevelopFlinkApi {

}
