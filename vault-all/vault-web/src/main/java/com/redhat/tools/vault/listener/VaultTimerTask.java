package com.redhat.tools.vault.listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.SendemailCount;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.SendemailCountDAO;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.web.helper.MGProperties;
import com.rehat.tools.vault.service.impl.VaultSendMail;

public class VaultTimerTask extends TimerTask {
    /** The logger. */
    protected static final Logger log = Logger.getLogger(VaultTimerTask.class);

    MGProperties prop;

    @Inject
    SendemailCount mailcount;

    List<SendemailCount> countlist = null;

    @Inject
    SendemailCountDAO countDAO = null;

    @Inject
    Request request = null;

    @Inject
    RequestDAO requestDAO = null;

    @Inject
    VaultSendMail mailer = null;

    List<Request> requests = null;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public VaultTimerTask() {
        log.info("init");
        prop = MGProperties.getInstance();
        // mailcount = new SendemailCount();
        countlist = new ArrayList<SendemailCount>();
        // countDAO = new SendemailCountDAO();
        // requestDAO = new RequestDAO();
        requests = new ArrayList<Request>();
    }

    /** Timer Task main **/
    @Override
    public void run() {
        log.info("start");
        try {
            log.debug(request);
            request.setStatus(request.ACTIVE);
            request.setRequesttime(DateUtil.dateOnly((new Date(new Date().getTime() + 24 * 60 * 60 * 1000))));
            requests = requestDAO.get(request);
            log.info(requests);
            if (requests != null && requests.size() > 0) {
                for (Request req : requests) {
                    mailer.sendEmail(null, req, null, null, "showrequest", "dueDate", "");
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.debug("success");
    }
}
