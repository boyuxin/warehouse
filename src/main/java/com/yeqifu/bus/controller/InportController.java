package com.yeqifu.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.*;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.ProviderMapper;
import com.yeqifu.bus.service.ICustomerService;
import com.yeqifu.bus.service.IGoodsService;
import com.yeqifu.bus.service.IInportService;
import com.yeqifu.bus.service.IProviderService;
import com.yeqifu.bus.vo.GoodsVo;
import com.yeqifu.bus.vo.InportVo;
import com.yeqifu.bus.vo.SalesVo;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.WebUtils;
import com.yeqifu.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
@RestController
@RequestMapping("inport")
public class InportController {

    @Autowired
    private IInportService inportService;

    @Autowired
    private IProviderService providerService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ProviderMapper providerMapper;

    @Autowired
    private ICustomerService customerService;

    /**
     * 查询商品进货
     * @param inportVo
     * @return
     */
    @RequestMapping("loadAllInport")
    public DataGridView loadAllInport(InportVo inportVo){
        IPage<Inport> page = new Page<Inport>(inportVo.getPage(),inportVo.getLimit());
        QueryWrapper<Inport> queryWrapper = new QueryWrapper<Inport>();
        //对供应商进行查询
        queryWrapper.eq(inportVo.getProviderid()!=null&&inportVo.getProviderid()!=0,"providerid",inportVo.getProviderid());
        //对商品进行查询
        queryWrapper.eq(inportVo.getGoodsid()!=null&&inportVo.getGoodsid()!=0,"goodsid",inportVo.getGoodsid());
        //对时间进行查询要求大于开始时间小于结束时间
        queryWrapper.ge(inportVo.getStartTime()!=null,"inporttime",inportVo.getStartTime());
        queryWrapper.le(inportVo.getEndTime()!=null,"inporttime",inportVo.getEndTime());
        //通过进货时间对商品进行排序
        queryWrapper.orderByDesc("inporttime");
        IPage<Inport> page1 = inportService.page(page, queryWrapper);
        List<Inport> records = page1.getRecords();
        for (Inport inport : records) {
            QueryWrapper<Provider> objectQueryWrapper1 = new QueryWrapper<>();
            Provider provider = providerMapper.selectOne(objectQueryWrapper1);
            if (provider!=null){
                //设置供应商姓名
                inport.setProvidername(provider.getProvidername());
            }
            QueryWrapper<Goods> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("id", inport.getGoodsid());
            Goods goods = goodsService.query(objectQueryWrapper);
            if (goods!=null){
                //设置商品名称
                inport.setGoodsname(goods.getGoodsname());
                //设置商品规格
                inport.setSize(goods.getSize());
            }
            Customer customer = customerService.getById(inport.getCustomerid());
            if(null!=customer){
                inport.setCustomername(customer.getCustomername());
            }
        }
        return new DataGridView(page1.getTotal(),page1.getRecords());
    }


    /**
     * 添加进货商品
     * @param inportVo
     * @return
     */
    @RequestMapping("addInport")
    @Transient
    public ResultObj addInport(InportVo inportVo){
        try {
            //获得当前系统用户
            User user = (User) WebUtils.getSession().getAttribute("user");
            //设置操作人
            inportVo.setOperateperson(user.getName());
            //设置进货时间
            inportVo.setInporttime(new Date());

            GoodsVo goodsVo = new GoodsVo();
            goodsVo.setId(inportVo.getGoodsid());
            QueryWrapper<Goods> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("id", inportVo.getGoodsid());
            Goods goods = goodsService.query(objectQueryWrapper);
            Goods goodsUpdate = new Goods();
            goodsUpdate.setId(inportVo.getGoodsid());
            goodsUpdate.setTnumber(goods.getTnumber() + inportVo.getTnumber());
            inportService.save(inportVo);
            goodsService.updateById(goodsUpdate);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 更新进货商品
     * @param inportVo
     * @return
     */
    @RequestMapping("updateInport")
    public ResultObj updateInport(InportVo inportVo){
        try {
            inportService.updateById(inportVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }

    }

    /**
     * 删除进货商品
     * @param id
     * @return
     */
    @RequestMapping("deleteInport")
    public ResultObj deleteInport(Integer id){
        try {
            inportService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 查询所有商品统计
     * @return
     */
    @RequestMapping("loadStatistics")
    public DataGridView loadStatistics(InportVo inportVo){
        List<Sales> sales = inportService.loadStatistics(inportVo);
        setname(sales);
        return new DataGridView(sales);
    }

    private void setname(List<Sales> records) {
        for (Sales sales : records) {
            //设置客户姓名
            Customer customer = customerService.getById(sales.getCustomerid());
            if (null != customer) {
                sales.setCustomername(customer.getCustomername());
            }
            //设置商品名称
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
            goodsQueryWrapper.eq("id", sales.getGoodsid());
            Goods goods = goodsService.query(goodsQueryWrapper);
            if (null != goods) {
                //设置商品名称
                sales.setGoodsname(goods.getGoodsname());
                //设置商品规格
                sales.setSize(goods.getSize());
            }
        }
    }
}

