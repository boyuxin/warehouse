package com.yeqifu.bus.mapper;

import com.yeqifu.bus.entity.Sales;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.vo.SalesVo;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB Mapper 接口
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-21
 */
public interface SalesMapper extends BaseMapper<Sales> {

    List<Sales> loadStatistics(SalesVo salesVo);
}
