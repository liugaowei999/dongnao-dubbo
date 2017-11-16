package com.dongnao.jack.loadbalance;

import java.util.List;

public interface LoadBalance {
	public NodeInfo select(List<String> registryInfo);
}
