package com.redhat.tools.vault.listener;

import java.util.TimerTask;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.rehat.tools.vault.service.impl.BugzillaProductUpdate;

/**
 * 
 * @author maying
 *
 */
public class UpdateTimer extends TimerTask {

	private BugzillaProductUpdate updateService = null;
	
	/** The logger. */
	protected static final Logger log = Logger.getLogger(UpdateTimer.class);
	
	public UpdateTimer() {
		updateService = new BugzillaProductUpdate();
	}

	@Override
	public void run() {
		try {
			log.info("[product update timertask] start......about 4 minutes to proceed the update task......");
			updateService.productVersionUpdateTask();
			log.info("[product update timertask] excute success......");
		} catch (Exception e) {
			log.error("[product update timertask] errorMessage:"+e.getMessage());
//			e.printStackTrace();
		} finally {
//			updateService = null;
		}
	}

}
