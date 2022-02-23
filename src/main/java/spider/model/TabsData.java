package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TabsData
 * @Description 装用户tab分页的壳,里面有多个tabs
 * @date 2022/2/8 9:50
 * @Author eee27
 */
public class TabsData implements Serializable
{
	@JsonProperty(value = "selectedTab")
	private Integer selectedTab;
	@JsonProperty(value = "tabs")
	private List<Tab> tabs;

	public Integer getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(Integer selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}
}
