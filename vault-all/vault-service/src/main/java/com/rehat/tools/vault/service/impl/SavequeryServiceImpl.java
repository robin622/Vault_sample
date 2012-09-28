package com.rehat.tools.vault.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Savequery;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.SavequeryDAO;
import com.redhat.tools.vault.dao.VersionDAO;
import com.redhat.tools.vault.service.SavequeryService;

public class SavequeryServiceImpl implements SavequeryService {
    @Inject
    private SavequeryDAO queryDAO;
    @Inject
    private ProductDAO productDAO;
    @Inject
    private VersionDAO versionDAO;
    
    private static final Logger log = Logger.getLogger(SavequeryServiceImpl.class);

    public List<Savequery> getUserquery(String username) throws Exception {
        List<Savequery> querys = null;
        Savequery condition = new Savequery();
        condition.setCreatedby(username);
        querys = queryDAO.get(condition);
        return querys;
    }

    public Long saveQuery(Savequery query) throws Exception {
        Long queryId = null;
        try{
            queryId = queryDAO.save(query);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new Exception("save query failure !");
        }
        return queryId;
    }

    public void deleteQuery(Savequery query) throws Exception {
        try{
            queryDAO.delete(query);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new Exception("");
        }
        
    }

    public List<Savequery> seachrQuery(Savequery condition) throws Exception {
        List<Savequery> querys = null;
        try{
            querys = queryDAO.get(condition);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return querys;
    }

    public List<Product> getAllProduct() throws Exception {
        List<Product> products = null;
        try{
            products = productDAO.get(new Product());
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return products;
    }

    public List<Version> getProdVersion(Long productId) throws Exception {
        List<Version> versions = null;
        Version version = new Version();
        version.setProduct_id(productId);
        try{
            versions = versionDAO.get(version);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return versions;
    }

    
}
