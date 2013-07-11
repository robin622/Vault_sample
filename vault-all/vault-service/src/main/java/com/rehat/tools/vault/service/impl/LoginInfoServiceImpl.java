package com.rehat.tools.vault.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.LoginInfo;
import com.redhat.tools.vault.dao.LoginInfoDAO;
import com.redhat.tools.vault.service.LoginInfoService;
import com.redhat.tools.vault.util.DateUtil;

public class LoginInfoServiceImpl implements LoginInfoService{

	private static final Logger log = Logger.getLogger(LoginInfoServiceImpl.class);
	
	@Inject
	private LoginInfoDAO loginInfoDAO;
	
	public JSONObject AddLoginInfo(String username, Date logintime) {
		JSONObject joReturn = new JSONObject();
		
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setUsername(username);
		loginInfo.setLogintime(DateUtil.getLocalUTCTime());
		
		List<LoginInfo> list = null;
		String loginid="";
		
		String message = "";
		
		try {
			
			loginInfoDAO.save(loginInfo);

			list = loginInfoDAO.get(loginInfo);
			if (list != null && list.size() > 0) {
				loginid = list.get(0).getLoginid().toString();
			}
			message = "Add loginInfo success!";
		}
		catch (Exception e) {
			message = "Add loginInfo failed!";
			log.error(e.getMessage());
		}
		finally {
			loginInfo.setLoginid(Long.parseLong(loginid));
			joReturn.put("message", message);
			joReturn.put("loginid", loginid);
		}
		
		return joReturn;		
		
	}

	/*public JSONObject staicsCount(String type) {
		JSONObject json = new JSONObject();
		int n=5;

		String dateType[]=new String[n];
		long[] resultDateUserNumber=new long[n];
	    Map<String, Long> resultUserNumber=null;
		
	    try{
	    	resultUserNumber=loginInfoDAO.countActiveUserNumber(type);
	    }catch(Exception e)
	    {
	    	log.error(e.getMessage(),e);
	    }
	    Set setUserNumber=resultUserNumber.keySet();
	    Iterator itUserNumber = setUserNumber.iterator();
	    int userNumber=0;
	    
	    while (itUserNumber.hasNext())
        {
        	String keyUserNumber = (String) itUserNumber.next();        	
        	Long valueUserNumber = (Long) resultUserNumber.get(keyUserNumber);    
        	dateType[userNumber]=keyUserNumber;
        	resultDateUserNumber[userNumber]=valueUserNumber;
        	userNumber++;

        }
	    json.put("dateType", dateType);
        json.put("resultUserNumber", resultDateUserNumber);
		return json;
	}
	
	public String[] teamCount()
	{
		//JSONObject json = new JSONObject();
		int n=2;
		String[] resultDateTwoMonth=new String[n];
		Map<String,String> resultTwoMonth=null;
		
		try{
        	resultTwoMonth=loginInfoDAO.userTwoMonth();
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
		 Set setTwoMonth=resultTwoMonth.keySet();
		 Iterator itTwoMonth=setTwoMonth.iterator();
		 int twoMonth=0;
		 
		 while (itTwoMonth.hasNext())
	     {
	        String keyTwoMonth = (String) itTwoMonth.next();
	        String valueTwoMonth = (String) resultTwoMonth.get(keyTwoMonth);
	        resultDateTwoMonth[twoMonth]=valueTwoMonth;	        	
	        twoMonth++;
	       }		 
		 return resultDateTwoMonth;
		 
	}*/
	
	
}
