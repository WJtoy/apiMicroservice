package com.wolfking.jeesite.modules.sys.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserRegionAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 帐号区域权限
 * 包含客服，业务，内部帐号等
 */
@JsonAdapter(UserRegionAdapter.class)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegion extends LongIDDataEntity<UserRegion> {

    //帐号id
    private long userId;
    //区域类型,1-国家 2-省 3-市 4-区/县
    private Integer areaType;
    //省
    private long provinceId;
    //市
    private long cityId;
    //区
    private long areaId;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.userId);
        hash = 79 * hash + Objects.hashCode(this.areaType==null?0:this.areaType.intValue());
        hash = 79 * hash + Objects.hashCode(this.provinceId);
        hash = 79 * hash + Objects.hashCode(this.cityId);
        hash = 79 * hash + Objects.hashCode(this.areaId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserRegion other = (UserRegion) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (!Objects.equals(this.areaType, other.areaType)) {
            return false;
        }
        if (!Objects.equals(this.provinceId, other.provinceId)) {
            return false;
        }
        if (!Objects.equals(this.cityId, other.cityId)) {
            return false;
        }
        if (!Objects.equals(this.areaId, other.areaId)) {
            return false;
        }
        return true;
    }

}
