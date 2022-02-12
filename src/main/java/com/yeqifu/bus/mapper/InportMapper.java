package com.yeqifu.bus.mapper;

import com.yeqifu.bus.entity.Inport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.Sales;
import com.yeqifu.bus.vo.InportVo;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo Mapper 接口
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
public interface InportMapper extends BaseMapper<Inport> {

    List<Sales> loadStatistics(InportVo inportVo);

    //void selectCount();


}
