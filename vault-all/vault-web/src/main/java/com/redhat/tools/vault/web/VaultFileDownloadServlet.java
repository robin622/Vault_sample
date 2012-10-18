package com.redhat.tools.vault.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.web.helper.Attachment;

/**
 * 
 * @author wguo@redhat.com
 *
 */
@WebServlet("/download")
public class VaultFileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger log = Logger
			.getLogger(VaultFileDownloadServlet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		log.debug("doGet starts.");
		String requestIdStr = (String) req.getParameter("requestid");
		long requestid = -1L;
		if (requestIdStr != null)
			requestid = Long.parseLong(requestIdStr);
		String fileName = (String) req.getParameter("file");
		log.debug("doGet. requestid=" + requestid + " file=" + fileName);
		if (requestid != -1 && fileName != null) {
			String path = Attachment.getPath(requestid);
			File fileOut = new File(path + "/" + fileName);
			log.debug("filePath=" + path + "/" + fileName);
			printOutFile(req, res, fileOut);
		}
		else {
			printOutNotFound(res);
		}
		log.debug("doGet ends.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	protected void printOutFile(HttpServletRequest req,
			HttpServletResponse res, File fileOut) throws ServletException,
			IOException {
		OutputStream os = res.getOutputStream();
		try {
			FileInputStream hFile = new FileInputStream(fileOut);
			BufferedInputStream bis = new BufferedInputStream(hFile);
			res.setContentType("application/octet-stream");
			res.setHeader("Content-Disposition",
					"filename=\"" + fileOut.getName() + "\"");
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = bis.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}

			bis.close();
		}
		catch (IOException e) {
			printOutNotFound(res);
		}
		finally {

			if (os != null) {
				try {
					os.close();
				}
				catch (IOException e) {

				}
				finally {
					os = null;
				}
			}
		}
	}

	private void printOutNotFound(HttpServletResponse res) {
		try {
			OutputStream toClient = res.getOutputStream();
			res.setContentType("text/html;charset=utf-8");
			toClient.write("File not found".getBytes());
			toClient.close();
		}
		catch (IOException e) {
			// do nothing
		}
	}

}