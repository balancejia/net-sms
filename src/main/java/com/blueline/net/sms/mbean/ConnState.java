package com.blueline.net.sms.mbean;

import com.blueline.net.sms.manager.EndpointConnector;
import com.blueline.net.sms.manager.EndpointEntity;
import com.blueline.net.sms.manager.EndpointManager;
import com.blueline.net.sms.manager.ServerEndpoint;
import com.blueline.net.sms.session.cmpp.SessionStateManager;
import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

public class ConnState implements ConnStateMBean {
	@Override
	public String print(String entityId) {
		 StringBuilder sb = new StringBuilder();
		EndpointManager em = EndpointManager.INS;
		if(StringUtils.isEmpty(entityId)){
			Set<EndpointEntity> enlist = em.allEndPointEntity();
			for(EndpointEntity e : enlist){
				sb.append(e.getId()+":\n");
				sb.append(printOne(e));
			}
		}else{
			EndpointEntity e = em.getEndpointEntity(entityId);
			if(e!=null){
				sb.append(e.getId()+":\n");
				sb.append(printOne(e));
			}
		}
		return sb.toString();
	}

	private String printOne(EndpointEntity e){
		 StringBuilder sb = new StringBuilder();
		EndpointConnector econn = EndpointManager.INS.getEndpointConnector(e);
		
		if(econn == null) return ""; 
			
		 Channel[] carr = econn.getallChannel();
		 if(carr!=null && carr.length>0){
			 
			 for(int i= 0;i<carr.length;i++){
				 Channel ch = carr[i];
				 SessionStateManager ssm = (SessionStateManager)ch.pipeline().get("sessionStateManager");
				 sb.append("\tch[");
				 sb.append(ch.localAddress().toString());
				 
				 if(e instanceof ServerEndpoint){
					 sb.append("<-");
				 }else{
					 sb.append("->");
				 }
				 sb.append(ch.remoteAddress().toString() +"]");
				 sb.append("\tWaitting-resp=").append(ssm.getWaittingResp());
				 sb.append("\tWriteCount=").append(ssm.getWriteCount());
				 sb.append("\tReadCount=").append(ssm.getReadCount());
				 sb.append("\n");
			 }
		 }
		 return sb.toString();
	}
}
