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
 *
 * 返件申请单单头
 * 
 * @author Ryan
 * @date 2019-06-28
 */
@Data
@NoArgsConstructor
@ToString
public class MaterialReturn extends LongIDDataEntity<MaterialReturn>
{
	private static final long serialVersionUID = 1L;

	private String quarter = "";//数据库分片，与订单相同
	//配件单id
	private Long masterId = 0l;
	// 返件单号
	private String returnNo = "";
	//状态
	private Dict status = new Dict("1","待确认");
	// 总金额
	private Double totalPrice =0.00;
	// 申请类型
	// dict:material_apply_type 1:向师傅购买(自购) 2:厂家寄发
	private Dict applyType = new Dict("0","");

	//region 订单信息
	private Long orderId;
	// 订单号
	private String orderNo;
	//产品
	private Product product;//原 productId
	private Long productId;//产品
	// 厂商
	private Customer customer;
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
	// 第三方单号
	private String thrdNo = "";
	// 产品id列表
	private String productIds = "";
	// 产品名称
	private String productNames = "";
	// 下单人
	private String orderCreator = "";
	//endregion

	//region 物流

	private Dict expressCompany = new Dict("","");//dict:express_type
	private String expressNo = "";
	//收件人
	private String receivor = "";
	//电话
	private String receivorPhone = "";
	//地址
	private String receivorAddress = "";
	//签收时间
	private Date signAt;
	//endregion

	//单身
	private List<MaterialReturnItem> items = Lists.newArrayList();
	//附件，如配件照片
	private List<MaterialAttachment> attachments = Lists.newArrayList();

	//region 跟踪进度
	//跟踪历史 MaterialLog与配件申请单共用
	// 跟踪状态
	//1:待确认
	//2:待申请
	//3:厂家缺件
	private Dict pendingType;
	private Date pendingDate;
	// 最新跟踪内容
	private String pendingContent;

	//endregion

	//region 关闭
	// 关闭人
	private User closeBy;
	// 关闭日期
	private Date closeDate;
	// 关闭备注
	private String closeRemark = "";
	//endregion

}
