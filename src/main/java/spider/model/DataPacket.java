package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName DataPacket
 * @Description 部分请求最外面标的data大包
 * @date 2022/2/8 9:13
 * @Author eee27
 */
public class DataPacket<T> implements Serializable {
	@JsonProperty("ok")
	private Integer status;
	@JsonProperty("data")
	private T result;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
