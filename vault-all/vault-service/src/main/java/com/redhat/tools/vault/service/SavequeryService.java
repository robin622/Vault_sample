package com.redhat.tools.vault.service;

import java.util.List;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Savequery;
import com.redhat.tools.vault.bean.Version;

public interface SavequeryService {
    /**
     * 
     * @param username 
     * @return
     * @throws Exception
     */
    public List<Savequery> getUserquery(String username) throws Exception;
    /**
     * 
     * @param contion
     * @return
     * @throws Exception
     */
    public List<Savequery> seachrQuery(Savequery condition) throws Exception;
    /**
     * 
     * @param query
     * @return
     * @throws Exception
     */
    public Long saveQuery(Savequery query) throws Exception;
    /**
     * 
     * @param query
     * @throws Exception
     */
    public void deleteQuery(Savequery query) throws Exception;
    /**
     * 
     * @return
     * @throws Exception
     */
    public List<Product> getAllProduct() throws Exception;
    /**
     * 
     * @param productId
     * @return
     * @throws Exception
     */
    public List<Version> getProdVersion(Long productId) throws Exception;
}
