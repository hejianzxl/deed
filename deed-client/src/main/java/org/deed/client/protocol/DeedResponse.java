package org.deed.client.protocol;

public class DeedResponse {
	
	private Long requestId;
	
	private String error;
	
	private Object result;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "DeedRespone [requestId=" + requestId + ", error=" + error + ", result=" + result + "]";
	}
}
