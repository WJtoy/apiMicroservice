/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.md.GlobalMappingSalesSubFlagEnum;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerAccountProfile;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleListAdapter;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserAdapter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.beans.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//import com.wolfking.jeesite.common.persistence.DataEntity;

/**
 * 用户Entity
 *
 * @author ThinkGem
 * @version 2013-12-05
 */
@JsonAdapter(UserAdapter.class)
public class User extends LongIDDataEntity<User> {

    /**
     * 用户类型
     */
    public static final Integer USER_TYPE_SYSTEM = 1;//普通系统用户
    public static final Integer USER_TYPE_SERVICE = 2;//客服
    public static final Integer USER_TYPE_CUSTOMER = 3;//厂商 主账号
    public static final Integer USER_TYPE_SUBCUSTOMER = 4;//厂商子账号
    public static final Integer USER_TYPE_SEARCH_CUSTOMER = 9;//厂商vip账号
    public static final Integer USER_TYPE_ENGINEER = 5;//安维
    public static final Integer USER_TYPE_CONSUMER = 6;//消费者（终端用户）
    public static final Integer USER_TYPE_SALES = 7;//业务人员
    public static final Integer USER_TYPE_INNER = 8;//内部账号,可设置负责区域
    public static final Integer USER_TYPE_GROUP = 10;//事业部账号
    public static final Integer USER_TYPE_FINANCE = 11; //财务账号   //2020-5-9

    private static final long serialVersionUID = 1L;
    public static final long APPROVE_FLAG_NO = 2;
    public static final long USER_TYPE_KEFU = 2;
    private Office company;    // 归属公司
    private Office office;    // 归属部门
    private String loginName = "";// 登录名
    private String password = "";// 密码
    //	private String no;		// 工号
    private String name = "";    // 姓名
    private String email = "";    // 邮箱
    private String phone = "";    // 电话
    private String mobile = "";    // 手机
    private String qq = "";        // QQ
    private Integer userType = 1;// 用户类型：公司账户，客户，维修员
    @GsonIgnore
    private String userTypeName = "";//用户类型名称，为微服务新增

    private String loginIp = "";    // 最后登陆IP
    private Date loginDate;    // 最后登陆日期
    private Integer subFlag = 0;    //是否为子帐号标记，1：是子帐号　0：不是子帐号
    private String photo = "";    // 头像

    private Long engineerId = 0l; //安维人员ID，与md_engineer.id关联
    private Long servicePointId = 0L; // 安维人员网点id，与md_servicepoint.id关联 // add on 2019-9-16 //ServicePoint微服务时加此属性
    private CustomerAccountProfile customerAccountProfile; // 客户账号属性，与md_customer_account_profile.id关联
    @GsonIgnore
    private List<Long> customerAccountProfileIds;    //客户账号id列表 // add 2019-7-29

    //业务员id,查询用
    @GsonIgnore
    private Long salesId;

    @GsonIgnore
    private String oldLoginName = "";// 原登录名
    @GsonIgnore
    private String newPassword = "";    // 新密码
    @GsonIgnore
    private String oldLoginIp = "";    // 上次登陆IP
    @GsonIgnore
    private Date oldLoginDate;    // 上次登陆日期
    @GsonIgnore
    private String orderBy = "";

    private Integer appLoged = 0; //是否app登录过

    private int shortMessageFlag = 0; //是否接收短信通知 1:接收  ->from servicepoint
    //安维
    private int appFlag = 0;  //安维是否可以在手机上接单
    private Role role;    // 根据角色查询用户条件
    private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
    @GsonIgnore
    private List<Area> areaList = Lists.newArrayList(); // 区域列表

    private String areas = "";//保存时提交，使用Json格式，包含id,type
    //区域权限
    private String regions = "";//保存时提交，使用Json格式，包含provinceId,cityId,areaId,type

    @JsonAdapter(CustomerSimpleListAdapter.class)
    private List<Customer> customerList = Lists.newArrayList(); // 授权客户

    private Set<Long> customerIds = Sets.newHashSet(); //授权客户id

    private List<Long> productCategoryIds = Lists.newArrayList();//

    public User() {
        super();
    }

    public User(Long id) {
        super(id);
    }

    public User(Long id, String loginName) {
        super(id);
        this.loginName = loginName;
    }

    public User(Long id, String name, String mobile) {
        super(id);
        this.mobile = mobile;
        this.name = name;
    }

    public User(Role role) {
        super();
        this.role = role;
    }

    //region getset
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getSubFlag() {
        return subFlag;
    }

    public void setSubFlag(Integer subFlag) {
        this.subFlag = subFlag;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }

    @JsonIgnore
    public Long getCompanyId() {
        return company == null ? 0l : company.getId();
    }

    @JsonIgnore
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @JsonIgnore
    public Long getOfficeId() {
        return office == null ? 0l : office.getId();
    }

    @Length(min = 1, max = 20, message = "登录名长度必须介于 1 和 20 之间")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JsonIgnore
    @Length(max = 100, message = "密码长度必须介于 1 和 100 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min = 1, max = 20, message = "姓名长度必须介于 1 和 20 之间")
    public String getName() {
        return name;
    }

    @Length(min = 0, max = 11, message = "QQ号长度应小于12")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Email(message = "邮箱格式不正确")
    @Length(min = 0, max = 100, message = "邮箱长度必须小于100")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 0, max = 20, message = "电话长度必须小于20")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(max = 11, message = "手机长度应为11位")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemarks() {
        return remarks;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public CustomerAccountProfile getCustomerAccountProfile() {
        return customerAccountProfile;
    }

    public void setCustomerAccountProfile(CustomerAccountProfile customerAccountProfile) {
        this.customerAccountProfile = customerAccountProfile;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null) {
            return loginIp;
        }
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOldLoginDate() {
        if (oldLoginDate == null) {
            return loginDate;
        }
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @JsonIgnore
    public List<Long> getRoleIdList() {
        List<Long> roleIdList = Lists.newArrayList();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        roleList = Lists.newArrayList();
        for (Long roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            roleList.add(role);
        }
    }

    /**
     * 用户拥有的角色英文名称字符串, ','分隔.
     */
    public Set<String> getRoleEnNames() {
        if (roleList == null || roleList.size() == 0) {
            return Sets.newHashSet();
        }
        return roleList.stream().map(t -> t.getEnname()).collect(Collectors.toSet());
    }

    public static boolean isAdmin(Long id) {
        return id != null && id == 1L;
    }

    public List<Long> getAreaIds() {
        List<Long> nameIdList = Lists.newArrayList();
        for (Area area : areaList) {
            nameIdList.add(area.getId());
        }
        return nameIdList;
    }

    public void setAreaIds(List<Long> areaIds) {
        areaList = Lists.newArrayList();
        if (areaIds != null) {
            for (Long areaId : areaIds) {
                Area area = new Area();
                area.setId(areaId);
                areaList.add(area);
            }
        }
    }

    @JsonIgnore
    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }


    //public List<Long> getCustomerIds()
    //{
    //	List<Long> nameIdList = Lists.newArrayList();
    //	for (Customer customer : customerList)
    //	{
    //		nameIdList.add(customer.getId());
    //	}
    //	return nameIdList;
    //}
    //
    //public void setCustomerIds(List<Long> customerIds)
    //{
    //	customerList = Lists.newArrayList();
    //	if (customerIds != null)
    //	{
    //		for (Long customerId : customerIds)
    //		{
    //			Customer customer = new Customer();
    //			customer.setId(customerId);
    //			customerList.add(customer);
    //		}
    //	}
    //}

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }


    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public Integer getAppLoged() {
        return appLoged;
    }

    public void setAppLoged(Integer appLoged) {
        this.appLoged = appLoged;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public int getShortMessageFlag() {
        return shortMessageFlag;
    }

    public void setShortMessageFlag(int shortMessageFlag) {
        this.shortMessageFlag = shortMessageFlag;
    }

    public int getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(int appFlag) {
        this.appFlag = appFlag;
    }

    public Set<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(Set<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public List<Long> getProductCategoryIds() {
        return productCategoryIds;
    }

    public void setProductCategoryIds(List<Long> productCategoryIds) {
        this.productCategoryIds = productCategoryIds;
    }

    public List<Long> getCustomerAccountProfileIds() {
        return customerAccountProfileIds;
    }

    public void setCustomerAccountProfileIds(List<Long> customerAccountProfileIds) {
        this.customerAccountProfileIds = customerAccountProfileIds;
    }

    public Long getServicePointId() {
        return servicePointId;
    }

    public void setServicePointId(Long servicePointId) {
        this.servicePointId = servicePointId;
    }

    //endregion getset

    //region 角色判断

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    @Transient
    public boolean isCustomer() {
        return this.userType == USER_TYPE_CUSTOMER || this.userType == USER_TYPE_SUBCUSTOMER
                || this.userType == USER_TYPE_SEARCH_CUSTOMER;
    }

    public boolean isKefu() {
        return this.userType == USER_TYPE_KEFU;
    }

    // 是否是内部帐号，内部帐号可分配区域
    public boolean isInnerAccount() {
        return this.userType == USER_TYPE_INNER;
    }

    public boolean isKefuLeader() {
        if (this.getRoleList() != null && this.getRoleList().stream().filter(t -> t.getEnname().equalsIgnoreCase("kefuleader")).count() > 0) {
            return true;
        }

        return false;
    }

    @Transient
    public boolean isEngineer() {
        return this.userType == USER_TYPE_ENGINEER;
    }

    @Transient
    public boolean isSystemUser() {
        return this.userType == USER_TYPE_SYSTEM || this.userType == USER_TYPE_SERVICE
                || this.userType == USER_TYPE_SALES || this.userType == USER_TYPE_INNER
                || this.userType == USER_TYPE_GROUP;
    }

    /**
     * 是否为业务员(user_type = 7,sub_flag=1,业务主管：by role)
     */
    @Transient
    public boolean isSaleman() {
        // 系统中 业务 角色的 ID
        //return this.getRoleIdList().contains(USER_ROLE_SALEMAN);
        if (this.userType == USER_TYPE_SALES) {
            return true;
        }
        //业务主管判断，暂时取消
        //if(this.getRoleList()!=null && this.getRoleList().stream().filter(t->t.getEnname().equalsIgnoreCase("salesleader")).count()>0){
        //	return true;
        //}
        return false;
    }

    /**
     * 是否为业务员
     *
     * @return
     */
    @Transient
    public boolean isSalesPerson() {
        // add on 2020-3-21
        // 系统中 业务 角色的 ID
        //return this.getRoleIdList().contains(USER_ROLE_SALEMAN);
        if (this.userType == USER_TYPE_SALES && this.subFlag.equals(GlobalMappingSalesSubFlagEnum.SALES.getValue())) {
            return true;
        }
        return false;
    }

    /**
     * 是否为跟单员(user_type = 7,sub_flag=2,业务主管：by role)
     */
    @Transient
    public boolean isMerchandiser() {
        // add on 2019-11-15
        // 系统中 业务 角色的 ID
        //return this.getRoleIdList().contains(USER_ROLE_SALEMAN);
        if (this.userType == USER_TYPE_SALES && this.subFlag.equals(GlobalMappingSalesSubFlagEnum.MERCHANDISER.getValue())) {
            return true;
        }
        return false;
    }

    /**
     * 是否是突击客服(user_type = 2,sub_flag=3)
     */
    public boolean isRushKefu() {
        if (this.userType == USER_TYPE_KEFU && this.subFlag.equals(KefuTypeEnum.Rush.getCode())) {
            return true;
        }
        return false;
    }


    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    //endregion 角色判断
}