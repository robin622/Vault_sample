package com.redhat.tools.vault.service;

import java.util.List;

import javax.inject.Inject;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.dao.RequestDAO;

public interface RequestService {
	
	public List<Request> getMyRequest(String username);
}
