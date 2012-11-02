package com.redhat.tools.vault.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.web.helper.Attachment;
import com.redhat.tools.vault.web.helper.MGProperties;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
@WebServlet("/VaultFileUpload")
public class VaultFileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/** The logger. */
	private static final Logger log = Logger.getLogger(VaultFileUploadServlet.class);
	
	private Lock lock = new ReentrantLock();

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		String operation = null;
		StringBuffer sb = new StringBuffer();
		try {
			String maxsizeStr = MGProperties.getInstance().getValue(MGProperties.KEY_MAXUPLOAD_SIZE);
			int maxsize = 102400;
			if(maxsizeStr != null && !"".equals(maxsizeStr)){
			    maxsize = Integer.parseInt(maxsizeStr);
			}
			res.setContentType("text/html;charset=UTF-8");
			res.setHeader("Cache-Control", "no-chche");
			String writePath = null;
			operation = req.getParameter("operation");
			res.getWriter().flush();
			if(operation != null) {
				if("list".equals(operation)) {
					String id = req.getParameter("id");
					if(id == null || "".equals(id)) {
						throw new Exception("Cannot obtain attachments.");
					}
					lock.lock();
					try{
					    Attachment.remove(createPath(req));
                        Attachment.copyTo(createPath(req), Integer.parseInt(id));
					}finally{
					    lock.unlock();
					}
				} else if("delete".equals(operation)) {
					String ref = req.getParameter("ref");
					writePath = createPath(req);
					File[] files = this.listFiles(writePath);
					if(files != null) {
						if(ref!=null && !"".equals(ref)){
							for(File file:files) {
								if(file.getName().equals(ref)) {
									String filePath = writePath + "/" + file.getName();
									Attachment.remove(filePath);
									break;
								}
							}	
						}else{
							Attachment.remove(writePath);
						}
					}
				} else if("upload".equals(operation)){
					DiskFileItemFactory factory = new DiskFileItemFactory();
					// Attachment Upload
					factory.setSizeThreshold(1024);
					ServletFileUpload upload = new ServletFileUpload(factory);
					// Set max bytes
					upload.setSizeMax(maxsize*1024);
					List<FileItem> fileItems = (List<FileItem>)upload.parseRequest(req);
										
					Iterator itr = fileItems.iterator();
					// Working Directory
					writePath = createPath(req);
					
					log.debug("writePath"+writePath);
					
					File current = new File(writePath);
					if(!current.exists()) {
						if(!current.mkdirs()) {
							throw new Exception("Cannot create path=" + writePath);
						}
					}
					//itr = fileItems.iterator();
					while (itr.hasNext()) {
						FileItem item = (FileItem)itr.next();
						String itemType = "";
                        String itemfileName = "";
                        if (item.getContentType() != null) {
                            itemType = item.getContentType().toLowerCase();
                        }
                        if (item.getName() != null) {
                            itemfileName = new File(item.getName()).getName();
                        }
                        boolean isAllowedType = false;
                        if (itemType.contains("vnd.oasis.opendocument")
                                || itemType.contains("pdf")
                                || itemType.contains("text")
                                || (itemType.contains("octet-stream")
                                        && !itemfileName.contains(".jsp") && !itemfileName
                                        .contains(".inc"))
                                || itemType.contains("image")
                                || itemType.contains("x-gzip")
                                || itemType.contains("x-bzip")
                                || itemType.contains("x-rar")
                                || itemType.contains("zip")
                                || itemType.contains("x-compress")
                                || itemType.contains("x-tar")
                                || itemType.contains("application/json")) {
                            isAllowedType = true;
                        }
						if (!item.isFormField()) {
							String sFilename = item.getName();
							if (!"".equals(sFilename) && isAllowedType) {
								String fileName= (new File(sFilename)).getName();
								item.write(new File(writePath + "/" + fileName));
							}
						}
						else {
							// do nothing.
						}
					}
				}
			}
			File[] files = this.listFiles(createPath(req));
			if(files != null) {
				for(File file:files) {
					sb.append(Attachment.createFileInfo(file, req.getContextPath()));
				}
			}
			res.getWriter().write(sb.toString());
		} catch (FileUploadException e) {
			res.getWriter().write(e.getMessage());
		} catch (Exception e) {
			res.getWriter().write(e.getMessage());
		}
	}

	private String createPath(HttpServletRequest req) {
		return getServletContext().getRealPath(Attachment.FILEPATH) + "/"
		+ req.getRequestedSessionId();
	}

	private File[] listFiles(String path) {
		File current = new File(path);				
		File[] files = current.listFiles();
		return files;
	}
}