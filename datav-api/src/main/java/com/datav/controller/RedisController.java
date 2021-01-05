package com.datav.controller;

import com.datav.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用来测试的controller
 * 加上 @ApiIgnore 就不会被加到swagger2文档里了
 */
@ApiIgnore
@RestController
@RequestMapping("/redis")
public class RedisController {


    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {
        redisOperator.set(key, value);
        return "hello 憨批!";
    }

    @GetMapping("/get")
    public Object get(String key) {
        return (String) redisOperator.get(key);
    }

    @GetMapping("/delte")
    public Object delte(String key) {

        return "OK";
    }

}
