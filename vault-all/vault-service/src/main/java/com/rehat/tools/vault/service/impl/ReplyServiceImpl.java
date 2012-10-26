package com.rehat.tools.vault.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.CommentRelationship;
import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.ReplyComment;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.CommentRelationshipDAO;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.ReplyCommentDAO;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.VAUserDAO;
import com.redhat.tools.vault.dao.VersionDAO;
import com.redhat.tools.vault.service.ReplyService;
import com.redhat.tools.vault.service.RequestService;
import com.redhat.tools.vault.util.DateUtil;

public class ReplyServiceImpl implements ReplyService{
	private static final Logger log = Logger.getLogger(ReplyServiceImpl.class);
	@Inject
	private ReplyCommentDAO replyCommentDAO;
	@Inject 
	private CommentRelationshipDAO commentRelationDAO;
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private VaultSendMail mailer;

	public JSONObject AddReply(String baseid, String replyComment,
			String editedby, String requestid, String historyid) {
		JSONObject joReturn = new JSONObject();
		List<ReplyComment> addedReplyList = new ArrayList<ReplyComment>();
		String flag = "failed";
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try{
			ReplyComment reply = new ReplyComment(Long.parseLong(baseid), replyComment,
					editedby, (format.format(DateUtil.getLocalUTCTime())).toString());
			replyCommentDAO.save(reply);
			CommentRelationship commentRelation = new CommentRelationship(Long.parseLong(historyid), Long.parseLong(requestid), reply.getReplyid());
			commentRelationDAO.save(commentRelation);
			List<Long> replyIdList = commentRelationDAO.getReplyIdListByHistoryId(Long.parseLong(historyid));
			addedReplyList = replyCommentDAO.getReplyCommentListByIdList(replyIdList);
			//send email
			Request request = requestDAO.find(Long.parseLong(requestid));
			mailer.sendEmail(null, request, reply, null, "showrequest", "reply", "");
			flag = "success";
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			joReturn.put("addedReplyList", addedReplyList);
			joReturn.put("flag", flag);
		}
		return joReturn;
	}

	public JSONObject ShowReply(String requestid, String historyid) {
		JSONObject joReturn = new JSONObject();
		List<ReplyComment> replyList = new ArrayList<ReplyComment>();
		try{
			List<Long> replyIdList = commentRelationDAO.getReplyIdListByHistoryId(Long.parseLong(historyid));
			replyList = replyCommentDAO.getReplyCommentListByIdList(replyIdList);					
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			joReturn.put("replyList", replyList);
		}
		return joReturn;
	}
	
}
