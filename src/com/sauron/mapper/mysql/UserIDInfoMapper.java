package com.sauron.mapper.mysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.sauron.model.UserIDInfo;


@Service("idmapper")
public interface UserIDInfoMapper {
	
	void batchAddID(List<UserIDInfo> infos);
	
	void addID(UserIDInfo info);
	
	void deleteID(@Param("id") int id);
	
	void getIDSize();
	
	ArrayList<HashMap<String,Object>> getIDAtPage(@Param("offset") int offset, @Param("pagecount") int pagecount);
	
	ArrayList<HashMap<String,Object>>  getIDs();
	
	ArrayList<HashMap<String,Object>> getIDsBydateAndName(@Param("qdate") String qdate,@Param("qname") String qname,
			@Param("offset") int offset, @Param("pagecount") int pagecount);
	//查询黑白名单  黑名单:1,白名单:0
	ArrayList<HashMap<String,Object>> getIDsByBlacklist(@Param("blacklist") int blacklist);

}
