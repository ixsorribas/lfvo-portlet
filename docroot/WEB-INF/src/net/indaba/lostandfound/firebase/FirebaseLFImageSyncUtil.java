package net.indaba.lostandfound.firebase;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.portlet.PortletProps;

import net.indaba.lostandfound.model.Item;
import net.indaba.lostandfound.model.LFImage;
import net.indaba.lostandfound.service.ItemLocalServiceUtil;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

/**
 * Firebase sync utility class. Considers objects as shared; uses the
 * autogenerated Firebase key
 * 
 * @author yzhan
 *
 */
public class FirebaseLFImageSyncUtil {

	private static FirebaseLFImageSyncUtil instance;
	
	FirebaseItemSyncUtil itemUtil = FirebaseItemSyncUtil.getInstance();

	private String FB_URI = "https://brilliant-torch-8285.firebaseio.com/images";
	private String FB_Item_URI = "https://brilliant-torch-8285.firebaseio.com/items";

	private FirebaseLFImageSyncUtil() {
		super();
	}

	public static FirebaseLFImageSyncUtil getInstance() {
		if (instance == null) {
			instance = new FirebaseLFImageSyncUtil();
		}
		return instance;
	}

	public boolean isSyncEnabled() {
		return true;
	}

	public void add(LFImage image)
			throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException, PortalException {
		Firebase firebase = new Firebase(FB_URI);
		Map<String, Object> objectMap = toMap(image);
		FirebaseResponse response = firebase.post(objectMap);
		if (response.getCode() == 200) {
			_log.debug("Firebase create sucessful");
			String fbImageKey = (String) response.getBody().get("name");
			setRelation(image.getItemId(), fbImageKey, true);
		} else {
			_log.debug("Firebase create unsuccessful. Response code: " + response.getCode());
		}
	}

	private void setRelation(long itemId, String fbImageKey, boolean add)
			throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException, PortalException {
		Firebase firebase = new Firebase(FB_Item_URI);
		Item item = ItemLocalServiceUtil.getItem(itemId);
		String fbItemKey = itemUtil.getFirebaseKey(item);

		FirebaseResponse response;
		if (add) {
			Map<String, Object> imagesMap = new HashMap<String, Object>();
			imagesMap.put(fbImageKey, true);
			response = firebase.patch("/" + itemUtil.getItemPath(item) + "/" + fbItemKey + "/images", imagesMap);
		} else {
			response = firebase.delete("/" + itemUtil.getItemPath(item) + "/" + fbItemKey + "/images/" + fbImageKey);
		}
		if (response.getCode() == 200) {
			_log.debug("Firebase relation modified");
		} else {
			_log.debug("Firebase relation not modified");
		}
	}

	public void update(LFImage image) throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException {
		update(image, getFirebaseKey(image));
	}

	public void update(LFImage image, String key)
			throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException {
		Firebase firebase = new Firebase(FB_URI);
		Map<String, Object> map = toMap(image);
		FirebaseResponse response = firebase.patch("/" + key, map);
		if (response.getCode() == 200) {
			_log.debug("Firebase update sucessful");
		} else {
			_log.debug("Firebase update unsuccessful. Response code: " + response.getCode());
		}
	}

	public void addOrUpdate(LFImage image)
			throws FirebaseException, JacksonUtilityException, UnsupportedEncodingException, PortalException {
		String key = getFirebaseKey(image);
		if (key != null) { /* Category exists already in Firebase: update */
			update(image, key);
		} else { /* Category does not exist in Firebase: create */
			add(image);
		}
	}

	public void delete(LFImage image)
			throws FirebaseException, UnsupportedEncodingException, JacksonUtilityException, PortalException {
		Firebase firebase = new Firebase(FB_URI);

		String key = getFirebaseKey(image);
		FirebaseResponse response;
		if (key != null) {
			response = firebase.delete("/" + key);
			if (response.getCode() == 200) {
				_log.debug("Firebase delete sucessful");
				setRelation(image.getItemId(), key, false);
			} else {
				_log.debug("Firebase delete unsuccessful. Response code: " + response.getCode());
			}
		} else {
			_log.debug("Could not find image with id " + image.getPrimaryKeyObj());
		}
	}

	private String getFirebaseKey(LFImage image) throws FirebaseException, UnsupportedEncodingException {
		Firebase firebase = new Firebase(FB_URI);
		firebase.addQuery("orderBy", "\"id\"");
		firebase.addQuery("equalTo", String.valueOf(image.getPrimaryKey()));
		FirebaseResponse response = firebase.get();
		if (response.getCode() == 200) {
			Map<String, Object> responseMap = response.getBody();
			Object[] keys = responseMap.keySet().toArray();
			if (keys.length > 0) {
				return (String) keys[0];
			} else {
				return null;
			}
		} else {
			_log.debug("Firebase get key unsuccessfull. Response code: " + response.getCode());
		}
		return null;
	}

	private Map<String, Object> toMap(LFImage image) {
		// TODO parse description and title maps
		Map<String, Object> imageMap = image.getModelAttributes();
		imageMap.remove("lfImageId");
		imageMap.put("id", image.getPrimaryKey());
		try {
			Blob imageBlob = image.getImage();
			byte[] b = imageBlob.getBytes(1, (int) imageBlob.length());
			String imageString = "data:image/jpeg;base64,";
			imageString += new String(b);
			imageMap.replace("image", imageString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imageMap;
	};

	private LFImage parseMap(Map<String, Object> map) {
		// TODO implement method body
		return null;
	};

	private final Log _log = LogFactoryUtil.getLog(FirebaseLFImageSyncUtil.class);

}
