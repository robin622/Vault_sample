package com.redhat.tools.vault.bean;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class Version {

	public static String PROPERTY_ID = "id";

	public static String PROPERTY_VALUE = "value";

	public static String PROPERTY_PRODUCT_ID = "product_id";

	private Long id = null;

	private String value = null;

	private Long product_id = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long productId) {
		product_id = productId;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("[");
		sb.append("id=" + id);
		sb.append(" ,value=" + value);
		sb.append(" ,product_id=" + product_id);
		sb.append("]");
		return sb.toString();
	}
}
