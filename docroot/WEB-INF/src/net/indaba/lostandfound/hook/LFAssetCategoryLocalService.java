package net.indaba.lostandfound.hook;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceWrapper;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;

import net.indaba.lostandfound.firebase.FirebaseService;
import net.indaba.lostandfound.firebase.FirebaseSynchronizer;
import net.indaba.lostandfound.model.Item;
import net.indaba.lostandfound.service.ItemLocalServiceUtil;

public class LFAssetCategoryLocalService extends
		AssetCategoryLocalServiceWrapper {

	/*
	 * (non-Java-doc)
	 * 
	 * @see com.liferay.asset.kernel.service.AssetCategoryLocalServiceWrapper#
	 * AssetCategoryLocalServiceWrapper(AssetCategoryLocalService
	 * assetCategoryLocalService)
	 */
	public LFAssetCategoryLocalService(
			AssetCategoryLocalService assetCategoryLocalService) {
		super(assetCategoryLocalService);
	}
	
	private FirebaseService<AssetCategory> getFbService() {
		return FirebaseSynchronizer.getInstance().getService(
				AssetCategory.class);
	}

	@Override
	public AssetCategory addCategory(long userId, long groupId,
			long parentCategoryId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, long vocabularyId,
			String[] categoryProperties,
			ServiceContext serviceContext) throws PortalException {
		AssetCategory category = super.addCategory(userId, groupId,
				parentCategoryId, titleMap, descriptionMap,
				vocabularyId, categoryProperties, serviceContext);
		if (getFbService().isSyncEnabled()) {
			getFbService().add(category, null);
		}
		return category;
	}

	@Override
	public AssetCategory updateCategory(long userId, long categoryId,
			long parentCategoryId,
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			long vocabularyId,
			String[] categoryProperties, ServiceContext serviceContext)
					throws PortalException {
		AssetCategory category = super.updateCategory(userId, categoryId,
				parentCategoryId, titleMap, descriptionMap,
				vocabularyId, categoryProperties, serviceContext);
		if (getFbService().isSyncEnabled()) {
			Future<String> fbKey = getFbService().update(category, null);
			List<AssetEntry> assetEntries = AssetEntryLocalServiceUtil
					.getAssetCategoryAssetEntries(categoryId);
			List<Item> items = new ArrayList<Item>();
			for (AssetEntry ae : assetEntries) {
				if (ae.getClassName().equals(Item.class.getName())) {
					Item item = ItemLocalServiceUtil.fetchItem(ae.getClassPK());
					items.add(item);
				}
			}
			FirebaseService<Item> fbItemService = FirebaseSynchronizer
					.getInstance().getService(Item.class);
			getFbService().setRelationOneToMany(category, items, fbItemService,
					fbKey);
		}
		return category;
	}

	@Override
	public AssetCategory deleteCategory(AssetCategory category,
			boolean skipRebuildTree) throws PortalException {
		if (getFbService().isSyncEnabled()) {
			List<Item> items = new ArrayList<Item>();
			FirebaseService<Item> fbItemService = FirebaseSynchronizer
					.getInstance().getService(Item.class);

			Future<Boolean> result = getFbService().setRelationOneToMany(category,
					items, fbItemService, null);
			getFbService().delete(category, result);
		}
		return super.deleteCategory(category, skipRebuildTree);
	}

}