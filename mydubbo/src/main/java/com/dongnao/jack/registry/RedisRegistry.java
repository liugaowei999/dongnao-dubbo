package com.dongnao.jack.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.configBean.Protocol;
import com.dongnao.jack.configBean.Registry;
import com.dongnao.jack.configBean.Service;
import com.dongnao.jack.redis.RedisApi;

/**
 * redis 注册中心处理类
 * 
 * @author liugaowei
 *
 */
public class RedisRegistry implements BaseRegistry {

	public boolean registry(String ref, ApplicationContext applicationContext) {
		// TODO Auto-generated method stub
		Protocol protocol = applicationContext.getBean(Protocol.class);
		Map<String, Service> services = applicationContext.getBeansOfType(Service.class);

		Registry registry = applicationContext.getBean(Registry.class);
		// 建立redis连接池
		System.out.println("开始建立redis链接");
		try {
			RedisApi.createJedisPool(registry.getAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		for (Map.Entry<String, Service> entry : services.entrySet()) {
			if (entry.getValue().getRef().equals(ref)) {
				System.out.println("services for:protocol: " + protocol);
				System.out.println("services for:service: " + entry.getValue());
				JSONObject joJsonObject = new JSONObject();
				joJsonObject.put("protocol", JSONObject.toJSONString(protocol));
				joJsonObject.put("service", JSONObject.toJSONString(entry.getValue()));

				JSONObject ipport = new JSONObject();
				ipport.put(protocol.getHost() + ":" + protocol.getPort(), joJsonObject);

				System.out.println("ref=" + ref + ", json=[" + ipport.toJSONString() + "]");
				lpush(ipport, ref);

			}
		}

		return false;
	}

	private void lpush(JSONObject ipport, String ref) {
		if (RedisApi.exists(ref)) {
			Set<String> keys = ipport.keySet();
			String ipportStr = "";
			for (String kkString : keys) {
				ipportStr = kkString;
			}

			// 通过服务引用ref的名称，获取redis中已经注册的全部内容
			List<String> registryInfoInRedis = RedisApi.lrange(ref);
			List<String> newRegistry = new ArrayList<String>();

			boolean isOld = false;
			for (String node : registryInfoInRedis) {
				JSONObject joJsonObject = JSONObject.parseObject(node);
				if (joJsonObject.containsKey(ipportStr)) {
					newRegistry.add(ipport.toJSONString());
					isOld = true;
				} else {
					newRegistry.add(node);
				}
			}

			if (isOld) {
				// 删除Redis中已经存在的注册信息
				if (newRegistry.size() > 0) {
					RedisApi.del(ref);
					String[] newReStr = new String[newRegistry.size()];
					for (int i = 0; i < newRegistry.size(); i++) {
						newReStr[i] = newRegistry.get(i);
					}
					RedisApi.lpush(ref, newReStr);
				}
			} else {
				RedisApi.lpush(ref, ipport.toJSONString());
			}
		} else {
			// 第一次启动，redis中尚没有任何其他的主机注册的该服务信息
			RedisApi.lpush(ref, ipport.toJSONString());
		}
	}

	public List<String> getRegistry(String serviceId, ApplicationContext applicationContext) {
		try {
			Registry registry = applicationContext.getBean(Registry.class);
			RedisApi.createJedisPool(registry.getAddress());
			if (RedisApi.exists(serviceId)) {
				// 获取redis中注册的服务定义信息字符串
				return RedisApi.lrange(serviceId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
