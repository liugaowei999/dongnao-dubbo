package com.dongnao.jack.invoke;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.loadbalance.NodeInfo;
import com.dongnao.jack.rpc.http.HttpRequest;

public class HttpInvoke implements Invoke {

	public String invoke(Invocation invocation) {
		// TODO Auto-generated method stub
		System.out.println("run: HttpInvoke");

		List<String> registryInfo = invocation.getReference().getRegistryInfo();

		NodeInfo nodeInfo = invocation.getReference().getLoadBalanceBean()
				.doSelect(registryInfo);
		// 此处通过json字符串进行远程调用的序列号【实际可以采用开源的序列号工具包来实现】
		JSONObject sendParam = new JSONObject();
		sendParam.put("methodName", invocation.getMethod().getName());
		sendParam.put("methodParams", invocation.getObjects());
		sendParam.put("serviceId", invocation.getReference().getId());
		sendParam.put("paramTypes", invocation.getMethod().getParameterTypes());

		// 构建url http://IP:PORT/PATH
		String url = "http://" + nodeInfo.getHost() + ":" + nodeInfo.getPort() +
				nodeInfo.getContextPath();
		System.out.println("Consumer: url=" + url);

		// 通过HTTP协议调用生产者提供的服务
		String Result = HttpRequest.sendPost(url, sendParam.toJSONString());
		System.out.println("Result=" + Result);
		return Result;
	}

}
