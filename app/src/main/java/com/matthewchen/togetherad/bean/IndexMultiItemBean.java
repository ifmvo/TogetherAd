package com.matthewchen.togetherad.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/*
 * (●ﾟωﾟ●) 多类型列表
 *
 * Created by Matthew_Chen on 2018/8/21.
 */
public class IndexMultiItemBean implements MultiItemEntity {

    /**
     * 内容
     */
    public static final int TYPE_CONTENT = 1;
    /**
     * 广告 GDT
     */
    public static final int TYPE_AD_GDT = 2;

    /**
     * 广告 BAIDU Mob
     */
    public static final int TYPE_AD_BAIDU = 3;

    /**
     * 广告 穿山甲
     */
    public static final int TYPE_AD_CSJ = 4;

    /**
     * 多类型列表中的类型
     */
    private int itemType;

    /**
     * 内容 bean
     */
    private IndexBean indexBean;

    /**
     * 广告的 Object，放广告的 实体类
     */
    private Object adObject;

    public IndexMultiItemBean(int itemType, IndexBean indexBean) {
        this.itemType = itemType;
        this.indexBean = indexBean;
    }

    public IndexMultiItemBean(int itemType, Object adObject) {
        this.adObject = adObject;
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public IndexBean getIndexBean() {
        return indexBean;
    }

    public void setIndexBean(IndexBean indexBean) {
        this.indexBean = indexBean;
    }

    public Object getAdObject() {
        return adObject;
    }

    public void setAdObject(Object adObject) {
        this.adObject = adObject;
    }
}
