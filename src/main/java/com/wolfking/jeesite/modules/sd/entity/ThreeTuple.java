package com.wolfking.jeesite.modules.sd.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Zhoucy
 * @date 2018/8/10 16:08
 **/
public class ThreeTuple<A, B, C> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private A aElement;

    @Getter
    @Setter
    private B bElement;

    @Getter
    @Setter
    private C cElement;

    public ThreeTuple() {}

    public ThreeTuple(A aElement, B bElement, C cElement) {
        this.aElement = aElement;
        this.bElement = bElement;
        this.cElement = cElement;
    }

}
