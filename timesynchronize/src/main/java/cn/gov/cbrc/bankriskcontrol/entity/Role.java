package cn.gov.cbrc.bankriskcontrol.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 角色
 * 
 * @author pl
 * 
 */
@Entity
@Table(name="role")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 6820068038460083208L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="role_id")
	private long roleId;

	private String description;

	@Column(name="role_name")
	private String roleName;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "role_permissions", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "PERMISSION_ID") })
	private Set<Permission> permissions;
	
	public Role() {
	}

	public long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Permission> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Transient
	public Set<String> getStringPermissions(){
		Set<String> result= new HashSet<String>();
		for(Permission perm:permissions){
			result.add(perm.getPermissionName());
		}
		return result;
	}
	
	@Transient
	public  String getPermisionStringDescription(){
		StringBuilder sb = new StringBuilder();
		int t =0;
		for(Permission perm:permissions){
			t++;
			sb.append(perm.getDescription());
			if(t!=permissions.size())
			   sb.append(",");
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(null==o)
			return false;
		if(o instanceof Role){
			Role p =(Role)o;
			if(p.getRoleId()==getRoleId())
				return true;
		}
		return false;
	}
	
	
}
