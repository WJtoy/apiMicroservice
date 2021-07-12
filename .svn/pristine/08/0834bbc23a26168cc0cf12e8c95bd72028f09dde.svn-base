package com.wolfking.jeesite.modules.api.entity.sd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 催单项目明细
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestReminderItem implements Serializable
{
    // 序号
    private int itemNo;
	// 处理人
	@Builder.Default
	private String processor = StringUtils.EMPTY;

	// 处理人 ，见：ReminderCreatorType
	@Builder.Default
	private String processorType = StringUtils.EMPTY;
	private int processorTypeId;

	// 处理时间
	private long processAt;

	// 处理备注
	@Builder.Default
	private String processRemark = StringUtils.EMPTY;

}
