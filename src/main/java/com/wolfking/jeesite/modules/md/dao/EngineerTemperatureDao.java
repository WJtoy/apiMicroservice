package com.wolfking.jeesite.modules.md.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.wolfking.jeesite.common.persistence.BaseDao;
import com.wolfking.jeesite.modules.api.entity.md.RestGetEngineerTemperaturePage;
import com.wolfking.jeesite.modules.md.entity.EngineerTemperature;
import com.wolfking.jeesite.modules.md.entity.viewModel.EngineerTemperatureSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther wj
 * @Date 2021/1/13 11:29
 */
@Mapper
public interface EngineerTemperatureDao  extends BaseDao {


    Integer insert(EngineerTemperature engineerTemperature);

    List<EngineerTemperature> getEngineerTemperatureList(EngineerTemperatureSearchModel params);
}
