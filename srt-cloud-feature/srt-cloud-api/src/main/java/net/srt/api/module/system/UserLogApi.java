package net.srt.api.module.system;

import net.srt.api.ServerNames;
import net.srt.framework.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME, contextId = "sys-userActivityLog")
public interface UserLogApi {
    @PostMapping(value = "api/system/{accessToken}/{action}")
    String save(@PathVariable String accessToken, @PathVariable Integer action);
}
