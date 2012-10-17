package com.redhat.tools.vault.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;

import com.redhat.tools.vault.bean.Request;

public interface ReportService {
	public Object[] report(String id);

	public Document doExport(List<Request> requests, HttpServletRequest request, Object objs, Object objs2, Object objs3, Object objs4);
}
