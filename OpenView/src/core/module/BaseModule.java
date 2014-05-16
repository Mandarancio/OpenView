package core.module;

public class BaseModule {
	private String name_;
	private String version_;

	public BaseModule(String name, String version) {
		name_ = name;
		version_ = version;
	}

	public String getModuleName() {
		return name_;
	}

	public String getVersion() {
		return version_;
	}
	
}
