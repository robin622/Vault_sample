package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.Set;

import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.service.LoginInfoService;
import com.redhat.tools.vault.service.RequestService;
import com.redhat.tools.vault.web.orgchart.OrgChartDataService;

@WebServlet("/StaticsServlet")
public class StaticsServlet extends HttpServlet{

	   private static final long serialVersionUID = 1L;
		
    	private static Logger log = Logger.getLogger(StaticsServlet.class);
		    @Inject
		    private RequestService service; 
		    @Inject
		    private OrgChartDataService dataService;
		    
		    @Inject
		    private LoginInfoService loginService;
		    
		    public void init() {
		    	members=dataService.getAllMembers(leader[0]);
		    	if(members.isEmpty())
				{
					dataService.loadOrgChartUsers();
				}
		    }

		    private Set<String> members;
		    private String[] groups={"Cloud DEV","Cloud QE","CS (Content Service)","Executive Assistant Of Paul. C",
		    		"Exec PPL Executive",
		    		"Gluster MKT","Hardware Certification","HSS","Internationalization(I18N)","GSS Operations",
		    		"Middleware DEV","Middleware MKT","Middleware QE",
		    		"MKT Management & Security",
		    		"Openshift DEV","Performance","Platform DEV",
		    		"Platform MKT","Platform QE","RCM (Release Management)","Sales Management","Security Response Team",
		    		"Software Certification","Storage DEV","Storage QE","Systems Engineering DEV",
		    		"Virtualization MKT","VP,Technical Business Development",
		    		"others"};
		    
		    private String[] leader={"tburke","jturner","mhideo","ddelegge",
		    		"cpeters","delisa","jyeaney","mrc",
                    "srangach","yshao","llim","petersen","mbillpet",
                    "mlittle","cmuzilla","mharvey",
                    "abadani","bche","mferris","mrimmler",	
		    		"mhicks","dshaks","ddumas",
		    		"jtotton","benl","jflanagan","oberoi","mjc",
		    		"smohan","rfortier","sdharane","vtrehan",
		    		"rbalakri","mevans",	    		
		    		};
		   

		    public Set<String> getMembers() {
				return members;
			}

			public void setMembers(Set<String> members) {
				this.members = members;
			}
			
			private static String[] getTokens(String input,String delimiters){

			    int i = 0;
			    StringTokenizer st = new StringTokenizer(input,delimiters);
			    int numTokens = st.countTokens();
			    String[] tokenList = new String[numTokens];
			    while (st.hasMoreTokens()){
			        tokenList[i] = st.nextToken();
			        i++;
			    }
			    return(tokenList);
			  }

			/**
		     * @see HttpServlet#HttpServlet()
		     */
		    public StaticsServlet() {
		    }

			/**
			 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
			 */
			protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				doPost(request,response);
			}

			/**
			 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
			 */
			protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				response.setContentType("application/json;charset=UTF-8");
				response.setHeader("Cache-Control", "no-chche");			
				
				String[] types={"day","week","month"};		
				JSONObject joReturn=new JSONObject();
				
				String[] userTwoMonth=service.teamCount();
				String lastMonth=userTwoMonth[0];
				//System.out.println("String thisMonth"+lastMonth);
				String thisMonth=userTwoMonth[1];
				//System.out.println("String lastMonth"+thisMonth);

				String[] activeUserThisMonth=getTokens(thisMonth,"\n");
				String[] activeUserLastMonth=getTokens(lastMonth,"\n");
				//System.out.println("activeUserThisMonth length="+activeUserThisMonth.length);
			   // System.out.println("activeUserLastMonth length="+activeUserLastMonth.length);
				
			    int n=leader.length;
			    int numGroups=groups.length;
			    int[] userNumberThisMonth=new int[n+1];
			    int[] userNumberLastMonth=new int[n+1];
			    int[] userGroupThisMonth=new int[numGroups];
			    int[] userGroupLastMonth=new int[numGroups];
			    
				for(int i=0;i<types.length;i++)
				{
				    joReturn.put(types[i], service.staicsCount(types[i]));	
				}
				
				
				for(int j=0;j<leader.length;j++)
				{
					//System.out.println("learder["+j+"]="+leader[j]);
					members=dataService.getAllMembers(leader[j]);
					
					//System.out.println("members.size="+members.size());
					userNumberThisMonth[j]=0;
					userNumberLastMonth[j]=0;
					
					for(int i=0;i<activeUserThisMonth.length;i++)
					{	
						 if(members.contains(activeUserThisMonth[i]))
						    {
							    userNumberThisMonth[j]++;
							    activeUserThisMonth[i]="";						    	
						    }
					}
					
					//System.out.println("userNumberThisMonth["+j+"]="+userNumberThisMonth[j]);
					for(int i=0;i<activeUserLastMonth.length;i++)
					{				
						 if(members.contains(activeUserLastMonth[i]))
						    {
							    userNumberLastMonth[j]++;
							    activeUserLastMonth[i]="";
						    }
					}
					//System.out.println("userNumberLastMonth["+j+"]="+userNumberLastMonth[j]);
					
				}
				userNumberThisMonth[leader.length]=0;
				userNumberLastMonth[leader.length]=0;
				for(int i=0;i<activeUserThisMonth.length;i++)
				{
					 
				     if(!(activeUserThisMonth[i].equals("")))
				     {
				    	 userNumberThisMonth[leader.length]++;
				     }
				}
				//System.out.println("userNumberThisMonth["+leader.length+"]="+userNumberThisMonth[leader.length]);
				
				for(int i=0;i<activeUserLastMonth.length;i++)
				{
					 
				     if(!(activeUserLastMonth[i].equals("")))
				     {
				    	 userNumberLastMonth[leader.length]++;
				     }
				}
				//System.out.println("userNumberLastMonth["+leader.length+"]="+userNumberLastMonth[leader.length]);
				
				for(int i=0;i<groups.length;i++)
				{
					userGroupThisMonth[i]=0;
					userGroupLastMonth[i]=0;
				}
		        for(int i=0;i<=3;i++)
		        {
		        	userGroupThisMonth[i]=userNumberThisMonth[i];
		        	userGroupLastMonth[i]=userNumberLastMonth[i];
		        }
		        for(int i=4;i<=7;i++)
		        {
		            userGroupThisMonth[4]+=userNumberThisMonth[i];
		            userGroupLastMonth[4]+=userNumberLastMonth[i];
		        }
		        for(int i=8;i<=15;i++)
		        {
		        	userGroupThisMonth[i-3]=userNumberThisMonth[i];
		        	userGroupLastMonth[i-3]=userNumberLastMonth[i];
		        }
		        for(int i=16;i<=19;i++)
		        {
		        	userGroupThisMonth[13]+=userNumberThisMonth[i];
		            userGroupLastMonth[13]+=userNumberLastMonth[i];
		        }
		        
		        for(int i=20;i<=33;i++)
		        {
		        	userGroupThisMonth[i-6]=userNumberThisMonth[i];
		        	userGroupLastMonth[i-6]=userNumberLastMonth[i];
		        }
		        userGroupThisMonth[28]=userNumberThisMonth[34];
		        userGroupLastMonth[28]=userNumberLastMonth[34];
//		        for(int i=0;i<groups.length;i++)
//		        {
//		        	System.out.println(groups[i]+"  userGroupThisMonth "+userGroupThisMonth[i]);
//		        	System.out.println(groups[i]+"  userGroupLastMonth "+userGroupLastMonth[i]);
//		        }
//			
				joReturn.put("groups", groups);
				joReturn.put("userGroupThisMonth", userGroupThisMonth);
				joReturn.put("userGroupLastMonth", userGroupLastMonth);
				response.getWriter().print(joReturn);
							
			}
}
