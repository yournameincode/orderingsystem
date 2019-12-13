package com.southwind.feignfallback;

import com.southwind.entity.Menu;
import com.southwind.entity.MenuVO;
import com.southwind.entity.Type;
import com.southwind.feign.MenuFeign;
import feign.hystrix.FallbackFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuFeignFallbackFactory implements FallbackFactory<MenuFeign> {
    @Override
    public MenuFeign create(Throwable throwable) {
        return new MenuFeign() {
            @Override
            public MenuVO findAll(int page, int limit) {
               MenuVO menuVO=new MenuVO();
               menuVO.setMsg("服务器异常，请稍后再试");
                return menuVO;
            }

            @Override
            public List<Type> findAll() {
                List<Type> list=new ArrayList<>();
                Type type=new Type();
                type.setName("服务器异常，导致查询菜品类别出错，请稍后再试");
                list.add(type);
                return list;
            }

            @Override
            public void save(Menu menu) {

            }

            @Override
            public Menu findById(long id) {
                Menu menu=new Menu();
                menu.setName("服务器异常，导致寻找菜品有误，请稍后再试");
                return menu;
            }

            @Override
            public void update(Menu menu) {

            }

            @Override
            public void deleteById(long id) {

            }
        };
    }
}
