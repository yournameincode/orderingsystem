package com.southwind.feignfallback;

import com.southwind.entity.Account;
import com.southwind.feign.AccountFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountFeignFallbackFactory implements FallbackFactory<AccountFeign> {
    @Override
    public AccountFeign create(Throwable throwable) {
        return new AccountFeign() {
            @Override
            public Account login(String username, String password, String type) {
               Account account=new Account();
                account.setUsername("服务器异常，请稍后再试");
                return account;
            }
        };
    }
}
