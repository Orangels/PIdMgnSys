package com.sauron.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sauron.model.blacklistInfo;
import com.sauron.service.HttpRequest;
import com.sauron.service.blacklistService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sauron.model.UserIDInfo;
import com.sauron.service.UserIDService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Controller
public class UIDController{

	private static Logger logger = Logger.getLogger(UIDController.class);
	private static String oriToken = null;
	private static long heartTime = 30;

	private static final Map<String, Future> futures = new HashMap<String, Future>();
	private static final String jobID = "heartBeat";

	@Resource(name="idservice")
	private UserIDService service;

	@Resource(name = "blacklistservice")
	private blacklistService blackService;

	//登录警综平台
	@RequestMapping(value="/login")
	public void login(@RequestParam(required=true) String uid,@RequestParam(required=true)String pwd,
					  HttpServletRequest request
			,HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String msg = null;
		String token = null;

		if (oriToken == null || oriToken.isEmpty()){
			System.out.println("首次登录");
		}else{
			//退出登录
			String destoryUrl = "http://gather.is11.com:9999/gather/webservice.asmx/token_destroy";
			String destoryParam = "token="+oriToken+"&userstate=1";
			String destoryResult = HttpRequest.sendPost(destoryUrl,destoryParam);

			try {
				Document doc = DocumentHelper.parseText(destoryResult);
				Element root = doc.getRootElement();
				Element res = root.element("res");
				Element errcodeNode = res.element("errcode");
				String errcode = errcodeNode.getText();
				if (!errcode.equals("0")){

					System.out.println("退出失败,退出任务");
					msg = "1";
					response.setCharacterEncoding("UTF-8");
					response.getWriter().print(msg);
					return;

				}else {
					System.out.println("退出成功,登录新的账号");
					//这里也要做 future 的取消,防止同一个账号登录,发送多次心跳包
					Future future = futures.get(jobID);
					if (future != null) future.cancel(true);
					System.out.println("取消前一个账号的心跳");
				}
			}catch (DocumentException e){
				e.printStackTrace();
			}
		}

		try {
			//注册 token
			String url = "http://gather.is11.com:9999/gather/webservice.asmx/token";
			String param = "uid=" + uid + "&pwd=" + pwd + "&userstate=1";
			String result = HttpRequest.sendPost(url, param);

			Document doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			Element res = root.element("res");
			Element tokenNode = res.element("token");
			token = tokenNode.getText();
			oriToken = token;
			//定义 心跳请求参数
			final String heartUrl = "http://gather.is11.com:9999/gather/webservice.asmx/heartbeat";
			final String heartParam = "token="+token+"&userstate=1";

			if (token == null || token.isEmpty()) {
				//登录失败
				msg = "1";
			} else {
				//登录成功
				msg = "0";
				//30s 发送一次心跳包 异步线程

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
				Future future = executor.scheduleWithFixedDelay(new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("发送心跳包");
							String heartResult = HttpRequest.sendPost(heartUrl,heartParam);

							Document heartDoc = DocumentHelper.parseText(heartResult);
							Element heartRoot = heartDoc.getRootElement();
							Element heartRes = heartRoot.element("res");
							Element errcodeNode = heartRes.element("errcode");
							String errcode = errcodeNode.getText();
							if (!errcode.equals("0")){
								//心跳错误 取消任务
								Future future = futures.get(jobID);
								if (future != null) future.cancel(true);
								System.out.println("发送心跳失败,取消任务");
							}
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				},20,heartTime, TimeUnit.SECONDS);
				futures.put(jobID, future);
			}
		}
		catch (Exception e) {
			msg = e.toString();
			logger.error(e);
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(msg);
	}


	//黑名单查询 blacklist: 1: 黑名单, 0: 白名单
	@RequestMapping(value="/blackList")
	public void blackList(@RequestParam String blacklist, HttpServletRequest request
			,HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 这里做 数据库 取出 blackList 的逻辑处理
		String msg = null;
        String errmsg = null;
        ArrayList<HashMap<String,Object>> ids = null;

        int jBlacklist = Integer.parseInt(new String(blacklist.getBytes("ISO-8859-1"),"UTF-8"));

        try{
            ids = blackService.getIDsByBlacklist(jBlacklist);
        }catch(Exception e){
            errmsg =e.toString();
            logger.error(e);
        }

        response.setContentType("text/html;charset=GBK");
        try{
            if(errmsg!=null){
                JSONObject obj = new JSONObject();
                obj.put("error", errmsg);
                response.getWriter().print(obj.toJSONString());
            }else{
                response.getWriter().print(JSONArray.toJSONString(ids));
            }
        }catch(Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }

	}

	//黑白名单 删除
	@RequestMapping(value="/blackDelete")
	public void blackDelete(@RequestParam(required=true) String idNum, HttpServletRequest request
			,HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 这里做 数据库 删除  的逻辑处理
		String msg = null;

		try {
			blackService.deleteID(idNum);
            msg = "0";
		}catch (Exception e){
            msg = e.toString();
            logger.error(e);
		}

		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(msg);
	}


    //黑白名单 添加 isblack : 1 黑名单  0:白名单
    @RequestMapping(value="/blackInsert")
    public void blackInsert(@RequestParam(required=true) String name,
                            @RequestParam(required=true) String gender,
                            @RequestParam(required=true) String idnum,
                            @RequestParam(required=true) String isblack,
                            HttpServletRequest request
            ,HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub


        String msg = null;
        int Jisblack = 0;
        String contentType = request.getContentType();


        //添加的 黑名单转换
        if (isblack == null || isblack.isEmpty()){
            // 默认值0,白名单
        }else {
            Jisblack = Integer.parseInt(new String(isblack.getBytes("ISO-8859-1"),"UTF-8"));
        }

		if (contentType.indexOf("UTF-8")!=-1){
            // utf-8 格式
        }else{
            name = new String(name.getBytes("ISO-8859-1"),"UTF-8");
            gender = new String(gender.getBytes("ISO-8859-1"),"UTF-8");
            idnum = new String(idnum.getBytes("ISO-8859-1"),"UTF-8");
        }



        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String visitdate = sdf.format(date);

        blacklistInfo blackPerson = new blacklistInfo();
        blackPerson.setIsblack(Jisblack);
        blackPerson.setBlackdate(visitdate);
        blackPerson.setBlackname(name);
        blackPerson.setGender(gender);
        blackPerson.setIdnum(idnum);

        try{
            blackService.addID(blackPerson);
            msg = "0";
        }catch(Exception e){
            msg = e.toString();
            logger.error(e);
        }

        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(msg);
    }




	@RequestMapping(value="/datasync")
	public void datasync(@RequestParam(required=true) String cidbaseinfo,@RequestParam(required=true)String cidpicinfo,
						 @RequestParam(required=true)String similar,@RequestParam String blacklist,
						 HttpServletRequest request
			,HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String msg = null;
        int jBlacklist = 0;

		String jCidbaseinfo = new String(cidbaseinfo.getBytes("ISO-8859-1"),"UTF-8");
		String jCidpicinfo = new String(cidpicinfo.getBytes("ISO-8859-1"),"UTF-8");
		double jSimilar = Double.parseDouble(new String(similar.getBytes("ISO-8859-1"),"UTF-8"));
		//添加的 黑名单转换
        if (blacklist == null || blacklist.isEmpty()){
            // 默认值0,白名单
        }else {
            jBlacklist = Integer.parseInt(new String(blacklist.getBytes("ISO-8859-1"),"UTF-8"));
        }



		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String visitdate = sdf.format(date);

		UserIDInfo uid = new UserIDInfo();
		uid.setIdbaseinfo(jCidbaseinfo);
		uid.setIdpicinfo(jCidpicinfo);
		uid.setSimilar(jSimilar);
		uid.setVisitdate(visitdate);
		uid.setBlacklist(jBlacklist);

		try{
			service.addID(uid);
			msg = "0";
		}catch(Exception e){
			msg = e.toString();
			logger.error(e);
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(msg);
	}

	@RequestMapping(value="/queryid")
	public void queryId(@RequestParam String qdate,
						@RequestParam String qname,
						@RequestParam(required=true) int pageindex,
						@RequestParam(required=true) int pagecount,
						HttpServletRequest request,
						HttpServletResponse response){

		String errmsg = null;
		ArrayList<HashMap<String,Object>> ids = null;
		try{
			if(qname!=null&&(qname.trim().toUpperCase().equals("UNDEFINDED")||
					qname.trim().equals(""))) qname=null;
			if(qdate!=null&&(qdate.trim().toUpperCase().equals("UNDEFINDED")||
					qdate.trim().equals(""))) qdate=null;
			if(qname==null&&qdate==null){
				ids = service.getIDAtPage(pageindex, pagecount);
			}else{
				ids = service.getIdsByCondition(qname, qdate,pageindex, pagecount);
			}
		}catch(Exception e){
			errmsg =e.toString();
			logger.error(e);
		}

		response.setContentType("text/html;charset=GBK");
		try{
			if(errmsg!=null){
				JSONObject obj = new JSONObject();
				obj.put("error", errmsg);
				response.getWriter().print(obj.toJSONString());
			}else{
				response.getWriter().print(JSONArray.toJSONString(ids));
			}
		}catch(Exception e){
			logger.error(e.toString());
			e.printStackTrace();
		}

	}
}
