package run.init.policy;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

public class PluginPolicy extends Policy{
	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		Permissions p=new Permissions();
		p.add(new AllPermission());
		return p;
	}
	
	@Override
	public void refresh() {
	}
}
