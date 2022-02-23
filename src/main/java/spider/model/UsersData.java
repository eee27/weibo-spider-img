package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName UserData
 * @Description 用户请求的基础数据 /getindex返回结果
 * @date 2022/2/8 9:10
 * @Author eee27
 */
//https://m.weibo.cn/api/container/getIndex?uid=3895364154&luicode=10000011&lfid=100103type%3D1%26q%3Deee27&type=uid&value=3895364154&containerid=1005053895364154
	@Deprecated
public class UsersData implements Serializable {
	@JsonProperty(value = "userInfo")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
