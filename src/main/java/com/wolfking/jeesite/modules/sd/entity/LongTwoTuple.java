package com.wolfking.jeesite.modules.sd.entity;

/**
 * @author Zhoucy
 * @date 2018/8/10 16:10
 **/
public class LongTwoTuple extends TwoTuple<Long, Long> {

    public LongTwoTuple() { }

    public LongTwoTuple(Long aElement, Long bElement) {
        this.setAElement(aElement);
        this.setBElement(bElement);
    }

}
