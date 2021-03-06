package net.indaba.lostandfound.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.portlet.PortletProps;

import net.indaba.lostandfound.model.Item;
import net.indaba.lostandfound.service.ItemLocalServiceUtil;
import net.indaba.lostandfound.util.AppFileUtil;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

/**
 * Portlet implementation class OfficeListPortlet
 */
public class OfficeListPortlet extends MVCPortlet {
 
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		HashMap<String, HashMap<String, Object>> resultado = new HashMap<String, HashMap<String, Object>>();
		
		// *********************
		// 1) Sites de LIFERAY
		// *********************
		List<Group> offices = new ArrayList<Group>();		
		String mainSiteGroupId = com.liferay.util.portlet.PortletProps.get("lfvo.main.site.group.id");		
		if(mainSiteGroupId != null && !"".equals(mainSiteGroupId)){
			try {		
				offices = GroupLocalServiceUtil.getGroups(themeDisplay.getCompanyId(), Long.parseLong(mainSiteGroupId), true);				
				
			} catch (NumberFormatException e) {			
				e.printStackTrace();
				offices = GroupLocalServiceUtil.getGroups(themeDisplay.getCompanyId(), 0, true);				
				
			}			
		} else {
			offices = GroupLocalServiceUtil.getGroups(themeDisplay.getCompanyId(), 0, true);			
		}
		
		// *********************
		// 2) Sites de FIREBASE
		// *********************
		HashMap<String, Object> officesMap = new HashMap<String, Object>();
		try {
			Firebase firebase = new Firebase(PortletProps.get("firebase.url"));
			FirebaseResponse response = firebase.get("/offices");
			officesMap = (HashMap<String, Object>)response.getBody();	
		
		} catch (FirebaseException e2) {
			e2.printStackTrace();
		}		
					
		if( officesMap != null){			
			for(Group office : offices){
				
				HashMap<String, Object> result = new HashMap<String, Object>();
				HashMap<String, Object> officeBD = (HashMap<String, Object>) officesMap.get(String.valueOf(office.getGroupId())); // Site Firebase <> Site Liferay
				if(officeBD!=null){						
										
					//A)  Nombres del site
					String nombre_es = "";
					String nombre_eu = "";
					if(officeBD.get("title")!= null ){						
						HashMap<String, Object> tittle = (HashMap<String, Object>)officeBD.get("title");						
						if(tittle!= null && tittle.get("es")!= null){							
							nombre_es = tittle.get("es").toString();							
						}
						
						if(tittle!= null && tittle.get("eu")!= null){							
							nombre_eu = tittle.get("eu").toString();							
						}
					}
					result.put("nombre_es", nombre_es);
					result.put("nombre_eu", nombre_eu);
										
					String site_name = office.getName();
					result.put("site_name", site_name);
					
					
					// B) Icono del site
					String icono = "";
					if(officeBD.get("icon")!= null && officeBD.get("icon").toString()!= null ){
						icono = officeBD.get("icon").toString();
					}
					result.put("icon", icono);
					
					
					// C) Numero de objetos del site
					int objetos = 0;
					try {				
						List<Item> items = ItemLocalServiceUtil.getItems(office.getGroupId(), -1, -1);
						for(Item miItem : items){						
							if( "".equals(miItem.getType()) || "office".equals(miItem.getType()) ){
								objetos = objetos + 1;
							}
						}
					} catch (PortalException e) {				
						e.printStackTrace();
					}										
					result.put("numObjetos", String.valueOf(objetos));
					
					
					// D) Telefono del cliente
					String telefono = "";
					if(officeBD.get("phoneNumber")!= null && officeBD.get("phoneNumber").toString()!= null ){
						telefono = officeBD.get("phoneNumber").toString();
					}
					result.put("telefono", telefono);
					
					// E) Email del cliente
					String email = "";
					if(officeBD.get("emailAddress")!= null && officeBD.get("emailAddress").toString()!= null ){
						email = officeBD.get("emailAddress").toString();
					}
					result.put("email", email);
										
					// F) URL del cliente
					String url = "";
					if(officeBD.get("url")!= null && officeBD.get("url").toString()!= null ){
						url = officeBD.get("url").toString();
					}
					result.put("url", url);
					
					// G) URL del sitio Lost And Found
					String siteUrl = office.getFriendlyURL();
					result.put("siteUrl", "/web" + siteUrl);
					
					// H) Estado del apk (true si se ha generado)
					Boolean apkGenerated = AppFileUtil.apkExistsForSite(office.getGroupId());
					result.put("apkGenerated", apkGenerated);
					
					resultado.put(String.valueOf(office.getGroupId()), result);
				}
			}
		}

		renderRequest.setAttribute("resultado", resultado);		
		super.doView(renderRequest, renderResponse);
	}
	
	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		
		long groupId = Long.valueOf(resourceRequest.getParameter("groupId"));
		File file = AppFileUtil.getApkForSite(groupId);
		
		String contentType = "application/vnd.android.package-archive";
		
		FileInputStream in = new FileInputStream(file);
		
		HttpServletResponse httpRes = PortalUtil.getHttpServletResponse(resourceResponse);
		HttpServletRequest httpReq = PortalUtil.getHttpServletRequest(resourceRequest);
		ServletResponseUtil.sendFile(httpReq,httpRes, file.getName(), in, contentType);
		
		super.serveResource(resourceRequest, resourceResponse);
	}

}
