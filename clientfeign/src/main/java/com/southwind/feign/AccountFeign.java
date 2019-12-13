package com.southwind.feign;

import com.southwind.entity.Account;
import com.southwind.feignfallback.AccountFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "account",fallbackFactory = AccountFeignFallbackFactory.class)
public interface AccountFeign {

    @GetMapping("/account/login/{username}/{password}/{type}")
    public Account login(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("type") String type);
}
