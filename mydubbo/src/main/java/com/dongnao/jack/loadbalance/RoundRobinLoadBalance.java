package com.dongnao.jack.loadbalance;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 轮询负载均衡算法
 * 
 * @author liugaowei
 *
 */
public class RoundRobinLoadBalance implements LoadBalance {

	private static Integer index = 0;

	public NodeInfo doSelect(List<String> registryInfo) {
		int curIndex = 0;
		synchronized (index) {
			if (index >= registryInfo.size()) {
				index = 0;
			}
			curIndex = index++;
		}
		String registry = registryInfo.get(curIndex);
		JSONObject registryjson = JSONObject.parseObject(registry);
		Collection<Object> values = registryjson.values();
		JSONObject node = new JSONObject();
		for (Object value : values) {
			node = JSONObject.parseObject(value.toString());
		}

		JSONObject protocol = node.getJSONObject("protocol");
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setHost(protocol.get("host") != null ? protocol.getString("host") : "");
		nodeInfo.setPort(protocol.get("port") != null ? protocol.getString("port") : "");
		nodeInfo.setContextPath(protocol.get("contextpath") != null ? protocol.getString("contextpath") : "");

		return nodeInfo;
	}

}
