package com.southwind.feignfallback;

import com.southwind.entity.User;
import com.southwind.entity.UserVO;
import com.southwind.feign.UserFeign;
import feign.hystrix.FallbackFactory;

public class UserFeignFallbackFactory implements FallbackFactory<UserFeign> {
    @Override
    public UserFeign create(Throwable throwable) {
        return new UserFeign() {
            @Override
            public UserVO findAll(int page, int limit) {
              UserVO userVO=new UserVO();
                userVO.setMsg("服务器异常，请稍后再试");
                return userVO;
            }

            @Override
            public void save(User user) {

            }

            @Override
            public void deleteById(long id) {

            }
        };
    }
}
