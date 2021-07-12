package com.wolfking.jeesite.common.config.redis;

/**
 * Created by Jeff on 2017/5/2.
 */
public class RedisConstant {
    /********************************************************************************************
     *  缓存数据库定义
     *********************************************************************************************/
    public enum RedisDBType{
        //系统缓存，shiro等(0)
        REDIS_CONSTANTS_DB,
        //存放SYS数据(1)
        REDIS_SYS_DB,
        //存放MD数据(2)
        REDIS_MD_DB,
        //存放SD数据(3)
        REDIS_SD_DB,
        //存放FI数据(4)
        REDIS_FI_DB,
        //存放全局序列号(5)
        REDIS_SEQ_DB,
        //存放锁(6)
        REDIS_LOCK_DB,
        //临时数据存放(7)
        // 上传附件过程中存放的临时记录，成功后更新至数据库，验证码存放
        // 订单下单统计
        REDIS_TEMP_DB,
        //记录队列处理计数(8)
        REDIS_MQ_DB,
        //APP登录数据(9)
        REDIS_APP_DB,
        //新APP登录数据库(10)
        REDIS_NEW_APP_DB,
        //区域，用于下单地址自动匹配(11)
        REDIS_SYS_AREA,
        //websocket & notice(12)
        REDIS_MS_DB,
        //B2B基本资料(13)
        REDIS_B2B_DB,
        REDIS_DB14,
        REDIS_TEST_DB,
        REDIS_DB16,
        REDIS_DB17,
        //订单列表按地址查询锁(18)
        REDIS_ADDRESS_QUERY_LOCK_DB,
        REDIS_DB19,
        REDIS_DB20,
        REDIS_DB21,
        REDIS_DB22,
        REDIS_DB23,
        REDIS_DB24,
        REDIS_DB25,
        REDIS_DB26,
        REDIS_DB27,
        REDIS_DB28,
        REDIS_DB29,
        REDIS_DB30,
        REDIS_DB31,
        REDIS_DB32,
        REDIS_DB33,
        REDIS_MS_MD
    }

    /********************************************************************************************
     *  SHIRO权限验证
     *********************************************************************************************/
    //客服区域
    public static final String SHIRO_KEFU_AREA = "shiro:area:%s";
    //客服/业务员负责的客户
    public static final String SHIRO_KEFU_CUSTOMER = "shiro:kefu:customers:%s";
    //用户菜单
    public static final String SHIRO_USER_MENU = "shiro:menu:%s";
    //机构
    public static final String USER_CACHE_LIST_BY_OFFICE_ID = "shiro:office:%s";
    //用户角色
    public static final String SHIRO_USER_ROLE = "shiro:role:%s";
    //用户Session
    public static final String SHIRO_USER_SESSION = "shiro:user:session:%s";
    //WebSocket Session(user_id,String类型)
    public static final String WEBSOCKET_SESSION = "WS:SESSION";

    /********************************************************************************************
     *  SYS缓存Key定义
     *********************************************************************************************/
    //数据字典
    public static final String SYS_DICT_TYPE = "DICT:%s";
    public static final String SYS_USER_ID = "user:id:%s";
    public static final String SYS_USER_LOGINNAME = "user:name:%s";
    //帐号区域(hash)
    public static final String SYS_USER_REGION = "USER:REGION:ALL";
    //帐号产品类别(hash)
    public static final String SYS_USER_PRODUCT_CATEGORY = "USER:CATEGORY:ALL";
    //区域，按区域类型分别存储在set中，根据需要做并集等处理
    public static final String SYS_AREA_TYPE = "area:type:%s";
    public static final String SYS_AREA_TYPE_TOWN = "area:type:town:%s";
    //菜单
    public static final String SYS_MENU_ALL_LIST = "all:menu";
    //角色
    public static final String SYS_ROLE_ALL_LIST = "all:role";
    //机构
    public static final String SYS_OFFICE_ALL_LIST = "all:office";

    //部门-帐号
    public static final String SYS_OFFICE_USER = "SYS:OFFICE:USER:%s";//office_id

    /*客服部-帐号
    public static final String SYS_KEFU_USER = "SYS:KEFU:USER:%s";//office_id
    //业务部-帐号
    public static final String SYS_SALES_USER = "SYS:SALES:USER:%s";//office_id
    */
    // 客户-帐号
    public static final String SYS_CUSTOMER_USER = "SYS:CUSTOMER:USER:%s";//customer_id


    /********************************************************************************************
     *  MD缓存Key定义
     *********************************************************************************************/
    //产品分类信息
    public static final String MD_PRODUCT_CATEGORY = "MD:PRODUCT:CATEGORY:LIST";

    //产品分类保险价格信息
    public static final String MD_PRODUCT_CATEGORY_INSURANCE = "MD:PRODUCT:CATEGORY:INSURANCE:LIST";

    //产品分类时效奖金
    public static final String MD_PRODUCT_CATEGORY_TIMELINESS = "MD:PRODUCT:CATEGORY:TIMELINESS:%s";//productCategoryId


    //产品分类品牌
    public static final String MD_PRODUCT_CATEGORY_BRND = "MD:PRODUCT:CATEGORY:BRAND:%s";//productCategoryId


    //产品信息
    public static final String MD_PRODUCT = "MD:PRODUCT:INFO";
    //产品服务信息
    public static final String MD_PRODUCTSERVICETYPE = "MD:PRODUCT:SERVICETYPE:INFO";

    //产品服务信息
    public static final String MD_PRODUCTSERVICETYPE_IDS = "MD:PRODUCT:SERVICETYPE:IDS";
    //产品型号导入
    public static final String MD_TMP_PRODUCT_MODEL = "TMP:PRODUCT:MODEL:%s";//userid
    //所有产品列表
    public static final String MD_PRODUCT_ALL = "MD:PRODUCT:ALL";
    //套组产品列表
    public static final String MD_PRODUCT_SET = "MD:PRODUCT:SET";
    //非套组产品列表
    public static final String MD_PRODUCT_SINGLE = "MD:PRODUCT:SINGLE";
    //产品下的配件列表
    public static final String MD_PRODUCT_MATERIAL = "MD:PRODUCT:MATERIAL:%s";
    //客户关联产品列表
    public static final String MD_PRODUCT_CUSTOMER = "MD:PRODUCT:CUSTOMER:%s";
    //服务网点关联产品列表(未使用，直接缓存价格)
    public static final String MD_PRODUCT_SERVICE_POINT = "MD:PRODUCT:SERVICEPOINT:%s";
    //配件信息
    public static final String MD_MATERIAL = "MD:MATERIAL:INFO";
    //所有配件列表
    public static final String MD_MATERIAL_ALL = "MD:MATERIAL:ALL";
    //客户配件
    public static final String MD_CUSTOMER_MATERIAL = "MD:CUSTOMER:MATERIAL:%s:%s";
    //客户配件分类(zset)
    public static final String MD_MATERIAL_CATEGORY_ALL = "MD:MATERIAL:CATEGORY:ALL";

    //产品完工图片/list
    public static final String MD_PRODUCT_COMPLETE_PIC_ALL = "MD:PRODUCT:COMPLETE:PIC:ALL";

    //客户完工图片配置
    public static final String  MD_CUSTOMER_PRODUCT_COMPLETE_PIC ="MD:CUSTOMER:PRODUCTCOMPLETE:PIC:%s";

    //品牌列表
    public static final String MD_BRAND_ALL = "MD:BRAND:ALL";

    //产品关联配件列表
    public static final String MD_MATERIAL_PRODUCT = "MD:MATERIAL:PRODUCT:%s";
    // 服务类型
    public static final String MD_SERVICE_TYPE ="MD:SERVICETYPE:ALL";
    // 客评项目for短信
    public static final String MD_GRADE ="MD:GRADE:LIST";
    //客评项目for订单
    public static final String MD_ORDER_GRADE ="MD:GRADE:LIST:ORDER";
	//客户
    public static final String MD_CUSTOMER_ALL = "MD:CUSTOMER:ALL";
    //客户服务价格
    public static final String MD_CUSTOMER_PRICE = "MD:CUSTOMER:PRICE:%s";

    //客户加急等级
    public static final String MD_CUSTOMER_URGENT= "MD:CUSTOMER:URGENT:%s";

    //客户时效等级
    public static final String MD_CUSTOMER_TIMELINESS= "MD:CUSTOMER:TIMELINESS:%s";

    //客户产品型号
    public static final String MD_CUSTOMER_CUSTOMERPRODUCTMODEL= "MD:CUSTOMER:CUSTOMERPRODUCTMODEL:%s";

    //客户产品大类品牌
    public static final String MD_CUSTOMER_CATEGORYBRAND= "MD:CUSTOMER:CATEGORYBRAND:%s";

    //加急等级列表
    public static final String MD_URGENTLEVEL_ALL= "MD:URGENTLEVEL:ALL";

    //时效补贴等级列表
    public static final String MD_TIMELINESS_ALL= "MD:TIMELINESS:ALL";

    //区域时效补贴开关(zset)
    public static final String MD_AREA_TIMELINESS_ALL = "MD:AREA:TIMELINESS:ALL";

    //网点
//    public static final String MD_SERVICEPOINT = "MD:SERVICEPOINT:%s";
    //网点
    public static final String MD_SERVICEPOINT_ALL = "MD:SERVICEPOINT:ALL";
    //网点价格
    public static final String MD_SERVICEPOINT_PRICE = "MD:SERVICEPOINT:PRICE:%s";
    //网点-安维
    public static final String MD_SERVICEPOINT_ENGINEER = "MD:SERVICEPOINT:ENGINEER:%s";
    //网点-服务点
    public static final String MD_SERVICEPOINT_STATION = "MD:SERVICEPOINT:STATION:%s";

    //B2B
    //厂商-B2B店铺ID(zSet),%s:DataSource  score:customerId value:List<NameValuePair>
    //根据厂商id查找对应的店铺id
    public static final String B2B_CUSTOMER_TO_SHOPID_LIST = "B2B:CUSTOMER:TO:SHOPID:%s:%s";


    //根据数据源品牌ID获取系统中品牌
    public static final String B2B_BRAND_TO_BRAND = "B2B:BRAND:TO:BRAND:%s";

    //所有店铺-数据源
    public static final String B2B_SHOP_ALL_DATASOURCR = "B2B:SHOP:ALL:%s";

    //B2B店铺ID-厂商(HashMap),%s:DataSource key:shopId value:customerId
    //根据店铺id查找对应的厂商id
    public static final String B2B_SHOPID_TO_CUSTOMER_MAP = "B2B:SHOPID:TO:CUSTOMER:%s";
    //根据店铺ID及产品类目查找工单系统中对应的产品(HashMap),%s:DataSource %s:shopId
    public static final String B2B_CUSTOMER_CATEGORY_TO_PRODUCT_MAP = "B2B:CUSTOMERCATEGORY:TO:PRODUCT:%s:%s";

    //SD
    //订单
    public static final String SD_ORDER = "ORDER:%s";
    //导入订单
    public static final String SD_TMP_ORDER = "TMP:ORDER:%s";//userid
    //订单锁
    public static final String SD_ORDER_LOCK = "order:lock:%s";
    //接单锁
    public static final String SD_ORDER_ACCEPT_LOCK = "engineer:accept:%s";
    //派单锁
    public static final String SD_ORDER_PLAN_LOCK = "order:plan:%s";
    //配件附件
    public static final String SD_MATERIAL_ATTACHE = "material:attachment:%s";
    //工单辅材或服务项目
    public static final String SD_AUXILIARY_MATERIALS = "order:auxiliary:materials:%s";
    //返件附件
    public static final String SD_RETURN_MATERIAL_ATTACHE = "material:return:attachment:%s";
    //导入订单转单锁
    public static final String SD_TMP_ORDER_TRANSFER = "TMP:ORDER:TRANSFER:%s";
    //订单下单统计
    public static final String SD_CREATE_ORDER_LOG = "order:create:log:%s";
    //上门服务操作标记
    public static final String SD_ORDER_DETAIL_FLAG = "order:detail:flag:%s";
    public static final int SD_ORDER_DETAIL_FLAG_TIMEOUT = 300;

    /********************************************************************************************
     *  SEQ缓存Key定义
     *********************************************************************************************/
    // 滚号规则 -- SEQ:OrderNo
    public static final String SEQ_RULE = "SEQ:RULE:%s";
    // 滚号 -- SEQ:ORDER:20170707
    public static final String SEQ_KEY = "SEQ:%s:%s";

    /********************************************************************************************
     *  LOCK缓存Key定义
     *********************************************************************************************/
    // 滚号锁
    public static final String LOCK_SEQ_KEY = "LOCK:SEQ:%s:%s";
    // 对帐锁
    public static final String LOCK_CHARGE_KEY = "LOCK:CHARGE:%s";//orderId
    // 网点在线充值
    public static final String LOCK_CUSTOMER_ONLINECHAGE = "LOCK:CUSTOMER:ONLINECHAGE:%s:%s";//cid,uid
    // 支付宝异步通知
    public static final String LOCK_ALIPAY_SYNC = "LOCK:ALIPAY:SYNC:%s";//trade_no
    //B2B天猫网点基础资料操作锁
    public static final String LOCK_TMALL_MD = "LOCK:TMALL:MD:%s";
    public static final String LOCK_SERVICEPOINT = "lock:servicepoint:%s";

    /********************************************************************************************
     *  队列缓存Key定义
     *********************************************************************************************/
    // 累计队列发送成功
    public static final String MQ_SS = "MQ:SS:%s";
    // 累计队列发送失败
    public static final String MQ_SE = "MQ:SE:%s";
    // 累计队列消费成功
    public static final String MQ_RS = "MQ:RS:%s";
    // 累计队列消费失败
    public static final String MQ_RE = "MQ:RE:%s";

    /********************************************************************************************
     *  提醒缓存Key定义  MS(Message)
     *********************************************************************************************/

    //待读问题反馈
    //按客户统计(哈希,key:customer_id,field:帐号id)
    public static final String MS_FEEDBACK_CUSTOMER = "MS:FEEDBACK:CUSTOMER:%s";
    //按区域统计（哈希,field:area_id)
    public static final String MS_FEEDBACK_KEFUBYAREA = "MS:FEEDBACK:KEFU:AREA";
    //客服按负责的客户统计（哈希,field:customer_id)
    public static final String MS_FEEDBACK_KEFUBYCUSTOMER = "MS:FEEDBACK:KEFU:BYCUSTOMER";

    //待处理问题反馈
    //按客户统计(哈希,key:customer_id,field:帐号id)
    public static final String MS_FEEDBACK_PENDING_CUSTOMER = "MS:FEEDBACK:PENDING:CUSTOMER:%s";
    //按区域统计（哈希,field:area_id)
    public static final String MS_FEEDBACK_PENDING_KEFUBYAREA = "MS:FEEDBACK:PENDING:KEFU:AREA";
    //客服按负责的客户统计（哈希,field:customer_id)
    public static final String MS_FEEDBACK_PENDING_KEFUBYCUSTOMER = "MS:FEEDBACK:PENDING:KEFU:BYCUSTOMER";

    //APP异常
    //按区域统计(哈希,field:area_id)
    public static final String MS_APP_ABNORMALY_KEFUBYAREA = "MS:APP:ABNORMALY:KEFU:AREA";
    //客服按负责的客户统计（哈希,field:customer_id)
    public static final String MS_APP_ABNORMALY_KEFUBYCUSTOMER = "MS:APP:ABNORMALY:KEFU:BYCUSTOMER";
    //FOR 客户
    //public static final String MS_APP_ABNORMALY_CUSTOMER = "MS:APP:ABNORMALY:CUSTOMER";
    /********************************************************************************************
     *  APP缓存Key定义
     *********************************************************************************************/
    public static final String APP_SESSION = "APP:LOGIN:%s";//user id

    /********************************************************************************************
     *  验证码缓存Key定义，存放于REDIS_TEMP_DB，7号数据库
     *********************************************************************************************/
    // 验证码，存活5分钟，VER:注册/重置密码，手机号码
    public static final String VERCODE_KEY = "VER:%d:%s";

    /********************************************************************************************
     * B2B功能Key定义
     *********************************************************************************************/
    public static final String B2B_WORKCARD_TRANSFER_KEY = "B2B:WORKCARD:TRANSFER:%s:%s";//datasource,workcardId
    //自动派单区域半径
    //{0}:将区域id取模后的值，比如2341%50 = 41
    public static final String MD_AREA_AUTO_PLAN_RADIUS = "MD:AREA:PlanRadius:{0}";
    // 网点财务缓存Key定义
    public static final String MD_SERVICEPOINT_FINANCE = "MD:SF:%s";

}
