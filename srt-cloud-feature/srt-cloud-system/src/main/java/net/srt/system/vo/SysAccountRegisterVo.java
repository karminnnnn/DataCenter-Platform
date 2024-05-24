package net.srt.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* @description: 前端传入注册信息
* @author PatrickLi 373595331@qq.com
* @date 2024/5/13
*/
@Data
@Schema(description = "账号注册vo")
public class SysAccountRegisterVo {
    private static final long serialVersionUID = 1L;

    @Schema(description = "验证码")
    private String captcha;
    @Schema(description = "唯一key")
    private String key;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "确认密码")
    private String confrimPassword;

}
