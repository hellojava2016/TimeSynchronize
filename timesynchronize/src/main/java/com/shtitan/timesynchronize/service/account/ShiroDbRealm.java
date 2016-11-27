package com.shtitan.timesynchronize.service.account;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.shtitan.timesynchronize.entity.Permission;
import com.shtitan.timesynchronize.entity.Role;
import com.shtitan.timesynchronize.entity.User;


public class ShiroDbRealm extends AuthorizingRealm {
	private static Logger logger = Logger.getLogger(ShiroDbRealm.class); 	
	
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	//认证回调函数
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		 User user=userService.getUserByUserName(token.getUsername());
		if (user != null) {			
            if(user.getPasswd().equals(new String(token.getPassword()))){
            	setOnLine(user);
            	 return new SimpleAuthenticationInfo(  
                         user.getUsername(),user.getPasswd(), getName()); 
            }else{
            	throw new CredentialsException();
            }			 
		} else {
			//此处应该抛出用户不存在的异常
			throw new CredentialsException();
		}
	}
	
	private void setOnLine(User user){
		user.setOnline(true);
		userService.updateUser(user);
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username=(String)principals.fromRealm(getName()).iterator().next();  
        if( username != null ){  
            User user = userService.getUserByUserName( username );  
            if( user != null && user.getRoles() != null ){  
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();  
                for(Role role: user.getRoles() ){  
                        info.addRole(role.getRoleName());  
                        for(Permission permission :role.getPermissions()){
                        	info.addStringPermission(permission.getPermissionName());
                        } 
                }  
                logger.info("**********username:"+username);
                for(String per:info.getStringPermissions()){
                	logger.info("**********permission:"+per);
                }
                return info;  
            }  
        }  
        return null;  
	}
}
