package com.jiqunar.client;

import com.jiqunar.common.RpcResponse;

/**
 * 干啥用的？？？
 * @author jieguang.wang
 * @date 2021/1/15 15:08
 */
public class DefaultFuture {
    private RpcResponse rpcResponse;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    public RpcResponse getRpcResponse(int timeout) {
        synchronized (object) {
            while (!isSucceed) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return rpcResponse;
        }
    }

    public void setRpcResponse(RpcResponse rpcResponse) {
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.rpcResponse = rpcResponse;
            this.isSucceed = true;
            object.notify();
        }
    }
}
