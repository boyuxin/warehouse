package com.yeqifu.bus.service;

import com.yeqifu.bus.entity.Sales;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.vo.SalesVo;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-21
 */
public interface ISalesService extends IService<Sales> {

    List<Sales> loadStatistics(SalesVo salesVo);

}
