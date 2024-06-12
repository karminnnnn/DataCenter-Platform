package net.srt.api;

import lombok.RequiredArgsConstructor;
import net.srt.api.module.system.UserLogApi;
import net.srt.framework.security.cache.TokenStoreCache;
import net.srt.framework.security.user.UserDetail;
import net.srt.system.service.SysUserActivityLogService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserLogApiImpl implements UserLogApi {
    private final SysUserActivityLogService service;
    private final TokenStoreCache tokenStoreCache;
    @Override
    public String save(String accessToken,Integer action){
        System.out.println("成功调用");
        UserDetail userDetail = tokenStoreCache.getUser(accessToken);
        String userName = userDetail.getUsername();
        service.save(userName,action);
        return "成功调用";
    }
}
