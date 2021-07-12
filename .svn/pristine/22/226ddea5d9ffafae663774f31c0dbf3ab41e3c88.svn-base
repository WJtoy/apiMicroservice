package com.wolfking.jeesite.modules.sys.entity.viewModel;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.sys.entity.Menu;
import com.wolfking.jeesite.modules.sys.entity.adapter.MenuListAdapter;

import java.util.List;

/**
 * Created by yanshenglu on 2017/6/23.
 */
public class MenuTreeModel {

    public MenuTreeModel(){}

    private  Boolean edit = false;
    @JsonAdapter(MenuListAdapter.class)
    private List<Menu> menus = Lists.newArrayList();


    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }
}
