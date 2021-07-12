package com.wolfking.jeesite.ms.b2bcenter.sd.entity;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.Data;

import java.util.List;

/**
 * B2B工单转换数据模型
 */
@Data
public class B2BOrderVModel extends B2BOrder {

    public static final MSErrorCode ERROR_CODE_B2BORDER_PARAMETER_INCOMPLETE = new MSErrorCode(-10401, "工单参数不完整");
    public static final MSErrorCode ERROR_CODE_B2BORDER_IS_CONVERTED = new MSErrorCode(-10001, "工单已被转换");
    public static final MSErrorCode ERROR_CODE_B2BORDER_ADDRESS_PARSE_FAILURE = new MSErrorCode(-10101, "工单的地址解析失败");
    public static final MSErrorCode ERROR_CODE_B2BORDER_AREA_HAS_NO_KEFU = new MSErrorCode(-10102, "区域暂未分配跟进客服");
    public static final MSErrorCode ERROR_CODE_B2BORDER_PRODUCT_HAS_NO_SERVICE_PRICE = new MSErrorCode(-10103, "产品未定义服务价格");
    public static final MSErrorCode ERROR_CODE_B2BORDER_HAS_NO_MATCH_PRODUCT = new MSErrorCode(-10104, "无匹配产品");
    public static final MSErrorCode ERROR_CODE_B2BORDER_HAS_NO_MATCH_SERVICETYPE = new MSErrorCode(-10105, "无匹配服务");
    public static final MSErrorCode ERROR_CODE_B2BORDER_INVALID_PRODUCT_QTY = new MSErrorCode(-10106, "产品数量应大于0");
    public static final MSErrorCode ERROR_CODE_B2BORDER_INVALID_PAYMENTTYPE = new MSErrorCode(-10107, "厂商未设置结算方式");
    public static final MSErrorCode ERROR_CODE_B2BORDER_HAS_NO_CUSTOMER_MAPINGINFO = new MSErrorCode(-10108, "无匹配关联的厂商信息");
    public static final MSErrorCode ERROR_CODE_B2BORDER_BALANCE_INSUFFICIENT = new MSErrorCode(-10201, "客户账户余额不足");
    public static final MSErrorCode ERROR_CODE_B2BORDER_ORDERITEM_TOOMANY = new MSErrorCode(-10301, "工单有多个工单子项，请人工处理");

    public static final User b2bUser = new User(3L, "B2B帐号", "");

    public static final String CANBO_B2BORDER_DESCRIPTION = "B2B工单，请务必规范操作！";

    public static final int FIELD_LENGTH_PRODUCT_SPEC = 40;


    public static String leftSubString(String content, int length) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        return StringUtils.left(content, length);
    }

    /**
     * B2B工单所属的客户
     */
    private Customer customer;

    /**
     * B2B工单所属的店铺名称
     */
    private B2BCustomerMapping customerMapping;

    /**
     * 处理进度
     */
    private String processFlagName;

    /**
     * 工单信息是否是只读
     */
    private Boolean isReadOnly = false;

    /**
     * 数据源名称
     */
    private String dataSourceName = "";

    /**
     * 数据源默认的店铺ID
     */
    private String defaultShopId = "";

    public int getOrderItemQty() {
        int qty = 0;
        if (getItems() != null) {
            qty = getItems().size();
        }
        return qty;
    }

    /**
     * 检查B2B工单中的产品数量
     *
     * @return
     */
    public int getProductQty() {
        int qty = 0;
        if (getItems() != null && getItems().size() > 0) {
            for (B2BOrder.B2BOrderItem item : getItems()) {
                qty = qty + StringUtils.toInteger(item.getQty());
            }
        }
        return qty;
    }

    /**
     * 获取工单中第一工单子项的产品编码
     *
     * @return
     */
    public String getFirstProductCode() {
        return getFirstProduct() != null ? getFirstProduct().getProductCode() : "";
    }

    public String getAllProductCode() {
        List<String> productCodeList = Lists.newArrayList();
        if (getItems() != null && getItems().size() > 0) {
            for (B2BOrder.B2BOrderItem item : getItems()) {
                productCodeList.add(StringUtils.toString(item.getProductCode()));
            }
        }
        return StringUtils.join(productCodeList, "、");
    }

    public String getAllProductSpec() {
        List<String> productSpecList = Lists.newArrayList();
        if (getItems() != null && getItems().size() > 0) {
            for (B2BOrder.B2BOrderItem item : getItems()) {
//                productSpecList.add(StringUtils.toString(item.getProductSpec()));
                productSpecList.add(leftSubString(item.getProductSpec(), FIELD_LENGTH_PRODUCT_SPEC));
            }
        }
        return StringUtils.join(productSpecList, "、");
    }

    public String getAllB2bWarrantyCodes() {
        List<String> b2bWarrantyCodeList = Lists.newArrayList();
        if (getItems() != null && getItems().size() > 0) {
            for (B2BOrder.B2BOrderItem item : getItems()) {
                if (StringUtils.isNotBlank(item.getB2bWarrantyCode())) {
                    b2bWarrantyCodeList.add(item.getB2bWarrantyCode());
                }
            }
        }
        return StringUtils.join(b2bWarrantyCodeList, "、");
    }

    public String getAllProductName() {
        List<String> productCodeList = Lists.newArrayList();
        if (getItems() != null && getItems().size() > 0) {
            for (B2BOrder.B2BOrderItem item : getItems()) {
                if (StringUtils.isNotBlank(item.getProductName())) {
                    productCodeList.add(item.getProductName());
                }
            }
        }
        return StringUtils.join(productCodeList, "、");
    }

    public String getAllClassName() {
        List<String> productClassNameList = Lists.newArrayList();
        if (getItems() != null && getItems().size() > 0) {
            for (B2BOrder.B2BOrderItem item : getItems()) {
                if (StringUtils.isNotBlank(item.getClassName())) {
                    productClassNameList.add(item.getClassName());
                }
            }
        }
        return StringUtils.join(productClassNameList, "、");
    }

    /**
     * 获取工单中第一工单子项的产品名称
     *
     * @return
     */
    public String getFirstProductName() {
        return getFirstProduct() != null ? getFirstProduct().getProductName() : "";
    }

    public B2BOrder.B2BOrderItem getFirstProduct() {
        B2BOrder.B2BOrderItem product = null;
        if (getItems() != null && getItems().size() > 0) {
            product = getItems().get(0);
        }
        return product;
    }

    /**
     * 获取工单中第一工单子项的产品规格/型号
     *
     * @return
     */
    public String getFirstProductSpec() {
        return getFirstProduct() != null ? getFirstProduct().getProductSpec() : "";
    }

    /**
     * 获取工单描述信息
     *
     * @return
     */
    public String getOrderDescription() {
        StringBuilder builder = new StringBuilder();
        if (B2BDataSourceEnum.isB2BDataSource(this.getDataSource())) {
            if (StringUtils.isNotBlank(this.getDescription())) {
                builder.append(this.getDescription()).append("——");
            }
            builder.append(CANBO_B2BORDER_DESCRIPTION);
        }
        return builder.toString();
    }

    public String getFirstServiceType() {
        String serviceType = this.getServiceType();
        if (StringUtils.isBlank(serviceType)) {
            B2BOrder.B2BOrderItem product = getFirstProduct();
            if (product != null && StringUtils.isNotBlank(product.getServiceType())) {
                serviceType = product.getServiceType();
            }
        }
        return StringUtils.toString(serviceType);
    }

    public String getFirstWarrantyType() {
        String warrantyType = this.getWarrantyType();
        if (StringUtils.isBlank(warrantyType)) {
            B2BOrder.B2BOrderItem product = getFirstProduct();
            if (product != null && StringUtils.isNotBlank(product.getWarrantyType())) {
                warrantyType = product.getWarrantyType();
            }
        }
        return StringUtils.toString(warrantyType);
    }

    @Override
    public Long getB2bOrderId() {
        Long b2bOrderId = super.getB2bOrderId();
        return b2bOrderId == null ? 0 : b2bOrderId;
    }

    public String getAllProductBrand() {
        String brands = "";
        if (getItems() != null && getItems().size() > 0) {
            List<String> brandList = Lists.newArrayList();
            for (B2BOrder.B2BOrderItem item : getItems()) {
                if (StringUtils.isNotBlank(item.getBrand())) {
                    brandList.add(item.getBrand());
                }
            }
            if (!brandList.isEmpty()) {
                brands = StringUtils.join(brandList, "、");
            } else {
                brands = StringUtils.toString(getBrand());
            }
        }
        return brands;
    }
}
