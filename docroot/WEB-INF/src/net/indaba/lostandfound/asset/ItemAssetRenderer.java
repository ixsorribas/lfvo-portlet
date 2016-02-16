package net.indaba.lostandfound.asset;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portlet.asset.model.BaseAssetRenderer;

import net.indaba.lostandfound.model.Item;

public class ItemAssetRenderer extends BaseAssetRenderer {

	private Item _item;
	
	public ItemAssetRenderer(Item item) {
		_item = item;
	}
	
	@Override
    public boolean hasEditPermission(PermissionChecker permissionChecker) {
            
            return true;
    }

    @Override
    public boolean hasViewPermission(PermissionChecker permissionChecker) {

    	return true;
    }
	
	@Override
	public Object getAssetObject() {
		return _item;
	}

	@Override
	public long getGroupId() {
		return _item.getGroupId();
	}

	@Override
	public long getUserId() {
		return _item.getUserId();
	}

	@Override
	public String getUserName() {
		return "NO NAME";
	}

	@Override
	public String getUuid() {
		return null;
	}

	@Override
	public String getClassName() {
		return Item.class.getName();
	}

	@Override
	public long getClassPK() {
		return _item.getItemId();
	}

	@Override
	public String getSummary(PortletRequest portletRequest, PortletResponse portletResponse) {
		return _item.getName();
	}

	@Override
	public String getTitle(Locale arg0) {
		return _item.getName();
	}

	@Override
	public boolean include(HttpServletRequest arg0, HttpServletResponse arg1, String arg2) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}