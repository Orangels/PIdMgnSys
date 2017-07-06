package com.sauron.mapper.mysql;

import com.sauron.model.blacklistInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;


@Service("blacklistmapper")
public interface blacklistInfoMapper {
    void addID(blacklistInfo info);
    void deleteID(@Param("idnum") String idNum);
    //查询黑白名单  黑名单:1,白名单:0
    ArrayList<HashMap<String,Object>> getIDsByBlacklist(@Param("blacklist") int isBlack);

}
