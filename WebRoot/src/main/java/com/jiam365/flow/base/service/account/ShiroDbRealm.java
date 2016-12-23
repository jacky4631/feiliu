// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.base.service.account;

import com.google.common.base.Objects;
import java.util.Date;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import java.util.Collection;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.authc.AuthenticationException;
import com.jiam365.flow.server.entity.User;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.util.ByteSource;
import com.jiam365.modules.utils.Encodes;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import com.jiam365.flow.server.service.UserManager;
import org.apache.shiro.realm.AuthorizingRealm;

public class ShiroDbRealm extends AuthorizingRealm
{
    protected UserManager userManager;
    
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken authcToken) throws AuthenticationException {
        final UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
        final User user = this.userManager.getUserByUsername(token.getUsername());
        if (user != null && user.isEnabled()) {
            final byte[] salt = Encodes.decodeHex(user.getSalt());
            final ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(), user.getDisplayName(), user.getCompany(), user.getRegisterDate());
            return (AuthenticationInfo)new SimpleAuthenticationInfo((Object)shiroUser, (Object)user.getPassword(), ByteSource.Util.bytes(salt), this.getName());
        }
        return null;
    }
    
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        final ShiroUser shiroUser = (ShiroUser)principals.getPrimaryPrincipal();
        final User user = this.userManager.getUserByUsername(shiroUser.username);
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user != null) {
            info.addRoles((Collection)user.getRoles());
        }
        return (AuthorizationInfo)info;
    }
    
    @PostConstruct
    public void initCredentialsMatcher() {
        final HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA-1");
        matcher.setHashIterations(1024);
        this.setCredentialsMatcher((CredentialsMatcher)matcher);
    }
    
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }
    
    public static class ShiroUser implements Serializable
    {
        private static final long serialVersionUID = -1373760761780840081L;
        public Long id;
        public String username;
        public String displayName;
        public String company;
        public Date registerDate;
        
        public ShiroUser(final Long id, final String username, final String displayName) {
            this.id = id;
            this.username = username;
            this.displayName = displayName;
        }
        
        public ShiroUser(final Long id, final String username, final String displayName, final String company, final Date registerDate) {
            this(id, username, displayName);
            this.company = company;
            this.registerDate = registerDate;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public String getCompany() {
            return this.company;
        }
        
        public Date getRegisterDate() {
            return this.registerDate;
        }
        
        @Override
        public String toString() {
            return this.username;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(new Object[] { this.username });
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final ShiroUser other = (ShiroUser)obj;
            if (this.username == null) {
                if (other.username != null) {
                    return false;
                }
            }
            else if (!this.username.equals(other.username)) {
                return false;
            }
            return true;
        }
    }
}
