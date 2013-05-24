package com.rehat.tools.vault.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.ReplyComment;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.RequestRelationship;
import com.redhat.tools.vault.bean.SendemailCount;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.CommentRelationshipDAO;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.ReplyCommentDAO;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.RequestRelationshipDAO;
import com.redhat.tools.vault.dao.SendemailCountDAO;
import com.redhat.tools.vault.dao.VAUserDAO;
import com.redhat.tools.vault.dao.VersionDAO;
import com.redhat.tools.vault.service.RequestService;
import com.redhat.tools.vault.util.DateUtil;
@Path("/request")
public class RequestServiceImpl implements RequestService{
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private VersionDAO versionDAO;
	@Inject
	private ProductDAO productDAO;
	@Inject
	private VAUserDAO userDAO;
	@Inject
	private RequestHistoryDAO historyDAO;
	@Inject
	private VaultSendMail mailer;
	@Inject
	private RequestRelationshipDAO relationshipDAO;
	@Inject
    private ReplyCommentDAO replyCommentDAO;
	@Inject 
    private CommentRelationshipDAO commentRelationDAO;
	@Inject
	private SendemailCountDAO countDAO;
	
	private static final Logger log = Logger.getLogger(RequestServiceImpl.class);
	
	@GET
	@Path("{username}")
	@Produces({"application/xml"})
	public List<Request> getMyRequest(@PathParam("username") String username) {
		Request condition = new Request();
		condition.setCreatedby(username);
		List<Request> myRequests  = requestDAO.get(condition);
		if (myRequests != null && myRequests.size() > 0) {
			for (Request myRequest : myRequests) {
				myRequest.setRequestname(StringEscapeUtils
						.escapeHtml(myRequest.getRequestname()));
				try {
					setVersionAndProduct(myRequest);
					setParentAndChildren(myRequest);
				} catch (Exception e) {
					
				}
			}
		}
		return myRequests;
	}
	
	private void setVersionAndProduct(Request request) throws Exception {
		if (request.getVersionid() == -1) {
			request.setProductname("Unspecified");
			request.setVersiondesc("Unspecified");
		}
		else {
			Version version = new Version();
			Product product = new Product();
			version.setId(request.getVersionid());
			if(VaultCacheHandler.getObject(VaultCacheHandler.PRODUCT_KEY) == null){
				List<Product> tempProducts = null;
	        	tempProducts = productDAO.get(new Product());
	            VaultCacheHandler.putToCache(VaultCacheHandler.PRODUCT_KEY, tempProducts);
	            
	            List<Version> tempVersions = versionDAO.get(new Version());
	            VaultCacheHandler.putToCache(VaultCacheHandler.VERSION_KEY, tempVersions);
			}
			
			Version result = VaultCacheHandler.getVersion(version);
			if(result != null){
				request.setVersiondesc(result.getValue());
				product.setId(result.getProduct_id());
				Product pro = VaultCacheHandler.getProduct(product);
				if(pro != null){
					request.setProductname(pro.getName());
				}
			}
		}
	}
	
	private void setParentAndChildren(Request request) throws Exception {
		String[] shipArray = requestDAO.generateParent(request);
		request.setParent(shipArray[0]);
		request.setChildren(shipArray[1]);
	}
	
	@GET
	@Path("/id/{requestid}")
	@Produces({"application/xml"})
	public List<Request> getDetailRequest(@PathParam("requestid") String requestid) {
		Request detailRequest = new Request();
		List<Request> detailRequests = null;
		detailRequest.setRequestid(Long.parseLong(requestid));
		try {
			detailRequests = requestDAO.get(detailRequest);
			if (detailRequests != null && detailRequests.size() > 0) {
				for (Request tempRequest : detailRequests) {
					tempRequest.setRequestname(StringEscapeUtils
							.escapeHtml(tempRequest.getRequestname()));
					setVersionAndProduct(tempRequest);
					setParentAndChildren(tempRequest);
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return detailRequests;
	}

	public boolean displaySignButton(String userName, long requestId) {
		RequestDAO rDAO = new RequestDAO();
		RequestHistoryDAO rhDAO = new RequestHistoryDAO();
		Request condition = new Request();
		condition.setRequestid(requestId);
		try {
			List<Request> requests = rDAO.get(condition);
			if (requests != null && requests.size() > 0) {
				Request request = requests.get(0);
				if (!request.displaySignoffRejectButton(userName)) {
					return false;
				}
				RequestHistory histCondition = new RequestHistory();
				histCondition.setEditedby(userName);
				histCondition.setRequestid(requestId);
				histCondition.setIsHistory(false);
				List<RequestHistory> rHists = rhDAO.get(histCondition, false);
				if (rHists != null && rHists.size() > 0) {
					for (RequestHistory rh : rHists) {
						if (rh.getStatus().equals(Request.SIGNED)
								|| rh.getStatus().equals(Request.SIGNED_BY)
								|| rh.getStatus().equals(Request.REJECTED)) {
							return false;
						}
					}
				}
				return true;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	public boolean displaySignOnBehalfButton(String userName, Long requestId) {
		RequestDAO rDAO = new RequestDAO();
		RequestHistoryDAO rhDAO = new RequestHistoryDAO();
		Request condition = new Request();
		condition.setRequestid(requestId);
		try {
			List<Request> requests = rDAO.get(condition);
			if (requests != null && requests.size() > 0) {
				Request request = requests.get(0);
				if (!request.displaySignoffOnBehalfButton(userName)) {
					return false;
				}

				// other singatories have all signed off. return false.
				String[] signarories = request.getOwner().split(",");
				int number = 0;
				boolean isInSignatoryList = false;
				for (String sg : signarories) {
					String editor = sg.substring(0, sg.indexOf("@"));
					if (!editor.equals(userName)) {
						RequestHistory histCondition = new RequestHistory();
						histCondition.setEditedby(editor);
						histCondition.setRequestid(requestId);
						histCondition.setIsHistory(false);
						List<RequestHistory> rHists = rhDAO.get(histCondition,
								false);
						if (rHists != null && rHists.size() > 0) {
							for (RequestHistory rh : rHists) {
								if (rh.getStatus().equals(Request.SIGNED)
										|| rh.getStatus().equals(
												Request.SIGNED_BY)) {
									number += 1;
								}
							}
						}
					}
				}

				// others all signed
				if (isInSignatoryList && number == signarories.length - 1) {
					return false;
				}

				// all signed
				if (!isInSignatoryList && number == signarories.length) {
					return false;
				}
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
		return false;
	}

	public JSONObject findRequest(String userName, String userEmail) {
		JSONObject joReturn = new JSONObject();
		List<String> requests_s = new ArrayList<String>();
		try {
			requestDAO = new RequestDAO();
			List<Request> rqsts = requestDAO.getCanView(userName, userEmail);
			if (rqsts != null && rqsts.size() > 0) {
				for (Request r : rqsts) {
					requests_s.add(r.getRequestid().toString() + "  "
							+ r.getRequestname().trim());
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}finally {
			joReturn.put("rqsts", requests_s);
		}
		return joReturn;
	}

	public JSONObject showAllEmails() {
		JSONObject joReturn = new JSONObject();
		List<String> emails = null;
		userDAO = new VAUserDAO();
		try {
			if(VaultCacheHandler.getObject(VaultCacheHandler.EMAIL_KEY) != null){
				emails = (List<String>) VaultCacheHandler.getObject(VaultCacheHandler.EMAIL_KEY);
			}else{
				emails = userDAO.findAllUserEmails();
				VaultCacheHandler.putToCache(VaultCacheHandler.EMAIL_KEY, emails);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			joReturn.put("emails", emails);
		}
		return joReturn;
	}

	public JSONObject listNoneSign(String requestId) {
		JSONObject json = new JSONObject();
		List<String> list = getNoneSigned(Long.parseLong(requestId));
		json.put("noneSignUsers", list);
		return json;
	}
	
	private List<String> getNoneSigned(Long requestId) {
		List<String> result = new ArrayList<String>();
		Request request = new RequestDAO().find(requestId);
		String[] ownerArr_pre = request.getOwner().split(",");
		List<String> ownerArr = new ArrayList<String>(); 
		String requester=new StringBuilder(request.getCreatedby()).append("@redhat.com").toString();
		//filter out the requester
		for(int i=0;i<ownerArr_pre.length;i++){
		    if(!ownerArr_pre[i].equals(requester)){
		        ownerArr.add(ownerArr_pre[i]);
		    }
		}
		RequestHistory rh = new RequestHistory();
		rh.setRequestid(request.getRequestid());
		rh.setIsHistory(false);
		List<RequestHistory> list = new RequestHistoryDAO().get(rh, false);
		if (list != null && list.size() > 0) {
			for (String email : ownerArr) {
				boolean signed = false;
				for (RequestHistory h : list) {
					if (email.equalsIgnoreCase(h.getUseremail())
							&& (Request.SIGNED.equals(h.getStatus()) || Request.SIGNED_BY
									.equals(h.getStatus()))) {
						signed = true;
					}
				}

				if (!signed) {
					result.add(email.substring(0, email.indexOf("@")));
				}
			}
		}
		else {
			for (String email : ownerArr) {
				result.add(email.substring(0, email.indexOf("@")));
			}
		}

		return result;
	}

	public JSONObject SignedOrRject(String requestid, String username,
			String comment, String type, String useremail, String flag,
			String onBehalfUsers) {
		JSONObject joReturn = new JSONObject();
		String message = "";
		Long requestId = Long.parseLong(requestid);
		Request searchRequest = requestDAO.find(requestId);
		if(!searchRequest.isCanSignOff(username)){
			joReturn.put("message", "No permission to do the operation!");
			return joReturn;
		}
		Integer requestVersion = searchRequest.getRequestVersion();
		Request request = new Request();
		request.setRequestid(requestId);
		boolean onBehalf = "2".equals(flag) ? true : false;

		if (Request.REJECTED.equals(type)) {
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.REJECTED);
			rh.setRequestid(requestId);
			rh.setEditedby(username);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment(comment);
			rh.setUseremail(useremail);
			rh.setRequestVersion(requestVersion);
			try {
				historyDAO.save(rh);
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			request.setComment(comment);
			request.setEditedby(username);
			request.setEditedtime(DateUtil.getLocalUTCTime());
			request.setSignedby(username);
			request.setSignedtime(DateUtil.getLocalUTCTime());
			requestDAO.update(request);
			message = "Reject success!";

			request = requestDAO.get(new Request(requestId)).get(0);
			try {
				mailer.sendEmail(null, request, null, null,
						"showrequest", "reject", "");
			} catch (Exception e) {
				log.error(e.getMessage());
			}

		}
		else if (!onBehalf) {
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.SIGNED);
			rh.setRequestid(requestId);
			rh.setEditedby(username);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment(comment);
			rh.setUseremail(useremail);
			rh.setRequestVersion(requestVersion);
			try {
				historyDAO.save(rh);
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			request.setComment(comment);
			request.setEditedby(username);
			request.setEditedtime(DateUtil.getLocalUTCTime());
			request.setSignedby(username);
			request.setSignedtime(DateUtil.getLocalUTCTime());
			if (allSigned(searchRequest)) {
				request.setStatus(Request.INACTIVE);
			}
			requestDAO.update(request);

			request = requestDAO.get(new Request(requestId)).get(0);

			try {
				mailer.sendEmail(null, request, null, null,
						"showrequest", "signoff", "");
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			message = "Sign Off success!";

		}
		else {

			String[] userArray = onBehalfUsers.split(",");
			for (String user : userArray) {
				if (StringUtils.isBlank(user)) {
					continue;
				}
				RequestHistory rh = new RequestHistory();
				rh.setIsHistory(false);
				rh.setStatus(Request.SIGNED_BY);
				rh.setRequestid(requestId);
				rh.setEditedby(user);
				rh.setEditedtime(DateUtil.getLocalUTCTime());
				// rh.setComment(comment);
				rh.setComment("");
				rh.setUseremail(user + "@redhat.com");
				rh.setRequestVersion(requestVersion);
				try {
					historyDAO.save(rh);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}

			// save request history for creator
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.SIGNED_ONBEHALF);
			rh.setRequestid(requestId);
			rh.setEditedby(username);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment(comment);
			rh.setUseremail(useremail);
			rh.setRequestVersion(requestVersion);
			try {
				historyDAO.save(rh);
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			request.setComment(comment);
			request.setEditedby(username);
			request.setEditedtime(DateUtil.getLocalUTCTime());
			request.setSignedby(username);
			request.setSignedtime(DateUtil.getLocalUTCTime());

			if (allSigned(searchRequest)) {
				request.setStatus(Request.INACTIVE);
			}

			requestDAO.update(request);

			request = requestDAO.get(new Request(requestId)).get(0);
			message = "Sign Off success!";
			try {
				mailer.sendEmail(null, request, null, null,
						"showrequest", "signoffOnBehalf", "");
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		joReturn.put("message", message);
		return joReturn;
	}
	
	private boolean allSigned(Request request) {

		String[] ownerArr = request.getOwner().split(",");
		int ownerCount = ownerArr.length;

		RequestHistory rh = new RequestHistory();
		rh.setRequestid(request.getRequestid());
		rh.setIsHistory(false);
		List<RequestHistory> list = new RequestHistoryDAO().get(rh, false);
		int signCount = 0;
		if (list != null && list.size() > 0) {
			for (String email : ownerArr) {
				for (RequestHistory h : list) {
					if (email.equalsIgnoreCase(h.getUseremail())
							&& (Request.SIGNED.equals(h.getStatus()) || Request.SIGNED_BY
									.equals(h.getStatus()))) {
						signCount++;
					}
				}
			}
		}
		if (ownerCount == signCount) {
			return true;
		}

		return false;

	}

    public List<Request> getWaitRequests(String userName,String userEmail) {
        List<Request> waitRequests  = requestDAO.getWaitRequests(userName,userEmail);
        if (waitRequests != null && waitRequests.size() > 0) {
            for (Request waitRequest : waitRequests) {
                waitRequest.setRequestname(StringEscapeUtils
                        .escapeHtml(waitRequest.getRequestname()));
                try {
                    setVersionAndProduct(waitRequest);
                    setParentAndChildren(waitRequest);
                } catch (Exception e) {
                    
                }
            }
        }
        return waitRequests;
    }

    public List<Request> getCanViewRequests(String userName,String userEmail) {
        List<Request> viewRequests  = requestDAO.getCanView(userName,userEmail);
        if (viewRequests != null && viewRequests.size() > 0) {
            for (Request viewRequest : viewRequests) {
                viewRequest.setRequestname(StringEscapeUtils
                        .escapeHtml(viewRequest.getRequestname()));
                try {
                    setVersionAndProduct(viewRequest);
                    //setParentAndChildren(viewRequest);
                } catch (Exception e) {
                    
                }
            }
        }
        return viewRequests;
    }

    public List<Request> getSignedOffRequests(String userName,String userEmail) {
        List<Request> signedOffRequests  = requestDAO.getSignedOff(userName,userEmail);
        if (signedOffRequests != null && signedOffRequests.size() > 0) {
            for (Request signedOffRequest : signedOffRequests) {
                signedOffRequest.setRequestname(StringEscapeUtils
                        .escapeHtml(signedOffRequest.getRequestname()));
                try {
                    setVersionAndProduct(signedOffRequest);
                    setParentAndChildren(signedOffRequest);
                } catch (Exception e) {
                    
                }
            }
        }
        return signedOffRequests;
    }

    public List<Request> advanceSearch(String requestName,String creator,String versionid,String productid,String status,
            String owneremail,String userName,String userEmail) {
        List<Request> requests = requestDAO.advanceSearch(requestName, creator, versionid, productid, status, owneremail,
                userName, userEmail);
        if(requests != null && requests.size() > 0){
            for(Request request : requests){
                request.setRequestname(StringEscapeUtils
                        .escapeHtml(request.getRequestname()));
                try{
                    setVersionAndProduct(request);
                    setParentAndChildren(request);
                }catch(Exception e){
                    
                }
            }
        }
        return requests;
    }

    public List<Request> getCCToMeRequests(String userName,String userEmail) {
        List<Request> ccToMeRequests  = requestDAO.getCCToMe(userName,userEmail);
        if (ccToMeRequests != null && ccToMeRequests.size() > 0) {
            for (Request ccToMeRequest : ccToMeRequests) {
                ccToMeRequest.setRequestname(StringEscapeUtils
                        .escapeHtml(ccToMeRequest.getRequestname()));
                try {
                    setVersionAndProduct(ccToMeRequest);
                    setParentAndChildren(ccToMeRequest);
                } catch (Exception e) {
                    
                }
            }
        }
        return ccToMeRequests;
    }

    public Long save(Request request) {
        Long requestId = null;
        try {
            requestId = requestDAO.save(request);
        } catch (Exception e) {
            
        }
        return requestId;
    }

    public void update(Request request) {
        requestDAO.update(request);
        
    }

    public Long saveRelationShip(RequestRelationship rls) {
        Long relationId = null;
        try{
            relationId = relationshipDAO.save(rls);
        } catch (Exception e){
            
        }
        return relationId;
    }

    public String[] generateParent(Long requestId) {
        String[] parentArray = null;
        try {
            parentArray = requestDAO.generateParent(requestId);
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
        return parentArray;
    }

    public void disableRelationShip(RequestRelationship condition) {
        relationshipDAO.disable(condition);
        
    }

	public JSONObject EditVersion(String versionid) {
		StringBuffer sb = new StringBuffer();
		JSONObject joReturn = new JSONObject();
		Version version = new Version();
		List<Version> versions = new ArrayList<Version>();
		Long productid = null;
		Version productVersion = new Version();
		List<Version> list = new ArrayList<Version>();
		versionDAO = new VersionDAO();
		try {
			version.setId(Long.parseLong(versionid));
			versions = versionDAO.get(version);
			if (versions != null && versions.size() > 0) {
				productid = versions.get(0).getProduct_id();

				productVersion.setProduct_id(productid);
				list = versionDAO.get(productVersion);
				if (list != null && list.size() > 0) {
					sb.append("<option value='-1'>------</option>");
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getId() == Long
								.parseLong(versionid)) {
							sb.append("<option value='"
									+ list.get(i).getId()
									+ "' selected='selected' id='versionid'>"
									+ list.get(i).getValue()
									+ "</option>");
						}
						else {
							sb.append("<option value='"
									+ list.get(i).getId() + "'>"
									+ list.get(i).getValue()
									+ "</option>");
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			joReturn.put("sboption", sb.toString());
			joReturn.put("productid", productid.toString());
		}
		return joReturn;
	}

    public void deleteRequest(Request request, String userName) throws Exception{
        RequestHistory history = new RequestHistory();
        List<RequestHistory> historys = null;
        List<Request> deleteRequest = null;
        try {
            deleteRequest = requestDAO.get(request);
            if (deleteRequest != null && deleteRequest.size() > 0) {
            	if(!deleteRequest.get(0).getCreatedby().equals(userName)){
                	throw new Exception("No permission to do the operation!");
                }
                requestDAO.deleteRequest(request);
                history.setRequestid(request.getRequestid());
                RequestHistoryDAO historyDAO = new RequestHistoryDAO();
                historys = historyDAO.get(history, false);
                if (historys != null && historys.size() > 0) {
                    for (RequestHistory temp : historys) {
                        historyDAO.deleteHistory(temp);
                        //delete reply
                        List<Long> replyIdList = commentRelationDAO.getReplyIdListByHistoryId(temp.getHistoryid());
                        commentRelationDAO.deleteRelationByHistoryid(temp.getHistoryid());
                        List<ReplyComment> replyList = replyCommentDAO.getReplyCommentListByIdList(replyIdList);
                        replyCommentDAO.deleteReplyCommentList(replyList);
                    }
                }

                RequestRelationship rl = new RequestRelationship();
                rl.setRelationshipId(request.getRequestid());
                relationshipDAO.delete(rl);
                rl = new RequestRelationship();
                rl.setRequestId(request.getRequestid());
                relationshipDAO.delete(rl);
                mailer.sendEmail(null, deleteRequest.get(0), null, 
                        "", "", "delete", "");
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new Exception(e);
        }
        
    }

    public List<Request> findByIds(Long[] requestIds) {
        List<Request> requests = null;
        try{
            requests = requestDAO.findByIds(requestIds);
            if (requests != null && requests.size() > 0) {
                for (Request r : requests) {
                    r.setRequestname(StringEscapeUtils.escapeHtml(r
                            .getRequestname()));
                    r = getDetailedStatus(r, historyDAO);
                }
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return requests;
    }
    
    private Request getDetailedStatus(Request r, RequestHistoryDAO histDao)
            throws Exception {
        // RequestHistoryDAO histDao = new RequestHistoryDAO();
        String waiting = "";
        String comment = "";
        String reject = "";
        String signoff = "";
        Date tempDate = null;
        boolean isSignOnBehalf = false;
        if ("".equals(r.getOwner()) || r.getOwner() == null)
            return r;
        String[] signatories = r.getOwner().split(",");
        List<String> signatoryList = Arrays.asList(signatories);
        for (int i = 0; i < signatories.length; i++) {
            RequestHistory hist = new RequestHistory();
            hist.setRequestid(r.getRequestid());
            hist.setUseremail(signatories[i].trim());
            hist.setIsHistory(false);
            List<RequestHistory> hists = histDao.get(hist, false);
            if (hists != null && hists.size() > 0) {
                boolean isSignedOrRejected = false;
                for (RequestHistory h : hists) {
                    if (h.getStatus().equals(Request.SIGNED)
                            || h.getStatus().equals(Request.SIGNED_BY)) {
                        signoff += signatories[i] + "|" + DateUtil.toString(h.getEditedtime(), "yyyy-MM-dd HH:mm") + ",";
                        isSignedOrRejected = true;
                        break;
                    }
                    else if (h.getStatus().equals(Request.REJECTED)) {
                        reject += signatories[i] + "|" + DateUtil.toString(h.getEditedtime(), "yyyy-MM-dd HH:mm") +  ",";
                        isSignedOrRejected = true;
                        break;
                    }
                    /*if(tempDate == null){
                    	tempDate = h.getEditedtime();
                    }else if(h.getEditedtime().getTime() > tempDate.getTime()){
                    	tempDate = h.getEditedtime();
                    }*/
                }
                /*if (!isSignedOrRejected) {
                    comment += signatories[i] + "|" + DateUtil.toString(tempDate, "yyyy-MM-dd HH:mm") + ",";
                }*/

            }
            else {
                waiting += signatories[i] + ",";
            }
        }

        RequestHistory hist = new RequestHistory();
        hist.setRequestid(r.getRequestid());
        hist.setIsHistory(false);
        List<RequestHistory> hists = histDao.get(hist, true);
        Map<String,Date> commentDateMap = new HashMap<String,Date>();
        if (hists != null && hists.size() > 0) {
            for (RequestHistory h : hists) {
                if (h.getStatus().equals(Request.COMMENTS)) {
                	if(!commentDateMap.keySet().contains(h.getUseremail())){
                		commentDateMap.put(h.getUseremail(), h.getEditedtime());
                	}else{
                		if(commentDateMap.get(h.getUseremail()) == null){
                			commentDateMap.put(h.getUseremail(), h.getEditedtime());
                		}else{
                			if(commentDateMap.get(h.getUseremail()).getTime() < h.getEditedtime().getTime()){
                				commentDateMap.put(h.getUseremail(), h.getEditedtime());
                			}
                		}
                	}
                }
                if (h.getEditedby().equals(r.getCreatedby())
                        && h.getStatus().equals(Request.SIGNED_ONBEHALF)) {
                    isSignOnBehalf = true;
                }
            }
        }
        for(String useremail : commentDateMap.keySet()){
        	comment += useremail + "|" + DateUtil.toString(commentDateMap.get(useremail), "yyyy-MM-dd HH:mm") + ",";
        }
        if (waiting.length() > 0) {
            waiting = waiting.substring(0, waiting.lastIndexOf(","));
        }
        if (comment.length() > 0) {
            comment = comment.substring(0, comment.lastIndexOf(","));
        }
        if (reject.length() > 0) {
            reject = reject.substring(0, reject.lastIndexOf(","));
        }
        if (signoff.length() > 0) {
            signoff = signoff.substring(0, signoff.lastIndexOf(","));
        }
        r.setWaitingList(waiting);
        r.setCommentList(comment);
        r.setSignoffList(signoff);
        r.setRejectedList(reject);
        r.setIsSignOnBehalf(isSignOnBehalf);
        return r;
    }

    public void updateEmailCount(Request request) {
        SendemailCount emailCount = new SendemailCount();
        try{
            emailCount.setRequestid(request.getRequestid());
            List<SendemailCount> sends = countDAO.get(emailCount);
            emailCount.setCount(0L);
            emailCount.setRequesttime(request.getRequesttime());
            if (sends != null && sends.size() > 0) {
                emailCount.setSendid(sends.get(0).getSendid());
                countDAO.update(emailCount);
            }
            else {
                countDAO.save(emailCount);
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        
    }

    public Map<String, Long> getRequestCount(String userName, String userEmail) {
        Map<String, Long> counts = null;
        try{
            counts = requestDAO.getRequestCount(userName, userEmail);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return counts;
    }

    public Request getRequest(Request request) {
        Request req = null;
        try{
            List<Request> reqList = requestDAO.get(request);
            if(reqList != null && reqList.size() > 0){
                req = reqList.get(0);
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return req;
    }

    public Long getParentId(Request current) {
        Long parentId = null;
        try{
            parentId = relationshipDAO.getParentId(current);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return parentId;
    }

    public List<Long> getChildId(Request current) {
        List<Long> childIds = null;
        try{
            childIds = relationshipDAO.getChildId(current);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return childIds;
    }

    public List<RequestHistory> getHistory(RequestHistory history,boolean b) {
        List<RequestHistory> historys = null;
        try{
            historys = historyDAO.get(history, b);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return historys;
    }

    public void disableHistory(RequestHistory history) {
        try{
            historyDAO.deleteHistory(history);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        
    }

    public String compare(Request current,Request request) {
        String result = "";
        try{
            result = requestDAO.compare(current, request);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return result;
    }

	public void withdrawRequest(Request request,String requestid, String requestVersion, String userName, String userEmail) {
		List<Request> withDrawRequest = null;
		withDrawRequest = requestDAO.get(request);
		if (withDrawRequest != null && withDrawRequest.size() > 0) {
			request.setStatus(Request.WITHDRAW);
			requestDAO.update(request);
			//save to history
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.WITHDRAW);
			rh.setRequestid(Long.parseLong(requestid));
			rh.setEditedby(userName);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment("");
			rh.setUseremail(userEmail);
			rh.setRequestVersion(Integer.parseInt(requestVersion));
			try {	
				historyDAO.save(rh);
				mailer.sendEmail(request, withDrawRequest.get(0),null,
						"", "", "withdraw", "");
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	
	//zym.test
	public JSONObject staicsCount() {
		JSONObject json = new JSONObject();
		int n=5;
		String day[]=new String[n];
		String week[]=new String[n];
		String month[]=new String[n];
		

		long[] resultDayDate=new long[n];
		long[] resultWeekDate=new long[n];
		long[] resultMonthDate=new long[n];
		BigInteger[] resultUserDayDate=new BigInteger[n];
		BigInteger[] resultUserWeekDate=new BigInteger[n];
		BigInteger[] resultUserMonthDate=new BigInteger[n];
		String[] userday=new String[n];
		String[] userweek=new String[n];
		String[] usermonth=new String[n];

		
		  Map<String, Long> resultDay = null;
		  Map<String, Long> resultWeek = null;
		  Map<String, Long> resultMonth= null;
		  Map<String, BigInteger> resultUserDay = null;
		  Map<String, BigInteger> resultUserWeek = null;
		  Map<String, BigInteger> resultUserMonth = null;
		  Map<String,String> userEveryDay=null;
		  Map<String,String> userEveryWeek=null;
		  Map<String,String> userEveryMonth=null;
		  
	        try{
	        	resultDay = requestDAO.countByCreatedTimeDay();
	        	resultWeek = requestDAO.countByCreatedTimeWeek();
	        	resultMonth = requestDAO.countByCreatedTimeMonth();
	        	resultUserDay=requestDAO.countActiveUserNumberDay();
	        	resultUserWeek=requestDAO.countActiveUserNumberWeek();
	        	resultUserMonth=requestDAO.countActiveUserNumberMonth();
	        	userEveryDay=requestDAO.countActiveUserDay();
	        	userEveryWeek=requestDAO.countActiveUserWeek();
	        	userEveryMonth=requestDAO.countActiveUserMonth();
	        }catch(Exception e){
	            log.error(e.getMessage(),e);
	        }
	        
	        Set setDay = resultDay.keySet();
	        Set setWeek = resultWeek.keySet();
	        Set setMonth = resultMonth.keySet();
	        Set setUserDay = resultUserDay.keySet();
	        Set setUserWeek = resultUserWeek.keySet();
	        Set setUserMonth = resultUserMonth.keySet();
	        Set setUserEveryDay=userEveryDay.keySet();
	        Set setUserEveryWeek=userEveryWeek.keySet();
	        Set setUserEveryMonth=userEveryMonth.keySet();
	        
	        Iterator itDay = setDay.iterator();
	        Iterator itWeek = setWeek.iterator();
	        Iterator itMonth = setMonth.iterator();
	        Iterator itUserDay = setUserDay.iterator();
	        Iterator itUserWeek = setUserWeek.iterator();
	        Iterator itUserMonth = setUserMonth.iterator();
	        Iterator itUserEveryDay = setUserEveryDay.iterator();
	        Iterator itUserEveryWeek = setUserEveryWeek.iterator();
	        Iterator itUserEveryMonth = setUserEveryMonth.iterator();
	        
	        int dayNum=0;
	        int weekNum=0;
	        int monthNum=0;
	        int dayUserNum=0;
	        int weekUserNum=0;
	        int monthUserNum=0;
	        int userNumDay=0;
	        int userNumWeek=0;
	        int userNumMonth=0;
	        
	        while (itDay.hasNext())
	        {
	        	String keyDay = (String) itDay.next();        	
	        	Long valueDay = (Long) resultDay.get(keyDay);    	
	        	day[dayNum]=keyDay;
	        	resultDayDate[dayNum]=valueDay;
	        	dayNum++;

	        }
	        //
	        while (itUserEveryDay.hasNext())
	        {
	        	
	        	String keyDay = (String) itUserEveryDay.next();   
	        	//System.out.println("keyDay="+keyDay);
	        	//System.out.println("valueday="+userEveryDay.get(keyDay));
	        	String valueDay = (String) userEveryDay.get(keyDay);    	

	        	userday[userNumDay]=valueDay;
	        	userNumDay++;

	        }
	        while (itWeek.hasNext())
	        {
	        	String keyWeek = (String) itWeek.next();        	
	        	Long valueWeek = (Long) resultWeek.get(keyWeek);       	
	        	week[weekNum]=keyWeek;
	        	resultWeekDate[weekNum]=valueWeek;
	        	weekNum++;

	        }
	        
	        while (itUserEveryWeek.hasNext())
	        {
	        	
	        	String keyWeek = (String) itUserEveryWeek.next();   
	        	//System.out.println("keyWeek="+keyWeek);
	        	//System.out.println("valueWeek="+userEveryWeek.get(keyWeek));
	        	String valueWeek = (String) userEveryWeek.get(keyWeek);    	

	        	userweek[userNumWeek]=valueWeek;
	        	userNumWeek++;
	        }
	        while (itMonth.hasNext())
	        {
	        	String keyMonth = (String) itMonth.next();	        	
	        	Long valueMonth = (Long) resultMonth.get(keyMonth);      	
	        	month[monthNum]=keyMonth;
	        	resultMonthDate[monthNum]=valueMonth;
	        	monthNum++;

	        }
	        while (itUserEveryMonth.hasNext())
	        {
	        	
	        	String keyMonth = (String) itUserEveryMonth.next();   
	        	System.out.println("keyMOnth="+keyMonth);
	        	System.out.println("valueMonth"+userEveryMonth.get(keyMonth));
	        	String valueMonth = (String) userEveryMonth.get(keyMonth);    	

	        	usermonth[userNumMonth]=valueMonth;
	        	userNumMonth++;
	        }
	        while (itUserDay.hasNext())
	        {
	        	String keyUserDay = (String) itUserDay.next();        	
	        	BigInteger valueUserDay = (BigInteger) resultUserDay.get(keyUserDay);    	

	        	resultUserDayDate[dayUserNum]=valueUserDay;
	        	dayUserNum++;

	        }
	        while (itUserWeek.hasNext())
	        {
	        	String keyUserWeek = (String) itUserWeek.next();        	
	        	BigInteger valueUserWeek = (BigInteger) resultUserWeek.get(keyUserWeek);    	

	        	resultUserWeekDate[weekUserNum]=valueUserWeek;
	        	weekUserNum++;

	        }
	        while (itUserMonth.hasNext())
	        {
	        	String keyUserMonth = (String) itUserMonth.next();        	
	        	BigInteger valueUserMonth = (BigInteger) resultUserMonth.get(keyUserMonth);    	

	        	resultUserMonthDate[monthUserNum]=valueUserMonth;
	        	monthUserNum++;

	        }

	        json.put("day", day);
	        json.put("resultDay", resultDayDate);
	        json.put("resultUserDay", resultUserDayDate);
	        
	        json.put("week", week);
	        json.put("resultWeek", resultWeekDate);
	        json.put("resultUserWeek", resultUserWeekDate);
	        
	        json.put("month", month);
	        json.put("resultMonth", resultMonthDate);
	        json.put("resultUserMonth", resultUserMonthDate);
	        
	        json.put("userday", userday);
	        json.put("userweek", userweek);
	        json.put("usermonth", usermonth);



		 return json;
	}


}
