package com.dongnao.jack.invoke;

import java.rmi.RemoteException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.configBean.Reference;
import com.dongnao.jack.loadbalance.LoadBalance;
import com.dongnao.jack.loadbalance.NodeInfo;
import com.dongnao.jack.rmi.RmiUtil;
import com.dongnao.jack.rmi.SoaRmi;

/**
 * RMI 方式下， 客户端调用远程服务
 * 
 * @author liugaowei
 *
 */
public class RmiInvoke implements Invoke {

	public String invoke(Invocation invocation) {
		List<String> registryInfo = invocation.getReference().getRegistryInfo();
		// 负载均衡
		String loadbalance = invocation.getReference().getLoadbalance();
		LoadBalance loadBalanceBean = Reference.getLoadbanlanceMap().get(loadbalance);
		NodeInfo nodeInfo = loadBalanceBean.doSelect(registryInfo);

		// 通过RMI方式调用远程服务，传递参数给远端
		JSONObject sendParam = new JSONObject();
		sendParam.put("methodName", invocation.getMethod().getName());
		sendParam.put("methodParams", invocation.getObjects());
		sendParam.put("serviceId", invocation.getReference().getId());
		sendParam.put("paramTypes", invocation.getMethod().getParameterTypes());

		try {
			RmiUtil rmi = new RmiUtil();
			System.out.println("RMI CLIENT getting ......");
			SoaRmi startRmiClient = rmi.startRmiClient(nodeInfo, "SOA_RMI_SERVICE"); // 写死为固定字符串，与服务端启动时的
			System.out.println("RMI CLIENT geted.");
			System.out.println("RMI param= " + sendParam.toJSONString());
			return startRmiClient.invoke(sendParam.toJSONString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ID要一致

		return null;
	}

}
