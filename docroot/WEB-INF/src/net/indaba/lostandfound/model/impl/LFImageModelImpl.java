/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package net.indaba.lostandfound.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import net.indaba.lostandfound.model.LFImage;
import net.indaba.lostandfound.model.LFImageImageBlobModel;
import net.indaba.lostandfound.model.LFImageModel;
import net.indaba.lostandfound.model.LFImageSoap;
import net.indaba.lostandfound.service.LFImageLocalServiceUtil;

import java.io.Serializable;

import java.sql.Blob;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the LFImage service. Represents a row in the &quot;lfvo_LFImage&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link LFImageModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link LFImageImpl}.
 * </p>
 *
 * @author aritz
 * @see LFImageImpl
 * @see LFImage
 * @see LFImageModel
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class LFImageModelImpl extends BaseModelImpl<LFImage>
	implements LFImageModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a l f image model instance should use the {@link LFImage} interface instead.
	 */
	public static final String TABLE_NAME = "lfvo_LFImage";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "lfImageId", Types.BIGINT },
			{ "itemId", Types.BIGINT },
			{ "image", Types.BLOB }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("lfImageId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("itemId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("image", Types.BLOB);
	}

	public static final String TABLE_SQL_CREATE = "create table lfvo_LFImage (uuid_ VARCHAR(75) null,lfImageId LONG not null primary key,itemId LONG,image BLOB)";
	public static final String TABLE_SQL_DROP = "drop table lfvo_LFImage";
	public static final String ORDER_BY_JPQL = " ORDER BY lfImage.lfImageId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY lfvo_LFImage.lfImageId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.net.indaba.lostandfound.model.LFImage"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.net.indaba.lostandfound.model.LFImage"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.column.bitmask.enabled.net.indaba.lostandfound.model.LFImage"),
			true);
	public static final long ITEMID_COLUMN_BITMASK = 1L;
	public static final long UUID_COLUMN_BITMASK = 2L;
	public static final long LFIMAGEID_COLUMN_BITMASK = 4L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static LFImage toModel(LFImageSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		LFImage model = new LFImageImpl();

		model.setUuid(soapModel.getUuid());
		model.setLfImageId(soapModel.getLfImageId());
		model.setItemId(soapModel.getItemId());
		model.setImage(soapModel.getImage());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<LFImage> toModels(LFImageSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<LFImage> models = new ArrayList<LFImage>(soapModels.length);

		for (LFImageSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.net.indaba.lostandfound.model.LFImage"));

	public LFImageModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _lfImageId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setLfImageId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _lfImageId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return LFImage.class;
	}

	@Override
	public String getModelClassName() {
		return LFImage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("lfImageId", getLfImageId());
		attributes.put("itemId", getItemId());
		attributes.put("image", getImage());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long lfImageId = (Long)attributes.get("lfImageId");

		if (lfImageId != null) {
			setLfImageId(lfImageId);
		}

		Long itemId = (Long)attributes.get("itemId");

		if (itemId != null) {
			setItemId(itemId);
		}

		Blob image = (Blob)attributes.get("image");

		if (image != null) {
			setImage(image);
		}
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return StringPool.BLANK;
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@JSON
	@Override
	public long getLfImageId() {
		return _lfImageId;
	}

	@Override
	public void setLfImageId(long lfImageId) {
		_lfImageId = lfImageId;
	}

	@JSON
	@Override
	public long getItemId() {
		return _itemId;
	}

	@Override
	public void setItemId(long itemId) {
		_columnBitmask |= ITEMID_COLUMN_BITMASK;

		if (!_setOriginalItemId) {
			_setOriginalItemId = true;

			_originalItemId = _itemId;
		}

		_itemId = itemId;
	}

	public long getOriginalItemId() {
		return _originalItemId;
	}

	@JSON
	@Override
	public Blob getImage() {
		if (_imageBlobModel == null) {
			try {
				_imageBlobModel = LFImageLocalServiceUtil.getImageBlobModel(getPrimaryKey());
			}
			catch (Exception e) {
			}
		}

		Blob blob = null;

		if (_imageBlobModel != null) {
			blob = _imageBlobModel.getImageBlob();
		}

		return blob;
	}

	@Override
	public void setImage(Blob image) {
		if (_imageBlobModel == null) {
			_imageBlobModel = new LFImageImageBlobModel(getPrimaryKey(), image);
		}
		else {
			_imageBlobModel.setImageBlob(image);
		}
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(0,
			LFImage.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public LFImage toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (LFImage)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		LFImageImpl lfImageImpl = new LFImageImpl();

		lfImageImpl.setUuid(getUuid());
		lfImageImpl.setLfImageId(getLfImageId());
		lfImageImpl.setItemId(getItemId());

		lfImageImpl.resetOriginalValues();

		return lfImageImpl;
	}

	@Override
	public int compareTo(LFImage lfImage) {
		long primaryKey = lfImage.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof LFImage)) {
			return false;
		}

		LFImage lfImage = (LFImage)obj;

		long primaryKey = lfImage.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		LFImageModelImpl lfImageModelImpl = this;

		lfImageModelImpl._originalUuid = lfImageModelImpl._uuid;

		lfImageModelImpl._originalItemId = lfImageModelImpl._itemId;

		lfImageModelImpl._setOriginalItemId = false;

		lfImageModelImpl._imageBlobModel = null;

		lfImageModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<LFImage> toCacheModel() {
		LFImageCacheModel lfImageCacheModel = new LFImageCacheModel();

		lfImageCacheModel.uuid = getUuid();

		String uuid = lfImageCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			lfImageCacheModel.uuid = null;
		}

		lfImageCacheModel.lfImageId = getLfImageId();

		lfImageCacheModel.itemId = getItemId();

		return lfImageCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(9);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", lfImageId=");
		sb.append(getLfImageId());
		sb.append(", itemId=");
		sb.append(getItemId());

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(16);

		sb.append("<model><model-name>");
		sb.append("net.indaba.lostandfound.model.LFImage");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>lfImageId</column-name><column-value><![CDATA[");
		sb.append(getLfImageId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>itemId</column-name><column-value><![CDATA[");
		sb.append(getItemId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = LFImage.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			LFImage.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _lfImageId;
	private long _itemId;
	private long _originalItemId;
	private boolean _setOriginalItemId;
	private LFImageImageBlobModel _imageBlobModel;
	private long _columnBitmask;
	private LFImage _escapedModel;
}