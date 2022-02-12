package com.yeqifu.bus.service;

import com.yeqifu.bus.entity.Inport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.Sales;
import com.yeqifu.bus.vo.InportVo;
import com.yeqifu.bus.vo.SalesVo;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
public interface IInportService extends IService<Inport> {


    List<Sales> loadStatistics(InportVo inportVo);
}
