package com.southwind.feignfallback;

import com.southwind.entity.Order;
import com.southwind.entity.OrderVO;
import com.southwind.feign.OrderFeign;
import feign.hystrix.FallbackFactory;

public class OrderFeignFallbackFactory implements FallbackFactory<OrderFeign>  {
    @Override
    public OrderFeign create(Throwable throwable) {
        return new OrderFeign() {
            @Override
            public void save(Order order) {

            }

            @Override
            public OrderVO findAllByUid(long uid, int page, int limit) {
                OrderVO orderVO=new OrderVO();
                orderVO.setMsg("服务器异常，请稍后再试");
                return orderVO;
            }

            @Override
            public void deleteByMid(long mid) {

            }

            @Override
            public void deleteByUid(long uid) {

            }

            @Override
            public OrderVO findAllByState(int state, int page, int limit) {
                OrderVO orderVO=new OrderVO();
                orderVO.setMsg("服务器异常，请稍后再试");
                return orderVO;
            }

            @Override
            public void updateState(long id, long state, long aid) {

            }
        };
    }
}
