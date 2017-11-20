package com.dongnao.jack.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.dongnao.jack.loadbalance.NodeInfo;

/**
 * 定义了RMI方式的 服务端和客户端的启动方法
 * 
 * @author liugaowei
 *
 */
public class RmiUtil {

	public void startRmiServer(String host, String Port, String id) {
		try {
			SoaRmi soaRmi = new SoaRmiImpl();
			LocateRegistry.createRegistry(Integer.valueOf(Port));

			// rmi://localhost:1135/servicepath
			String rmiServiceName = "rmi://" + host + ":" + Port + "/" + id;
			Naming.bind(rmiServiceName, soaRmi);
			System.out.println("RMI Server Started! rmiServiceName= " + rmiServiceName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SoaRmi startRmiClient(NodeInfo nodeInfo, String id) {
		try {
			String host = nodeInfo.getHost();
			String port = nodeInfo.getPort();
			String rmiServiceName = "rmi://" + host + ":" + port + "/" + id;
			return (SoaRmi) Naming.lookup(rmiServiceName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
