/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.service;

import com.kkl.kklplus.utils.SequenceIdUtils;
import com.wolfking.jeesite.common.config.WebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * ID生成服务
 * @author Ryan
 * @version 2020-05-09
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SequenceIdService {

//	@Value("${sequence.workerid}")
	private int workerid;

//	@Value("${sequence.datacenterid}")
	private int datacenterid;

	@Autowired
    public void setWebProperties(WebProperties webProperties) {
        this.workerid = webProperties.getSequence().getWorkerId();
        this.datacenterid = webProperties.getSequence().getDataCenterId();
    }

    //id generator
	private static SequenceIdUtils sequenceIdUtils;

	@PostConstruct
	public void init() {
		SequenceIdService.sequenceIdUtils = new SequenceIdUtils(workerid,datacenterid);
	}

	/**
	 * 生成id
	 * @return
	 */
	public long nextId(){
		return sequenceIdUtils.nextId();
	}

}
