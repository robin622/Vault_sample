package com.redhat.tools.vault.dao;

import java.math.BigInteger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.LoginInfo;


public class LoginInfoDAO {

	    protected static final Logger log = Logger.getLogger(LoginInfoDAO.class);

	    DAOFactory dao = null;
	    Session session = null;
	    
	    
	    private static final String QUERY_USERSTATICREQ="from VA_loginInfo as a where a.logintime like ?";
	    private static final String QUERY_USERSTATICMONTH="from VA_loginInfo as a where a.logintime between ? and ? or a.logintime like ? or a.logintime like ?";
	    public LoginInfoDAO() {
	        dao = DAOFactory.getInstance();
	    }
	   
	    public List<LoginInfo> get(LoginInfo condition) {
	        Session sess = null;
	        try {
	            sess = dao.getSession();
	            Criteria criteria = sess.createCriteria(LoginInfo.class);
	            if (condition.getLoginid() != null) {
	                criteria.add(Expression.eq(LoginInfo.PROPERTY_LOGINID, new Long(condition.getLoginid())));
	            }
	            if (condition.getUsername() != null) {
	                criteria.add(Expression.eq(LoginInfo.PROPERTY_USERNAME , condition.getUsername()));
	            }
	            if (condition.getLogintime() != null) {
	                criteria.add(Expression.eq(LoginInfo.PROPERTY_LOGINTIME, condition.getLogintime()));
	            }
	            
	            criteria.addOrder(Order.desc((LoginInfo.PROPERTY_LOGINID)));
	            List<LoginInfo> list = criteria.list();
	            if (list != null && list.size() > 0) {
	                for (LoginInfo s : list) {
	                    log.debug("LoginInfo=" + s);
	                }
	            }

	            return list;
	        }catch (Exception e) {
	            log.error(e.getMessage());
	        } finally {
	            if (sess != null) {
	                sess.close();
	            }
	        }
	        return null;
	        }


	    public Long save(LoginInfo loginInfo) throws Exception {
			Transaction trans = null;
			Long id = null;
			try {
				session = dao.getSession();
				id = (Long) session.save(loginInfo);
				trans = null;
			}
			catch (HibernateException e) {
				log.error(e.getMessage());
				throw e;
			}
			catch (IllegalStateException ie) {
				log.error(ie.getMessage());
			}
			finally {
				try {
					if (trans != null) {
						trans.rollback();
					}
					dao.closeSession(session);
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
			}
			return id;
		}
	
	    /*   
	  public  Map<String, BigInteger> countActiveUserNumber(String type)
	    {
		    int n=5;
		    Calendar cl = null;
			Date date[]=new Date[n+1];
			SimpleDateFormat dday = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dmonth = new SimpleDateFormat("yyyy-MM");
			String queryString[]=new String[n];
			Query query[]=new Query[n]; 
			String dateType[]=new String[n];
		    String day[]=new String[n+1];
	    	Map<String, BigInteger> countsUser= new TreeMap<String, BigInteger>();
	    	Session sess=null;
	    	BigInteger sum[]=new BigInteger[n];
			try {
				sess = dao.getSession();
			} catch (Exception e) {
				log.error("Create session failed", e);
			}
			if(type.equals("day")||type.equals("month"))
			{
			   for(int i=0;i<n;i++)
	    	   {
				   cl = Calendar.getInstance();
				   if(type.equals("day"))
				    {
					    cl.add(Calendar.DATE, -i);
					    date[i]=cl.getTime();
					    dateType[i] = dday.format(date[i]);
				   }else if(type.equals("month"))
				    {
					    cl.add(Calendar.MONTH, -i);	
		    		    date[i]=cl.getTime();
		    		    dateType[i] = dmonth.format(date[i]);
				    }
		            queryString[i] = "select count(*) from (select a.username "+QUERY_USERSTATICREQ+" group by a.username) as user";
		            System.out.println(queryString[i]);
	    	        query[i] = sess.createSQLQuery(queryString[i]);
	    	        query[i].setString(0, dateType[i] + "%");
	    	        List<BigInteger> temp = query[i].list();
	    	        sum[i]=new BigInteger("0");
	                if (temp != null && temp.size() > 0) {
	            	    sum[i]=temp.get(0);
	            	    countsUser.put(dateType[i], sum[i]);            
	              } else {
	                    sum[i]=new BigInteger("0");
	                    countsUser.put(dateType[i], sum[i]);
	            }

	    	}
			}
		
			else if(type.equals("week"))
			{
				cl = Calendar.getInstance();
				date[0]=cl.getTime();
			  	day[0] = dday.format(date[0]);
			  	for(int i=1;i<=n;i++)
			  	{
			  		cl = Calendar.getInstance();
			  		int daySub=(i-1)*7+cl.get(Calendar.DAY_OF_WEEK);
			  		cl.add(Calendar.DATE, -(daySub-1));
			  		
			  		date[i]=cl.getTime();
			  		day[i] = dday.format(date[i]);
			  		dateType[i-1]="week"+cl.get(Calendar.WEEK_OF_YEAR);
			  		queryString[i-1] = "select count(*) from (select a.username "+QUERY_USERSTATICMONTH+" group by a.username) as user";
			  	    query[i-1] = sess.createSQLQuery(queryString[i-1]);
			  	    query[i-1].setString(0, day[i]);
			  	    query[i-1].setString(1, day[i-1]);
			  	    query[i-1].setString(2, day[i] + "%");
			  	    query[i-1].setString(3, day[i-1] + "%");
			  	    List<BigInteger> temp = query[i-1].list();
			  	    sum[i-1]=new BigInteger("0");
			          if (temp != null && temp.size() > 0) {
			          	sum[i-1]=temp.get(0);
			          	countsUser.put(dateType[i-1], sum[i-1]);
			             
			          } else {
			             sum[i-1]=new BigInteger("0");
			             countsUser.put(dateType[i-1], sum[i-1]);
			          }
			         
			  	}
			}
	    	return countsUser;
	    }
	  
	  public Map<String,String> userTwoMonth()
	  {
		  Map<String,String> countUserThisMonth=new TreeMap<String,String>();
		  Calendar cl = Calendar.getInstance();
	  	  Date date[]=new Date[31];
	  	  SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
	      SimpleDateFormat dday = new SimpleDateFormat("dd");
	  	  SimpleDateFormat ddmonth = new SimpleDateFormat("yyyy-MM");
	  	  int whichDay =Integer.parseInt(dday.format(cl.getTime())); 
	  	  String day[]=new String[31];
	  	  Date LastMonth=new Date();
	  	
	  	  String month=null;
	  	  String thisMonth="";
	  	  String lastMonth="";
	  	  Session sess=null;
	  	  String queryString[]=new String[31];
	  	  Query query[]=new Query[31];
	  	  String[] sumEveryDay=new String[31];
	  	  try {
			sess = dao.getSession();
		  } catch (Exception e) {
			log.error("Create session failed", e);
		  }	
	  	  
		  for(int i=0;i<whichDay;i++)
		  {
			  cl = Calendar.getInstance();
	  		  cl.add(Calendar.DATE, -i);
	  		  date[i]=cl.getTime();
	  		  day[i] = dd.format(date[i]);
	  		  queryString[i] = "select a.username "+QUERY_USERSTATICREQ+" group by a.username";	
 	          query[i] = sess.createSQLQuery(queryString[i]);
 	          query[i].setString(0, day[i] + "%");
		      List<String> temp = query[i].list();
	  	      sumEveryDay[i]="";
	  	    
	  	      for(int j=0;j<temp.size();j++)
	  	      {
	  	    	sumEveryDay[i]=sumEveryDay[i]+temp.get(j)+"\n";    	
	  	      }
	  	    thisMonth=thisMonth+sumEveryDay[i];
		      
		  }
		  countUserThisMonth.put("thisMonth", thisMonth);
		  
		  cl = Calendar.getInstance();
		  cl.add(Calendar.MONTH, -1);
		  LastMonth=cl.getTime();
			
		  month=ddmonth.format(LastMonth);
		  int numberOfDay=cl.getActualMaximum(Calendar.DAY_OF_MONTH);
		  for(int k=0;k<numberOfDay;k++)
		  {
		    if((numberOfDay-k)>=10&&(numberOfDay-k)<=31)
		    {
	  		  day[k] = month+"-"+(numberOfDay-k);
		    }
		    else if((numberOfDay-k)>0&&(numberOfDay-k)<=9)
		    {
		      day[k] = month+"-0"+(numberOfDay-k);
		    }
		    queryString[k] = "select a.username "+QUERY_USERSTATICREQ+" group by a.username";	
	         query[k] = sess.createSQLQuery(queryString[k]);
	         query[k].setString(0, day[k] + "%");
		    List<String> temp = query[k].list();
		    sumEveryDay[k]="";
		    
		    for(int j=0;j<temp.size();j++)
		    {
		    	sumEveryDay[k]=sumEveryDay[k]+temp.get(j)+"\n";      	
		      }
		    lastMonth=lastMonth+sumEveryDay[k];
			}
		  countUserThisMonth.put("lastMonth", lastMonth);
		  return countUserThisMonth;
	  }
	  */
	  
	  
}
