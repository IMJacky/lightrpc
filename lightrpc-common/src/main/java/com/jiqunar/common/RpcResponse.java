package com.jiqunar.common;

/**
 * Rpc响应实体
 *
 * @author jieguang.wang
 * @date 2021/1/15 11:13
 */
public class RpcResponse {
    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 响应结果
     */
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
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
}
