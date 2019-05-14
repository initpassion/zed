package com.initpassion.cache;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.initpassion.cache.bo.GoodsInfo;
import com.initpassion.cache.service.GoodsInfoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class InitpassionCacheApplicationTests {

    @Resource
    private GoodsInfoService goodsInfoService;

    @Test
    public void insert() {
        for (int i = 0; i < 1000000; i++) {
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.setCreateTime(System.currentTimeMillis());
            goodsInfo.setUpdateTime(System.currentTimeMillis());
            goodsInfo.setCreator("initpassion");
            goodsInfo.setOperator("initpassion");
            goodsInfo.setGoodsCode("g_" + UUID.randomUUID().toString().replace("-", ""));
            goodsInfo.setGoodsName("初始化产品_" + i);
            goodsInfo.setGoodsDesc("desc");
            goodsInfo.setGoodsPrice(1.0D);
            goodsInfo.setGoodsStatus(1);
            goodsInfoService.insert(goodsInfo);
        }
    }

}
