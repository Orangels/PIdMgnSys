package com.sauron.service;

import com.sauron.model.blacklistInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sauron.mapper.mysql.blacklistInfoMapper;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

@Service("blacklistservice")
@Transactional
public class blacklistService {

    private static Logger logger = Logger.getLogger(UserIDService.class);

    @Resource(name="blacklistmapper")
    private blacklistInfoMapper mapper;

    public void addID(blacklistInfo id){
        if(id!=null){
            logger.debug("add ID info: "+id.getIdnum());
            mapper.addID(id);
        }
    }

    public void deleteID(String idnum){
        logger.debug("delete ID info at id="+idnum);
        mapper.deleteID(idnum);
    }

    //查询黑白名单,黑名单1,白名单0
    public ArrayList<HashMap<String,Object>> getIDsByBlacklist(int blacklist) throws ParseException {
        ArrayList<HashMap<String,Object>> result = mapper.getIDsByBlacklist(blacklist);

        return result;
    }


}
