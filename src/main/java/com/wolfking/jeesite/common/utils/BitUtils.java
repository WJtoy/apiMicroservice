package com.wolfking.jeesite.common.utils;

import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;

import java.util.List;

/**
 * 位运算工具类,采用二进制位保存信息
 * position: 有效值(1)位置 -> 0 ,2,3  代表 第0，2，3位为1  -> 00001101
 * 位置顺序： 大 <—— 0
 * tag/状态值:= 2^value -> 1 , 4 , 8
 */
public class BitUtils {

    /**
     * 将数字按(2的n次方运算的结果相加)相反拆解
     * 6 = 2^1 + 2^2  -> List<1,2>
     * ——> 00000110  第1 和 第2位为1
     * 位置顺序： 大 <—— 0
     *
     * @param tags 状态值,如：6
     */
    public static List getPositions(int tags, Class returnType) {
        List list = Lists.newArrayList();
        if (tags <= 0) {
            return list;
        }
        String binaryStr = Integer.toBinaryString(tags);//57->111001 = 2^5 + 2^4 + 2^3 + 2^0
        binaryStr = StringUtils.reverse(binaryStr);
        for (int i = 0, size = binaryStr.length(); i < size; i++) {
            if (binaryStr.charAt(i) != '0') {
                list.add(returnType.getName().equalsIgnoreCase(Integer.class.getName()) ? i : String.valueOf(i));
            }
        }
        return list;
    }

    /**
     * 将列表中位值标记为1的二进制转十机制
     * 将列表值2的n次方运算的结果相加
     * 如List<Integer> list = Lists.newArrayList(1,2);
     * 00000110  第1 和 第2位为：1
     * ||
     * |--- 位置1
     * ---- 位置2
     * 位置顺序： 大 <—— 0
     * ——> val = 2^1 + 2^2 = 6
     *
     * @param positions 位置列表
     */
    public static int markedAndToTags(List<Integer> positions) {
        int tag = 0;
        for (int i = 0, size = positions.size(); i < size; i++) {
            tag = tag + (1 << positions.get(i));
        }
        return tag;
    }


    /**
     * 原始值 转 状态值
     *
     * @param position 位置
     * @return 状态值
     */
    public static int positionToTag(int position) {
        return 1 << position;
    }

    /**
     * 计算状态位
     * nowTags: 已有状态值
     * tags: 需要添加的状态值
     * 1 | 4 = 5  (或运算)
     */
    public static int addTags(int nowTags, int... tags) {
        for (int value : tags) {
            nowTags |= value;
        }
        return nowTags;
    }

    /**
     * 移除状态位
     * tags: 已有状态值
     * tag: 需要移除的状态值
     * 5 ^ 1 == 0000 0101 ^ 0000 0001 == 4
     */
    public static int delTag(int tags, int tag) {
        if ((tags & tag) != tag) return tags;
        return tags ^ tag;
    }

    /**
     * 是否包含状态位
     * tags: 已有状态值
     * tag: 需要判断的状态值
     * 5 & 1 == 0000 0101 & 0000 0001 == 1
     */
    public static boolean hasTag(int tags, int tag) {
        return (tags & tag) == tag;
    }

}
