package com.redhat.tools.vault.web;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestMap;
import com.redhat.tools.vault.bean.RequestRelationship;
import com.redhat.tools.vault.service.RequestService;
import com.redhat.tools.vault.util.MD5Util;
import com.redhat.tools.vault.util.StringUtil;
import com.redhat.tools.vault.web.helper.Attachment;

/**
 * Servlet implementation class SaverequestServlet
 */
@WebServlet("/Saverequest")
public class SaverequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger log = Logger.getLogger(SaverequestServlet.class);
	@Inject
	private RequestService service;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaverequestServlet() {
        super();
        // TODO Auto-generated constructor stub
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
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
	    Long requestId         =   null;
        String requestName     =   null;
        String versionId       =   null;
        String requestTime     =   null;
        String requestDatetime =   null;
        String detail          =   null;
        String isPublic        =   null;
        String userName        =   "wguo";
        String owner           =   null;
        String notifyMap       =   null; 
        String cc              =   null;
        String parent          =   null;
        String children        =   null;
        try{
            requestName = req.getParameter("requestname");
            versionId = req.getParameter("selectversionid");
            requestTime = req.getParameter("requesttime");
            requestDatetime = req.getParameter("requestdatetime");
            detail = req.getParameter("detailStr");
            isPublic = req.getParameter("is_public");
            owner = req.getParameter("owner");
            notifyMap = req.getParameter("notifyOption");
            cc = req.getParameter("cc");
            parent = req.getParameter("parentStr");
            children = req.getParameter("childrenStr");
            
            Request request = new Request();
            request.setRequestVersion(1);
            request.setCreatedtime(new Date());
            request.setCreatedby(userName);
            request.setStatus(Request.ACTIVE);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            Date date = new Date();
            if (requestTime != null && !"".equals(requestTime)) {
                try {
                    date = dateFormat.parse(requestTime);
                    if (!StringUtil.isEmpty(requestDatetime)) {
                        date = timeFormat.parse(new StringBuffer(requestTime).append(" ").append(requestDatetime).toString());
                    }
                } catch (ParseException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(isPublic != null){
                request.setIs_public(1);
            }else{
                request.setIs_public(0);
            }
            owner = StringUtil.removeComma(owner.trim());
            children = StringUtil.removeComma(children);
            cc = StringUtil.removeComma(cc);
            
            request.setRequestname(requestName);
            request.setVersionid(Long.parseLong(versionId));
            request.setRequesttime(date);
            request.setDetail(detail);
            request.setOwner(owner);
            request.setForward(cc);
            request.setParent(parent);
            request.setChildren(children);
            
            requestId = service.save(request);
            
            Set<RequestMap> maps        = new HashSet<RequestMap>();
            //maps, notify option                   
            Set<String> needSignOffList = new HashSet<String>();
            String signOffList = "";
            String notifySignOffList = "";
            String sendEmailList = "";
            boolean haveNoNeedSignOffEmails = false;
            boolean signedOffOnBehalf  = false;
            String[] notifyArray = notifyMap.split(",");
            if (notifyArray != null && notifyArray.length > 0) {
                for (int i = 0; i < notifyArray.length; i++) {
                    RequestMap rMap = new RequestMap();
                    String emailAndType = notifyArray[i];
                    if(!StringUtil.isEmpty(emailAndType)){
                        rMap.setMapname(Request.PROPERTY_REQUEST_NOTIFYCATION);
                        rMap.setMapvalue(emailAndType);
                        rMap.setRequestid(requestId);
                        rMap.setRequestVersion(request.getRequestVersion());
                        maps.add(rMap);
    
                        String notifyType = emailAndType.substring(emailAndType.indexOf(":") + 1, emailAndType.length());
                        String notifyEmail = emailAndType.substring(0, emailAndType.indexOf(":"));
    
                        if (notifyType.equals(Request.NOTIFICATION_TYPE_REQUIRED_SIGNOFF)
                                || notifyType.equals(Request.NOTIFICATION_TYPE_DEFAULT)) {
                            needSignOffList.add(notifyEmail.toUpperCase());
                            signOffList += notifyEmail + ",";
                        } else if (notifyType.equals(Request.NOTIFICATION_TYPE_NOTIFY_CHANGE)) {
                            notifySignOffList += notifyEmail + ",";
                        } else {
                            sendEmailList += notifyEmail + ",";
                        }
                    }
                }
            }
            
            //files
            String writePath = getServletContext().getRealPath(Attachment.FILEPATH);
            String sessionid = req.getRequestedSessionId();
            writePath += "/" + sessionid;
            File[] files = Attachment.getAttachment(writePath);
            
            if (files != null && files.length > 0) {                        
                for (File file : files) {
                    if(file!= null && file.isFile()){
                        RequestMap fmap = new RequestMap();
                        fmap.setMapname(Request.PROPERTY_REQUEST_ATTACHMENT);
                        fmap.setMapvalue(file.getName());
                        fmap.setRequestid(requestId);
                        fmap.setRequestVersion(request.getRequestVersion());
                        maps.add(fmap);
                        Map<String, String> dynamic = new HashMap<String, String>();
                        dynamic.put(Request.PROPERTY_ATTACHMENT_USER,userName);
                        dynamic.put(Request.PROPERTY_ATTACHMENT_SIZE, String.valueOf(file.length()));
                        dynamic.put(Request.PROPERTY_ATTACHMENT_DATE,Attachment.getTimeStamp(file));
                        dynamic.put(Request.PROPERTY_ATTACHMENT_MD5,MD5Util.getFileMD5String(file));
                        fmap.setDynamic(dynamic);   
                    }                           
                }
            }
            
            request.setMaps(maps);
            service.update(request);
            //save parent and child info
            //1.parent
            if(parent!=null && !"".equals(parent.trim())){
                Long parentId = Long.parseLong(parent.split("  ")[0]);                      
                RequestRelationship rls = new RequestRelationship();
                rls.setRequestId(request.getRequestid());
                rls.setRelationshipId(parentId);                            
                rls.setIsParent(true);
                rls.setRequestVersion(request.getRequestVersion());
                rls.setEnable(true);
                service.saveRelationShip(rls);
            }
                                
            //2. child
            if(!StringUtil.isEmpty(children)){
                //100  requestname,101  requestname
                String[] childArray = children.split(",");
                if(childArray!=null && childArray.length>0){                                
                    for(int i=0;i<childArray.length;i++){   
                        Long childId = Long.parseLong(childArray[i].split("  ")[0]);                                    
                        //remove relationship which childId as child, as there is only one parent for this child request.
                        RequestRelationship childR = new RequestRelationship();
                        childR.setRelationshipId(childId);
                        childR.setIsParent(false);
                        childR.setEnable(true);
                        service.disableRelationShip(childR);
                        childR = new RequestRelationship();
                        childR.setRequestId(childId);
                        childR.setIsParent(true);
                        childR.setEnable(true);
                        service.disableRelationShip(childR);
                        //save new child
                        childR = new RequestRelationship();
                        childR.setRequestId(request.getRequestid());
                        childR.setRelationshipId(childId);
                        childR.setIsParent(false);
                        childR.setEnable(true);
                        childR.setRequestVersion(request.getRequestVersion());
                        service.saveRelationShip(childR);
                    }
                }
            }
            
            //TODO email
            
            log.info("save request successfully !");
            req.getRequestDispatcher("/showRequest?requestid=" + request.getRequestid()).forward(req, response);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
	}

}
