package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.Set;

import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.jboss.logging.Logger;
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
		    
		    @PostConstruct
		    public void init() {
		        dataService.toString();
		    }

		    private Set<String> members;
		    private String[] groups={"Cloud DEV","Middleware DEV","Openshift DEV","Platform DEV","Storage DEV",
		    		"Performance","Systems Engineering DEV","Storage QE","Cloud QE","Middleware QE","Platform QE",
		    		"HSS","RCM (Release Management)","CS (Content Service)","Internationalization(I18N)",
		    		"Security Response Team","Software Certification","Hardware Certification","Platform MKT","Middleware MKT",
		    		"Virtualization MKT","Gluster MKT","GSS Operations","Sales Management",
		    		"MKT Management & Security",		    		
		    		"Exec PPL Executive",
		    		"others"};
		    
		    private String[] leader={"tburke","mlittle","mhicks","ddumas","rfortier",
		    		"dshaks","vtrehan","sdharane","jturner","mharvey","benl",
		    		"llim","jflanagan","mhideo","petersen",
		    		"mjc","smohan","yshao","jtotton","cmuzilla",
		    		"rbalakri","srangach","mbillpet","oberoi",
		    		"abadani","bche","mferris","mrimmler",	    		
		    		"cpeters","delisa","jyeaney","mrc"};
		   

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
				System.out.println("String thisMonth"+lastMonth);
				String thisMonth=userTwoMonth[1];
				System.out.println("String lastMonth"+thisMonth);

				String[] activeUserThisMonth=getTokens(thisMonth,"\n");
				String[] activeUserLastMonth=getTokens(lastMonth,"\n");
				System.out.println("activeUserThisMonth length="+activeUserThisMonth.length);
			    System.out.println("activeUserLastMonth length="+activeUserLastMonth.length);
				
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
					System.out.println("learder["+j+"]="+leader[j]);
					members=dataService.getAllMembers(leader[j]);
					System.out.println("members.size="+members.size());
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
					
					System.out.println("userNumberThisMonth["+j+"]="+userNumberThisMonth[j]);
					for(int i=0;i<activeUserLastMonth.length;i++)
					{				
						 if(members.contains(activeUserLastMonth[i]))
						    {
							    userNumberLastMonth[j]++;
							    activeUserLastMonth[i]="";
						    }
					}
					System.out.println("userNumberLastMonth["+j+"]="+userNumberLastMonth[j]);
					
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
				System.out.println("userNumberThisMonth["+leader.length+"]="+userNumberThisMonth[leader.length]);
				
				for(int i=0;i<activeUserLastMonth.length;i++)
				{
					 
				     if(!(activeUserLastMonth[i].equals("")))
				     {
				    	 userNumberLastMonth[leader.length]++;
				     }
				}
				System.out.println("userNumberLastMonth["+leader.length+"]="+userNumberLastMonth[leader.length]);
				
				for(int i=0;i<groups.length;i++)
				{
					userGroupThisMonth[i]=0;
					userGroupLastMonth[i]=0;
				}
		        for(int i=0;i<=23;i++)
		        {
		        	userGroupThisMonth[i]=userNumberThisMonth[i];
		        	userGroupLastMonth[i]=userNumberLastMonth[i];
		        }
		        for(int i=24;i<=27;i++)
		        {
		            userGroupThisMonth[24]+=userNumberThisMonth[i];
		            userGroupLastMonth[24]+=userNumberLastMonth[i];
		        }
		        for(int i=28;i<=31;i++)
		        {
		        	userGroupThisMonth[25]+=userNumberThisMonth[i];
		            userGroupLastMonth[25]+=userNumberLastMonth[i];
		        }
		        
		        
		        userGroupThisMonth[26]=userNumberThisMonth[32];
		        userGroupLastMonth[26]=userNumberLastMonth[32];
		        for(int i=0;i<groups.length;i++)
		        {
		        	System.out.println(groups[i]+"  userGroupThisMonth "+userGroupThisMonth[i]);
		        	System.out.println(groups[i]+"  userGroupLastMonth "+userGroupLastMonth[i]);
		        }
			
				joReturn.put("groups", groups);
				joReturn.put("userGroupThisMonth", userGroupThisMonth);
				joReturn.put("userGroupLastMonth", userGroupLastMonth);
				response.getWriter().print(joReturn);
							
			}
}
