package com.redhat.tools.vault.web.helper;

import com.redhat.tools.vault.bean.Request;

public class VaultHelper {
	
	public static String getParent(String parent,String contextPath){
		StringBuffer sb=new StringBuffer("");
		if(parent != null){
			String[] pArray1 = parent.split("##");
			if(pArray1!=null && pArray1.length>1){
				sb.append("<a href=").append(contextPath).append("/showRequest/")
				.append(pArray1[1].split("  ")[0]).append(">")
				.append(pArray1[1]).append("&nbsp;&nbsp;")
				.append("<span class='").append(pArray1[0].equals(Request.INACTIVE)?Request.SIGNED:Request.WAITING)
				.append("'>").append(pArray1[0].equals(Request.INACTIVE)?"Signed":pArray1[0])
				.append("</span></a>");
			}
		}
		return sb.toString();
	}
	
	public static String getChildren(String children,String contextPath){
		StringBuffer sb=new StringBuffer("");
		if(children!=null){							
			String[] childArray = children.split(",");
			if(childArray!=null && childArray.length>0){
				for(int i=0;i<childArray.length;i++){
					String[] cArray = childArray[i].split("##");
					if(cArray!=null && cArray.length>1){
						String childId = cArray[1].split("  ")[0];
						String childStr = cArray[1];
						String cStatus = cArray[0];
						sb.append("<a href=").append(contextPath).append("/showRequest/")
						.append(childId).append(">")
						.append(childStr).append("&nbsp;&nbsp;")
						.append("<span class='").append(cStatus.equals(Request.INACTIVE)?Request.SIGNED:Request.WAITING)
						.append("'>").append(cStatus.equals(Request.INACTIVE)?"Signed":cStatus)
						.append("</span></a><br>");
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String getRequestSign(String val){
		String mark="";
		if("signed".equalsIgnoreCase(val)){
			mark=Request.SIGNED;
		}else if("reject".equalsIgnoreCase(val)){
			mark=Request.REJECTED;
		}
		return mark;
	}
	
	public static String replaceCharacter(String old){
		return old.replaceAll("&amp;","&");
	}
}
