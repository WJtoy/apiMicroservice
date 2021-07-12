package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.md.MDCustomerAddress;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.CustomerAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
@JsonAdapter(CustomerAdapter.class)
public class Customer extends LongIDDataEntity<Customer>
{
    private static final long serialVersionUID = 1L;

    // Fields
    private String code="";            //编码
    private String name="";            //名称
    private String fullName ="";        //全称
    private User sales = new User(0l,"");
//    private String salesMan ="";        //业务员
//    private String salesManPhone ="";   //业务员电话
//    private String salesManQq = "";     //业务员QQ号
    private String address ="";         //地址
    private String zipCode ="";         //邮编
    private String master = "";          //主要负责人
    private String phone = "";           //联系电话
    private String mobile = "";          // 手机号
    private String fax = "";             //传真
    private String email = "";           //邮箱
    private Date contractDate;      //签约日期
    private String projectOwner = "";    //项目负责人
    private String projectOwnerPhone = "";//项目负责人电话
    private String projectOwnerQq = "";   //项目负责人qq号
    private String serviceOwner = "";     //售后负责人
    private String serviceOwnerPhone = "";//售后负责人电话
    private String serviceOwnerQq = "";      //售后负责人qq号
    private String financeOwner = "";        //财务负责人
    private String financeOwnerPhone = "";   //财务负责人电话
    private String financeOwnerQq = "";      //财务负责人qq号
    private String technologyOwner = "";     //技术负责人
    private String technologyOwnerPhone = "";//技术负责人电话
    private String technologyOwnerQq = "";   //技术负责人qq号
    private String defaultBrand = "";        // 默认名牌,在下单时自动填充此品牌
    private int effectFlag = 1;              //是否可下单 2018/04/08
    //短信发送开关，根据开关决定是否发送以下短信(1:发送)：2018/04/12
    // 1.发给用户接单短信已经安排师傅短信
    // 2.预约时间短信
    // 3.完工后发给用户短信，让用户短信回复客评
    private int shortMessageFlag = 1;

    private MdAttachment logo;                //客户商标
//    private String contract = "";            //合同附件
//    private String idcard = "";              //身份证
//    private String businesslicence = "";     //营业执照
//    private String manufactlicense = "";     //生产许可证
    private MdAttachment attachment1;   //合同附件
    private MdAttachment attachment2;   //身份证
    private MdAttachment attachment3;   //营业执照
    private MdAttachment attachment4;   //生产许可证
    private int  useDefaultPrice = 0; //使用默认价，0:自定义价, >0:数据字典维护价格

    private int isFrontShow = 0;         // 是否前台展示
    private int sort = 1;                   // 前台展示排序
    private String description = "";         // 厂商简介
    private int minUploadNumber =1;      //最小上传图片张数
    private int maxUploadNumber =5;      //最大上传图片张数
    private String returnAddress = "";       //客户的返件地址
    private int orderApproveFlag = 1;       //订单审核标志
    private CustomerFinance finance=new CustomerFinance();
    private String productIds = "";
    private int timeLinessFlag = 0; //时效奖励开关 1:开启 0:关闭
    private int urgentFlag = 0; //加急开关 1:开启 0:关闭
    private Dict paymentType;        //支付类型  与CustomerFinance.paymentType 相同保持一致

    //以下为辅助字段
    //客户关联B2B商铺id(md_b2b_customer_map)
    private String shopId = "";

    private List<CustomerAccountProfile> customerAccountProfile;

    private List<MDCustomerAddress> customerAddresses;
    private int reminderFlag=0; //催单标志  // add on 2019-8-26
    private User merchandiser = new User(0L,""); //跟单员    // add on 2019-11-14
    private Integer vipFlag = 0;   //是否为vip客户 // add on 2019-12-9
    private Integer remoteFeeFlag = 0; //远程费用标志
    private Integer contractFlag; //签约标记
    private Integer autoCompleteOrder;  //是否自动完工,0-否，1-是



    private Long userId;
    public Customer() {}

    public Customer(Long id){
        this.id = id;
    }

    public Customer(Long id,String name){
        this.id = id;
        this.name = name;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    @Length(max = 10,message = "客户编码长度不能超过10")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Length(max = 100,message = "客户名称长度不能超过100")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(max = 150,message = "客户全称长度不能超过150")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Length(max = 150,message = "地址长度不能超过150")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(max = 6,message = "邮编长度不能超过6")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Length(max = 20,message = "主要负责人长度不能超过20")
    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    @Length(max = 15,message = "联系电话长度不能超过15")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(max = 15,message = "手机号长度不能超过15")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(max = 15,message = "传真号长度不能超过15")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Length(max = 60,message = "邮箱长度不能超过60")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    @NotNull(message = "请设定业务员")
    public User getSales() {
        return sales;
    }

    public void setSales(User sales) {
        this.sales = sales;
    }

    @Length(max = 20,message = "项目负责人长度不能超过20")
    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    @Length(max = 15,message = "项目负责人电话长度不能超过15")
    public String getProjectOwnerPhone() {
        return projectOwnerPhone;
    }

    public void setProjectOwnerPhone(String projectOwnerPhone) {
        this.projectOwnerPhone = projectOwnerPhone;
    }

    @Length(max = 11,message = "项目负责人QQ号长度不能超过11")
    public String getProjectOwnerQq() {
        return projectOwnerQq;
    }

    public void setProjectOwnerQq(String projectOwnerQq) {
        this.projectOwnerQq = projectOwnerQq;
    }

    @Length(max = 20,message = "售后负责人长度不能超过20")
    public String getServiceOwner() {
        return serviceOwner;
    }

    public void setServiceOwner(String serviceOwner) {
        this.serviceOwner = serviceOwner;
    }

    @Length(max = 15,message = "售后负责人QQ号长度不能超过15")
    public String getServiceOwnerPhone() {
        return serviceOwnerPhone;
    }

    public void setServiceOwnerPhone(String serviceOwnerPhone) {
        this.serviceOwnerPhone = serviceOwnerPhone;
    }

    @Length(max = 11,message = "售后负责人QQ号长度不能超过11")
    public String getServiceOwnerQq() {
        return serviceOwnerQq;
    }

    public void setServiceOwnerQq(String serviceOwnerQq) {
        this.serviceOwnerQq = serviceOwnerQq;
    }

    @Length(max = 20,message = "财务负责人长度不能超过20")
    public String getFinanceOwner() {
        return financeOwner;
    }

    public void setFinanceOwner(String financeOwner) {
        this.financeOwner = financeOwner;
    }

    @Length(max = 15,message = "财务负责人QQ号长度不能超过15")
    public String getFinanceOwnerPhone() {
        return financeOwnerPhone;
    }

    public void setFinanceOwnerPhone(String financeOwnerPhone) {
        this.financeOwnerPhone = financeOwnerPhone;
    }

    @Length(max = 11,message = "财务负责人QQ号长度不能超过11")
    public String getFinanceOwnerQq() {
        return financeOwnerQq;
    }

    public void setFinanceOwnerQq(String financeOwnerQq) {
        this.financeOwnerQq = financeOwnerQq;
    }

    @Length(max = 20,message = "技术负责人长度不能超过20")
    public String getTechnologyOwner() {
        return technologyOwner;
    }

    public void setTechnologyOwner(String technologyOwner) {
        this.technologyOwner = technologyOwner;
    }

    @Length(max = 15,message = "技术负责人QQ号长度不能超过15")
    public String getTechnologyOwnerPhone() {
        return technologyOwnerPhone;
    }

    public void setTechnologyOwnerPhone(String technologyOwnerPhone) {
        this.technologyOwnerPhone = technologyOwnerPhone;
    }

    @Length(max = 11,message = "技术负责人QQ号长度不能超过11")
    public String getTechnologyOwnerQq() {
        return technologyOwnerQq;
    }

    public void setTechnologyOwnerQq(String technologyOwnerQq) {
        this.technologyOwnerQq = technologyOwnerQq;
    }

    @Length(max = 60,message = "默认品牌长度不能超过60")
    public String getDefaultBrand() {
        return defaultBrand;
    }

    public void setDefaultBrand(String defaultBrand) {
        this.defaultBrand = defaultBrand;
    }

    public int getEffectFlag() {
        return effectFlag;
    }

    public void setEffectFlag(int effectFlag) {
        this.effectFlag = effectFlag;
    }

    public MdAttachment getLogo() {
        return logo;
    }

    public void setLogo(MdAttachment logo) {
        this.logo = logo;
    }

    public MdAttachment getAttachment1() {
        return attachment1;
    }

    public void setAttachment1(MdAttachment attachment1) {
        this.attachment1 = attachment1;
    }

    public MdAttachment getAttachment2() {
        return attachment2;
    }

    public void setAttachment2(MdAttachment attachment2) {
        this.attachment2 = attachment2;
    }

    public MdAttachment getAttachment3() {
        return attachment3;
    }

    public void setAttachment3(MdAttachment attachment3) {
        this.attachment3 = attachment3;
    }

    public MdAttachment getAttachment4() {
        return attachment4;
    }

    public void setAttachment4(MdAttachment attachment4) {
        this.attachment4 = attachment4;
    }

    public int getUseDefaultPrice() {
        return useDefaultPrice;
    }

    public void setUseDefaultPrice(int useDefaultPrice) {
        this.useDefaultPrice = useDefaultPrice;
    }

    public int getIsFrontShow() {
        return isFrontShow;
    }

    public void setIsFrontShow(int isFrontShow) {
        this.isFrontShow = isFrontShow;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinUploadNumber() {
        return minUploadNumber;
    }

    public void setMinUploadNumber(int minUploadNumber) {
        this.minUploadNumber = minUploadNumber;
    }

    public int getMaxUploadNumber() {
        return maxUploadNumber;
    }

    public void setMaxUploadNumber(int maxUploadNumber) {
        this.maxUploadNumber = maxUploadNumber;
    }

    @Length(max = 150,message = "返件地址长度不能超过150")
    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public int getOrderApproveFlag() {
        return orderApproveFlag;
    }

    public void setOrderApproveFlag(int orderApproveFlag) {
        this.orderApproveFlag = orderApproveFlag;
    }

    public CustomerFinance getFinance() {
        return finance;
    }

    public void setFinance(CustomerFinance finance) {
        this.finance = finance;
    }

    public List<CustomerAccountProfile> getCustomerAccountProfile() {
        return customerAccountProfile;
    }

    public void setCustomerAccountProfile(List<CustomerAccountProfile> customerAccountProfile) {
        this.customerAccountProfile = customerAccountProfile;
    }
	
	public int getShortMessageFlag() {
        return shortMessageFlag;
    }

    public void setShortMessageFlag(int shortMessageFlag) {
        this.shortMessageFlag = shortMessageFlag;
    }
	
	public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getTimeLinessFlag() {
        return timeLinessFlag;
    }

    public void setTimeLinessFlag(int timeLinessFlag) {
        this.timeLinessFlag = timeLinessFlag;
    }

    public int getUrgentFlag() {
        return urgentFlag;
    }

    public void setUrgentFlag(int urgentFlag) {
        this.urgentFlag = urgentFlag;
    }

    public Dict getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Dict paymentType) {
        this.paymentType = paymentType;
    }

    public int getReminderFlag() {
        return reminderFlag;
    }

    public void setReminderFlag(int reminderFlag) {
        this.reminderFlag = reminderFlag;
    }

    public User getMerchandiser() {
        return merchandiser;
    }

    public void setMerchandiser(User merchandiser) {
        this.merchandiser = merchandiser;
    }

    public Integer getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(Integer vipFlag) {
        this.vipFlag = vipFlag;
    }
    public Integer getRemoteFeeFlag() {
        return remoteFeeFlag;
    }

    public void setRemoteFeeFlag(Integer remoteFeeFlag) {
        this.remoteFeeFlag = remoteFeeFlag;
    }

    public List<MDCustomerAddress> getCustomerAddresses() {
        return customerAddresses;
    }

    public void setCustomerAddresses(List<MDCustomerAddress> customerAddresses) {
        this.customerAddresses = customerAddresses;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getContractFlag() {
        return contractFlag;
    }

    public void setContractFlag(Integer contractFlag) {
        this.contractFlag = contractFlag;
    }

    public Integer getAutoCompleteOrder() {
        return autoCompleteOrder;
    }

    public void setAutoCompleteOrder(Integer autoCompleteOrder) {
        this.autoCompleteOrder = autoCompleteOrder;
    }

    //以下复写方法，用于做list的交集/并集/差集处理
    @Override
    public String toString() {
        return String.format("%s(%s-%S)", id, code,name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.code);
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

}
