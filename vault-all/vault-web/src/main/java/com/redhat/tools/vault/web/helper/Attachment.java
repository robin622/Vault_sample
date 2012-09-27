/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package com.redhat.tools.vault.web.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestMap;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.MD5Util;

/**
 * @author wezhao
 */
public class Attachment {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(Attachment.class);

	public Attachment() {
	}

	public static final int REQUEST = 0;

	public static final String FILEPATH = "files";

	public static final String REQUESTPATH = "request/";

	public static File[] getAttachment(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}

	public static String getTimeStamp(File file) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(file.lastModified());
		return sdf.format(DateUtil.changeToUTCTime(date));
	}
	
	public static String getPath(long id) {
		String to = MGProperties.getInstance().getValue(
				MGProperties.KEY_ATTACHEMENTPATH);
		to += REQUESTPATH + id;
		return to;
	}

	public static String createFileInfo(Request request, String path) {
		StringBuffer sb = new StringBuffer();
		Set<RequestMap> maps = request.getMaps();
		String fileName = "";
		String fileUser = "";
		String fileNameUrl = "";
		String fileSize = "";
		String fileDate = "";
		String fileMD5 = "";
		for (RequestMap map : maps) {
			if (map.getMapname().equals(Request.PROPERTY_REQUEST_ATTACHMENT)
					&& map.getRequestVersion().intValue() == request
							.getRequestVersion().intValue()) {
				fileName = map.getMapvalue();
				log.debug("fileName=" + fileName);
				Map<String, String> dynamic = map.getDynamic();
				fileUser = dynamic.get(Request.PROPERTY_ATTACHMENT_USER);
				log.debug("fileUser=" + fileUser);
				fileSize = dynamic.get(Request.PROPERTY_ATTACHMENT_SIZE);
				log.debug("fileSize=" + fileSize);
				fileDate = dynamic.get(Request.PROPERTY_ATTACHMENT_DATE);
				log.debug("fileDate=" + fileDate);
				fileMD5 = dynamic.get(Request.PROPERTY_ATTACHMENT_MD5);
				log.debug("fileMD5=" + fileMD5);
				if (fileMD5 == null || "".equals(fileMD5)) {
					log.debug("filepath=" + getPath(request.getRequestid())
							+ File.separator + fileName);
					fileMD5 = MD5Util.getFileMD5String(new File(getPath(request
							.getRequestid()) + File.separator + fileName));

				}
				fileMD5 = fileMD5 == null ? "" : fileMD5;
				long kbyte = Integer.parseInt(fileSize) / 1024;
				fileNameUrl = fileName.replace("%", "%25").replace(" ", "%20")
						.replace("+", "%2B").replace("?", "%3F")
						.replace("#", "%23").replace("&", "%26")
						.replace("=", "%3D");
				String line = "<big><a href=" + path + "/download?requestid="
						+ request.getRequestid() + "&file=" + fileNameUrl + ">"
						+ fileName + "</a></big><br>" + "Date:" + fileDate
						+ "&nbsp;Size:" + kbyte + "&nbsp;[Kbyte]&nbsp;By:"
						+ fileUser + "&nbsp; MD5:" + fileMD5 + "<br>";

				sb.append(line);
			}
		}
		return sb.toString();
	}

}
