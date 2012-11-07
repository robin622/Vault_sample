package com.rehat.tools.vault.service.impl;

import java.util.List;

import org.jboss.cache.Cache;
import org.jboss.cache.CacheFactory;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Version;

public class VaultCacheHandler {

	private static Cache<Object, Object> vaultCache;

	public static String PRODUCT_KEY = "vault/product";
	public static String VERSION_KEY = "vault/version";
	public static String EMAIL_KEY = "vault/email";

	private static final int EXPIRATION_TIME = 1800000;

	public static Product getProduct(Product condition) {
		List<Product> products = (List<Product>) vaultCache.get(
				Fqn.fromString(PRODUCT_KEY), PRODUCT_KEY);
		for (Product pro : products) {
			if (condition.getId().equals(pro.getId())) {
				return pro;
			}
		}
		return null;
	}

	public static Version getVersion(Version version) {
		List<Version> versions = (List<Version>) vaultCache.get(
				Fqn.fromString(VERSION_KEY), VERSION_KEY);
		for (Version ver : versions) {
			if (version.getId().equals(ver.getId())) {
				return ver;
			}
		}
		return null;
	}
	
	public static void putToCache(String key, Object vaultToCache){
		vaultCache.clearData(Fqn.fromString(key));
		Long future = new Long(System.currentTimeMillis() + EXPIRATION_TIME);
		vaultCache.put(Fqn.fromString(key),
				ExpirationAlgorithmConfig.EXPIRATION_KEY, future);
		vaultCache.put(Fqn.fromString(key), key, vaultToCache);
	}
	
	public static Object getObject(String key){
		return vaultCache.get(Fqn.fromString(key), key);
	}

	static {
		if (vaultCache == null) {
			final CacheFactory<Object, Object> factory = new DefaultCacheFactory<Object, Object>();

			vaultCache = factory.createCache();

			vaultCache.create();
			vaultCache.start();
		}
	}

}
