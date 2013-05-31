package com.redhat.tools.vault.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.web.helper.MGProperties;

/**
 * 
 * @author maying
 * 
 */
public class UpdateServletListener implements ServletContextListener {

    private Timer timer = new Timer();

    @Inject
    VaultTimerTask vaultTimerTask = null;

    @Inject
    UpdateTimer updateTimer = null;

    /** The logger. */
    protected static final Logger log = Logger.getLogger(UpdateServletListener.class);

    public void contextInitialized(ServletContextEvent event) {
        /**
         * for piwiki
         */
        String piwikServer = MGProperties.getInstance().getValue(MGProperties.PI_WIKI_SERVER);
        String piwikIdSite = MGProperties.getInstance().getValue(MGProperties.PI_WIKI_ID_SITE);
        event.getServletContext().setAttribute("piwikServer", piwikServer);
        event.getServletContext().setAttribute("piwikIdSite", piwikIdSite);
        log.info("piwikServer is " + piwikServer);
        log.info("piwikIdSite is " + piwikIdSite);
        /**
         * for the function of reminding email
         */
        // Start Timer Task
        long interval = Long.parseLong(MGProperties.getInstance().getTimerInterval());
        log.info("UpdateServletListener for" + interval + "ms");
        timer.schedule(vaultTimerTask, getRemainSeconds(), interval);
        /**
         * for the function of product and version
         */
        // default update period is one day
        Long period = 24l * 60 * 60 * 1000;
        String periodDayStr = MGProperties.getInstance().getValue(MGProperties.KEY_UPDATE_PERIOD_DAY);
        if (periodDayStr != null && !"".equals(periodDayStr)) {
            period = (long) (Double.parseDouble(periodDayStr) * period);
        }
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        // 0 <= delayhour's value <=23.5, so update time is between 23:30 and 00:30
        double delayhour = 23.5 - Integer.parseInt(hourFormat.format(new Date()));
        if (delayhour < 0) {
            delayhour = 0;
        }
        Long delay = (long) (delayhour * 60 * 60 * 1000);
        timer = new Timer(true);
        log.info("[product update listener] period......" + period + "milliseconds");
        log.info("[product update listener] delay......" + delay + "milliseconds");
        log.info("[product update listener] start......");
        timer.schedule(updateTimer, delay, period);
    }

    public void contextDestroyed(ServletContextEvent event) {
        timer.cancel();
        log.info("[product update listener] stop......");
    }

    public long getRemainSeconds() {
        long now = System.currentTimeMillis();
        now = 86400000L - (now + 28800000L) % 86400000L;
        return (now + 1800000);
    }
}
