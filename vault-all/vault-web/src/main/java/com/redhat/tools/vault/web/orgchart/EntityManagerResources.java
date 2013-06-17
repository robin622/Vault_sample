package com.redhat.tools.vault.web.orgchart;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerResources {
    @Produces
    @Vdb
    @PersistenceContext(name = "vdbEntityManager", unitName = "engvdbf")
    private EntityManager vdbEntityManager;

}
