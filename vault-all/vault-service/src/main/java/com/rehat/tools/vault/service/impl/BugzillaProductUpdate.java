package com.rehat.tools.vault.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.inject.Inject;
import net.sf.json.JSONObject;
import org.jboss.logging.Logger;
import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.VersionDAO;

/**
 * 
 * @author maying
 *
 */
public class BugzillaProductUpdate {
	
	@Inject
	public ProductDAO productDAO;
	
	@Inject
	public VersionDAO versionDAO;

	/** The logger. */
	protected static final Logger log = Logger.getLogger(BugzillaProductUpdate.class);
	
	public JSONObject productVersionUpdate() throws Exception {
		JSONObject joReturn = new JSONObject();
		String flag = "failed";
		Class.forName("org.teiid.jdbc.TeiidDriver");
		Connection conn = DriverManager.getConnection("jdbc:teiid:EngVDBF@mms://vdb.engineering.redhat.com:31000", "teiid", "teiid");
		try {
			this.productUpdate(conn);
			this.versionUpdate(conn);
			flag = "success";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			try {
				joReturn.put("flag", flag);
				conn.close();
			} catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
		return joReturn;
	}
	
	/** Union bugzilla_products to vault_products */
	private void productUpdate(Connection conn) throws Exception {
		ResultSet bugzilla_products = this.getBugzillaProducts(conn);
		List<Product> vault_products =  productDAO.getAllProducts();
		// vault_products current index
		int i = 0;
		// vault_products length
		int length = vault_products.size();
		// move cursor to the first row if ResultSet is not null, 0 means no row
		bugzilla_products.next();
		while(bugzilla_products.getRow() != 0 && (i < length)) {
			Product vault_product = vault_products.get(i);
			/** update the product which has same id, different attributes (hash compare) */
			if (bugzilla_products.getLong(1) == vault_product.getId()) {
				// hash string of vault product
				String vp_hash_str = vault_product.getName() + vault_product.getDescription() + vault_product.getVotesperuser() +
									 vault_product.getMaxvotesperbug() + vault_product.getVotestoconfirm() + vault_product.getDefaultmilestone() +
									 vault_product.getDepends() + vault_product.getClassification_id() + vault_product.getIsactive();
				// hash string of current bugzilla products
				String bp_hash_str = bugzilla_products.getString(2) + bugzilla_products.getString(3) + bugzilla_products.getInt(4) +
									 bugzilla_products.getInt(5) + bugzilla_products.getInt(6) + bugzilla_products.getString(7) +
									 bugzilla_products.getInt(8) + bugzilla_products.getInt(9) + bugzilla_products.getInt(10);
				if (vp_hash_str.hashCode() != bp_hash_str.hashCode()) {
					this.setVaultProduct(vault_product, bugzilla_products);
					productDAO.updateProduct(vault_product);
				}
				bugzilla_products.next(); i++;
			  /** insert new added product */
			} else if (bugzilla_products.getLong(1) < vault_product.getId()) {
				Product product = new Product();
				product.setId(bugzilla_products.getLong(1));
				this.setVaultProduct(product, bugzilla_products);
				productDAO.saveProduct(product);
				bugzilla_products.next();
			} else if (bugzilla_products.getLong(1) > vault_product.getId()) {
				i++;
			}
		}
		/** insert new added product (rest of bugzilla_products) */
		while (bugzilla_products.getRow() != 0) {
			Product product = new Product();
			product.setId(bugzilla_products.getLong(1));
			this.setVaultProduct(product, bugzilla_products);
			productDAO.saveProduct(product);
			bugzilla_products.next();
		}		
	}
	
	private ResultSet getBugzillaProducts(Connection conn) throws Exception {
		String product_sql = "select products.id, products.name, products.description, products.votesperuser, products.maxvotesperbug, " +
							 "products.votestoconfirm, products.defaultmilestone, products.depends, products.classification_id, products.isactive " +
							 "from BugzillaS.products products order by products.id";
		PreparedStatement statement = conn.prepareStatement(product_sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}
	
	private void setVaultProduct (Product vault_product, ResultSet bugzilla_products) throws Exception {
		vault_product.setName(bugzilla_products.getString(2));
		vault_product.setDescription(bugzilla_products.getString(3));
		vault_product.setVotesperuser(bugzilla_products.getInt(4));
		vault_product.setMaxvotesperbug(bugzilla_products.getInt(5));
		vault_product.setVotestoconfirm(bugzilla_products.getInt(6));
		vault_product.setDefaultmilestone(bugzilla_products.getString(7));
		vault_product.setDepends(bugzilla_products.getInt(8));
		vault_product.setClassification_id(bugzilla_products.getInt(9));
		vault_product.setIsactive(bugzilla_products.getInt(10));
	}
	
	/** Union bugzilla_versions to vault_versions */
	private void versionUpdate(Connection conn) throws Exception {
		ResultSet bugzilla_versions = this.getBugzillaVersions(conn);
		List<Version> vault_versions =  versionDAO.getAllVersions();
		// vault_versions current index
		int i = 0;
		// vault_versions length
		int length = vault_versions.size();
		// move cursor to the first row if ResultSet is not null, 0 means no row
		bugzilla_versions.next();
		while(bugzilla_versions.getRow() != 0 && (i < length)) {
			Version vault_version = vault_versions.get(i);
			/** update the product which has same id, different attributes (hash compare) */
			if (bugzilla_versions.getLong(1) == vault_version.getId()) {
				// hash string of vault version
				String vv_hash_str = vault_version.getProduct_id() + vault_version.getValue();
				// hash string of current bugzilla versions
				String bv_hash_str = bugzilla_versions.getLong(2) + bugzilla_versions.getString(3);
				if (vv_hash_str.hashCode() != bv_hash_str.hashCode()) {
					vault_version.setProduct_id(bugzilla_versions.getLong(2));
					vault_version.setValue(bugzilla_versions.getString(3));
					versionDAO.updateVersion(vault_version);
				}
				bugzilla_versions.next(); i++;
			  /** insert new added version */
			} else if (bugzilla_versions.getLong(1) < vault_version.getId()) {
				Version version = new Version();
				version.setId(bugzilla_versions.getLong(1));
				version.setProduct_id(bugzilla_versions.getLong(2));
				version.setValue(bugzilla_versions.getString(3));
				versionDAO.saveVersion(version);
				bugzilla_versions.next();
			} else if (bugzilla_versions.getLong(1) > vault_version.getId()) {
				i++;
			}
		}
		/** insert new added version (rest of bugzilla_versions) */
		while (bugzilla_versions.getRow() != 0) {
			Version version = new Version();
			version.setId(bugzilla_versions.getLong(1));
			version.setProduct_id(bugzilla_versions.getLong(2));
			version.setValue(bugzilla_versions.getString(3));
			versionDAO.saveVersion(version);
			bugzilla_versions.next();
		}		
	}
	
	private ResultSet getBugzillaVersions(Connection conn) throws Exception {
		String version_sql = "select versions.id, versions.product_id, versions.value " +
							 "from BugzillaS.versions versions order by versions.id";
		PreparedStatement statement = conn.prepareStatement(version_sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}
}
