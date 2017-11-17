package com.dongnao.jack.loadbalance;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

/**
 * 随机负载均衡算法
 * 
 * @author liugaowei
 *
 */
public class RandomLoadBalance implements LoadBalance {

	public NodeInfo doSelect(List<String> registryInfo) {
		Random random = new Random();
		int index = random.nextInt(registryInfo.size());
		String registry = registryInfo.get(index);

		JSONObject registryJson = JSONObject.parseObject(registry);
		Collection<Object> values = registryJson.values();
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
