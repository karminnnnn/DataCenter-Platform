package net.srt.framework.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户信息
 *
 * @author 阿沐 babamu@126.com
 */
@Data
public class UserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String realName;
    private String avatar;
    private Integer gender;
    private Long orgId;
    private Integer status;
    private Integer superAdmin;

    /**
     * 数据权限范围
     * null：表示全部数据权限
     */
    private List<Long> dataScopeList;
	/**
	 * 数据权限
	 */
	private Integer dataScope;
    /**
     * 帐户是否过期
     */
    private boolean isAccountNonExpired = true;
    /**
     * 帐户是否被锁定
     */
    private boolean isAccountNonLocked = true;
    /**
     * 密码是否过期
     */
    private boolean isCredentialsNonExpired = true;
    /**
     * 帐户是否可用
     */
    private boolean isEnabled = true;
    /**
     * 拥有权限集合
     */
    private Set<String> authoritySet;
	/**
	 * 拥有的项目id列表
	 */
	private List<Long> projectIds;

    /**
     * 用于返回用户具有的所有权限。
     * @return Collection<? extends GrantedAuthority> 返回一个权限集合，其中每个权限都是 {@link GrantedAuthority} 接口的实现。
     *         这里具体返回的是 {@link SimpleGrantedAuthority} 对象的集合。
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将authoritySet中的元素映射为SimpleGrantedAuthority对象，并收集到一个Set中返回
        return authoritySet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
