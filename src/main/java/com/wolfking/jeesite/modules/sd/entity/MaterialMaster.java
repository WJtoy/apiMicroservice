package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 配件申请主表
 */
@Data
@NoArgsConstructor
@ToString
public class MaterialMaster extends LongIDDataEntity<MaterialMaster> {
    private static final long serialVersionUID = 1L;

    public static final Integer APPLY_TYPE_ZIGOU = 1;// 自购;
    public static final Integer APPLY_TYPE_CHANGJIA = 2;//向厂家申请

    public static final Integer STATUS_NEW = 1; // New 待确认
    public static final Integer STATUS_APPROVED = 2;//Approved 待发货
    public static final Integer STATUS_SENDED = 3;//Sended 已发货
    public static final Integer STATUS_CLOSED = 4;//Closed 已完成
    public static final Integer STATUS_REJECT = 5;//Reject 已驳回
    public static final Integer STATUS_ABNORMAL = 6;//Closed 异常收件

    //申请单类型
    public enum MaterialType {
        NONE,
        APPLY,//配件申请
        RETURN_APPLY //返件申请
    }

    //数据来源
    private Integer dataSource = 0;
    private Long orderId;
    private Long orderDetailId;
    private Long productId;//产品
    //dict:MaterialType 1:配件 2:返件
    //private Dict materialType = new Dict("1","配件申请");//comment at 2019-07-01 返件单独立保存，此栏位无意义
    //dict:material_apply_type 1:向师傅购买(自购) 2:厂家寄发
    private Dict applyType = new Dict("0", "");
    //申请次序，第几次申请
    private int applyTime = 1;
    private Dict status = new Dict("1", "待确认");
    private Long applyId = 0l;
    private Double totalPrice = 0.00;
    //dict:express_type
    private Dict expressCompany = new Dict("", "");
    private String expressNo = "";
    private Integer returnFlag = 0;//是否需要返件
    private Integer recycleFlag = 0;//是否需要回收
    private String quarter = "";//数据库分片，与订单相同
    private List<MaterialProduct> productInfos = Lists.newArrayList();
    //单身
    private List<MaterialItem> items = Lists.newArrayList();
    //附件，如配件照片
    private List<MaterialAttachment> attachments = Lists.newArrayList();

    //辅助字段
    @GsonIgnore
    @ToStringExclude
    private Map<Product, List<MaterialItem>> mateirals;//配件申请用，按产品分开显示配件

    @ToStringExclude
    private Map<String, List<MaterialItem>> productGroup;//配件申请用，按产品提交配件

    @ToStringExclude
    private Map<String, Product> products;//配件申请用，按产品提交配件

    //region 修改字段

    //产品
    private Product product;//原 productId

    //endregion

    //region 新增字段 2019/05/31

    // 订单号
    private String orderNo;
    // 厂商
    private Customer customer;
    // 店铺ID
    private String shopId;
    // 用户姓名
    private String userName;
    // 用户电话
    private String userPhone;
    // 区县
    private Area area;
    // 街道
    private Area subArea;
    // 详细地址
    private String userAddress;
    // 跟踪状态
    //1:待确认
    //2:待申请
    //3:厂家缺件
    private Dict pendingType;
    private Date pendingDate;
    // 最新跟踪内容
    private String pendingContent;

    //endregion

    //region 新增字段 2019/06/13
    // 关闭人
    private User closeBy;
    // 关闭日期
    private Date closeDate;
    // 关闭备注
    private String closeRemark = "";
    //异常收件时的类型
    private String closeType;
    // 配件单号
    private String masterNo = "";
    // 第三方单号
    private String thrdNo = "";
    // 产品id列表
    private String productIds = "";
    // 产品名称
    private String productNames = "";
    // 下单人
    private String orderCreator = "";
    //endregion

    //签收时间
    private Date signAt;

    //突击标识
    private Integer canRush = 0;

    //省Id
    private Long provinceId = 0L;

    //市Id
    private Long cityId = 0L;

    //品类id
    private Long productCategoryId = 0L;

    /**
     * 收件人
     */
    private String receiver = "";

    /**
     * 收件人电话
     */
    private String receiverPhone = "";

    /**
     * 收件地址省id
     */
    private Long receiverProvinceId = 0L;

    /**
     * 收件地址市id
     */
    private Long receiverCityId = 0L;

    /**
     * 收件地址区县id
     */
    private Long receiverAreaId = 0L;

    /**
     * 收件地址
     */
    private String receiverAddress = "";

    /**
     * 客户b2b工单(用于传给新迎燕微服务)
     */
    private Long b2bOrderId = 0L;

    /**
     * 收件类型(1:用户收件,2:师傅收件)
     */
    private Integer receiverType = 0;

    /**
     * 工单服务描述
     * */
    private String description="";

    /**
     * 客服类型 0-大客服 1-自动客服 2-突击客服 3-vip客服
     * */
    private Integer kefuType = 0;

}
