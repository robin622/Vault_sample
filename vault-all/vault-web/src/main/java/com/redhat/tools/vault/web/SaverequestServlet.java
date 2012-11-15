package com.redhat.tools.vault.web;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.RequestMap;
import com.redhat.tools.vault.bean.RequestRelationship;
import com.redhat.tools.vault.service.RequestService;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.MD5Util;
import com.redhat.tools.vault.util.StringUtil;
import com.redhat.tools.vault.web.helper.Attachment;
import com.rehat.tools.vault.service.impl.VaultSendMail;

/**
 * Servlet implementation class SaverequestServlet
 */
@WebServlet("/Saverequest")
public class SaverequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger log = Logger.getLogger(SaverequestServlet.class);
	@Inject
	private RequestService service;
    @Inject
    private VaultSendMail mailer;
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
        String userName        =   (String) req.getSession().getAttribute("userName");
        String owner           =   null;
        String notifyMap       =   null; 
        String cc              =   null;
        String parent          =   null;
        String children        =   null;
        String gap             =   null;
        boolean editMode = false;
        try{
            String newrequestid = req.getParameter("newrequestid");
            if(newrequestid != null && !"".equals(newrequestid)){
                requestId = Long.valueOf(newrequestid);
                editMode = true;
            }
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
            gap = req.getParameter("gap");
            int requestVersion = 0;
            Set<RequestMap> maps        = new HashSet<RequestMap>();
            String signOffList = "";
            String notifySignOffList = "";
            String sendEmailList = "";
            
            Request request = new Request();
            if(editMode){
                request.setRequestid(requestId);
                request = service.getRequest(request);
                if(!request.getCreatedby().equals(userName)){
                	throw new Exception("No permission to do the operation!");
                }
                requestVersion = request.getRequestVersion();
                request.setRequestVersion(requestVersion + 1);
                request.setEditedby(userName);
                request.setEditedtime(DateUtil.getLocalUTCTime());
                // maps.addAll(request.getMaps());//add exsisting maps
                // to changed request.
                /*for (RequestMap m : request.getMaps()) {
                    m.setMapid(null);
                    maps.add(m);
                }*/
                Attachment.remove(requestId);
            }else{
                request.setRequestVersion(1);
                request.setCreatedtime(DateUtil.getLocalUTCTime());
                request.setCreatedby(userName);
                request.setStatus(Request.ACTIVE);
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            Date date = DateUtil.getLocalUTCTime();
            if (requestTime != null && !"".equals(requestTime)) {
                try {
                    date = dateFormat.parse(requestTime);
                    if (!StringUtil.isEmpty(requestDatetime)) {
                        date = timeFormat.parse(new StringBuffer(requestTime).append(" ").append(requestDatetime).toString());
                    }
                    date=DateUtil.addHours(date,Integer.parseInt(gap));
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
            
            String oldOwner = request.getOwner();
            request.setRequestname(requestName);
            request.setVersionid(Long.parseLong(versionId));
            request.setRequesttime(date);
            request.setDetail(detail);
            request.setOwner(owner);
            request.setForward(cc);
            request.setParent(parent);
            request.setChildren(children);
            Request current = new Request();
            
            //maps, notify option                   
            Set<String> needSignOffList = new HashSet<String>();
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
            
            if (editMode) {
                // get current request info for compare
                current.setRequestid(requestId);
                current = service.getRequest(current);
                Long parentId = service.getParentId(current);
                String childId = "";
    
                List<Long> childIdList = service.getChildId(current);
                if (childIdList != null && childIdList.size() > 0) {
                    int index = 0;
                    for (Long c : childIdList) {
                        if (index < childIdList.size() - 1) {
                            childId += c + ",";
                        }
                        else {
                            childId += c;
                        }
                        index++;
                    }
                }
                if (parentId != null) {
                    current.setParent(parentId.toString());
                }
                current.setChildren(childId);
    
                request.setStatus(current.getStatus());
                RequestHistory history = new RequestHistory();
                List<RequestHistory> list = null;
                history.setRequestid(request.getRequestid());
                history.setIsHistory(false);
                list = service.getHistory(history, false);
                changeRequestStatus(request, oldOwner, needSignOffList,
                        history, list);
                request.setNotifySignOffList(StringUtil
                        .removeComma(notifySignOffList));// notify the
                                                            // change
                request.setNeedSignOffList(StringUtil
                        .removeComma(signOffList));// resend email
                request.setSendEmailList(StringUtil
                        .removeComma(sendEmailList));
    
                // requestDAO.update(request);
            }else{
                requestId = service.save(request);
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
                Attachment.move(writePath, requestId);
                Attachment.remove(writePath);
            }
            
            request.setMaps(maps);
            service.update(request);
            //save parent and child info
            //1.parent
            if (editMode) {
                RequestRelationship parentR = new RequestRelationship();
                parentR.setRelationshipId(request.getRequestid());
                parentR.setIsParent(false);
                parentR.setEnable(true);
                service.disableRelationShip(parentR);

                parentR = new RequestRelationship();
                parentR.setRequestId(request.getRequestid());
                parentR.setIsParent(true);
                parentR.setEnable(true);
                service.disableRelationShip(parentR);
            }
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
            if (editMode) {
                RequestRelationship childR = new RequestRelationship();
                childR = new RequestRelationship();
                childR.setRelationshipId(request.getRequestid());
                childR.setIsParent(true);
                childR.setEnable(true);
                service.disableRelationShip(childR);

                childR = new RequestRelationship();
                childR.setRequestId(request.getRequestid());
                childR.setIsParent(false);
                childR.setEnable(true);
                service.disableRelationShip(childR);
            }
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
            
            //email
            service.updateEmailCount(request);
            if(!editMode){
                mailer.sendEmail(req, request, null, "", "showrequest", "new", "");
            }else{
                // compare
                String compare = service.compare(current, request);
                mailer.sendEmail(req, request, null, "",
                        "showrequest", "change", compare);
            }
            log.info("save request successfully !");
            response.sendRedirect(req.getContextPath() + "/showRequest/" + request.getRequestid());
        }catch(Exception e){
            log.error(e.getMessage(), e);
            if(e.getMessage().contains("No permission to do the operation")){
            	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
	}
	
	private void changeRequestStatus(Request currentRequest, String oldOwner,
            Set<String> needSignOffList, RequestHistory history,
            List<RequestHistory> list) throws Exception {
        String[] currentOwnerArray = currentRequest.getOwner().split(",");
        String currentStatus = currentRequest.getStatus();
        boolean isAddingOwner = false;
        boolean isNotifySign = false;
        boolean isAllSigned = false;
        int signCount = 0;
        for (String owner : currentOwnerArray) {
            if (oldOwner != null && !oldOwner.contains(owner)) {
                isAddingOwner = true;
                break;
            }
            if (needSignOffList.contains(owner.toUpperCase())) {
                isNotifySign = true;
                break;
            }
        }
        if (list != null && list.size() > 0) {
            if (isAddingOwner == false) {
                for (String nowOwner : currentOwnerArray) {
                    for (RequestHistory h : list) {
                        if (nowOwner.equalsIgnoreCase(h.getUseremail())
                                && (Request.SIGNED.equals(h.getStatus()) || Request.SIGNED_BY
                                        .equals(h.getStatus())))
                            signCount++;
                    }
                }
            }
            for (RequestHistory rh : list) {
                if (!rh.getStatus().equals("Comments") && (needSignOffList.contains(rh.getUseremail().toUpperCase())
                        || !currentRequest.isSignatory(rh.getEditedby()))) {
                    history.setHistoryid(rh.getHistoryid());
                    service.disableHistory(history);
                }
            }
        }
        if (currentOwnerArray.length == signCount)
            isAllSigned = true;

        if (isAddingOwner == true || isNotifySign == true) {
            currentRequest.setStatus(Request.ACTIVE);
        }
        else if (currentStatus != null && currentStatus.equals("In progress")
                && isAllSigned == true) {
            currentRequest.setStatus(Request.INACTIVE);
        }
    }

}
