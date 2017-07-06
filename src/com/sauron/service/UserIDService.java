package com.sauron.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sauron.mapper.mysql.UserIDInfoMapper;
import com.sauron.model.UserIDInfo;
import org.springframework.transaction.annotation.Transactional;


@Service("idservice")
@Transactional
public class UserIDService {

	private static Logger logger = Logger.getLogger(UserIDService.class);
	
	@Resource(name="idmapper")
	private UserIDInfoMapper mapper;
	
	public void batchAdd(ArrayList<UserIDInfo> ids){
		
		if(ids!=null && ids.size()>0){
			logger.debug("batch add ids with size: "+ids.size());
			mapper.batchAddID(ids);
		}
		
	}
	
	public void addID(UserIDInfo id){
		if(id!=null){
			logger.debug("add ID info: "+id.getIdbaseinfo());
			mapper.addID(id);
		}
	}
	
	public void deleteID(int id){
		logger.debug("delete ID info at id="+id);
		mapper.deleteID(id);
	}
	
	public ArrayList<HashMap<String,Object>> getIDAtPage(int pageindex,int pagecount){
		ArrayList<HashMap<String,Object>> result = mapper.getIDAtPage(pageindex*pagecount,pagecount);
		for(HashMap<String,Object> map : result){
			String idbaseinfo = (String)map.get("idbaseinfo");
			String[] infos = idbaseinfo.split("#");
			if(infos.length>2){
				map.put("visitorname", infos[0]);
				map.put("gentor", infos[1]);
			}
		}
		return result;
	}
	
	public ArrayList<HashMap<String,Object>> getIdsByCondition(String qname,String qdate,int pageindex,int pagecount) throws ParseException{
		if(qdate!=null) qdate = new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("yyyy-MM-dd").parse(qdate));
		ArrayList<HashMap<String,Object>> result = mapper.getIDsBydateAndName(qdate, qname,pageindex*pagecount,pagecount);
		for(HashMap<String,Object> map : result){
			String idbaseinfo = (String)map.get("idbaseinfo");
			String[] infos = idbaseinfo.split("#");
			if(infos.length>2){
				map.put("visitorname", infos[0]);
				map.put("gentor", infos[1]);
			}
		}
		return result;
	}
	//查询黑白名单,黑名单1,白名单0
	public ArrayList<HashMap<String,Object>> getIDsByBlacklist(int blacklist) throws ParseException{
		ArrayList<HashMap<String,Object>> result = mapper.getIDsByBlacklist(blacklist);
		for(HashMap<String,Object> map : result){
			String idbaseinfo = (String)map.get("idbaseinfo");
			String[] infos = idbaseinfo.split("#");
			if(infos.length>2){
				map.put("visitorname", infos[0]);
				map.put("gentor", infos[1]);
			}
		}
		return result;
	}

}
