package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName HomePage
 * @Description 根据用户id请求首页时data内包的
 * @date 2022/2/8 11:56
 * @Author eee27
 */
public class HomePage implements Serializable {
	@JsonProperty(value = "userInfo")
	private User userInfo;
	@JsonProperty(value = "tabsInfo")
	private TabsData tabsData;

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public TabsData getTabsData() {
		return tabsData;
	}

	public void setTabsData(TabsData tabsData) {
		this.tabsData = tabsData;
	}
}
