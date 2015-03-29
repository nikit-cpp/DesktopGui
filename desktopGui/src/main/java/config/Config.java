package config;

import java.util.Set;

public class Config {
	private Set<String> groupNames;
	private String cacheFolder;
	private boolean isSearchFileOnDisk;

	public Set<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(Set<String> groupNames) {
		this.groupNames = groupNames;
	}

	public String getCacheFolder() {
		return cacheFolder;
	}

	public void setCacheFolder(String cacheFolder) {
		this.cacheFolder = cacheFolder;
	}

	public boolean getSearchFileOnDisk() {
		return isSearchFileOnDisk;
	}

	public void setSearchFileOnDisk(boolean isSearchFileOnDisk) {
		this.isSearchFileOnDisk = isSearchFileOnDisk;
	}
}
