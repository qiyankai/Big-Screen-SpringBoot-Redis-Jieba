package com.datav.controller;

import com.datav.service.QueryStatementService;
import com.datav.utils.JSONResult;
import com.datav.utils.JsonUtils;
import com.datav.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class QueryStatementController {

    @Autowired
    private QueryStatementService queryStatementService;
    @Autowired
    private RedisOperator redisOperator;

    public static String QUERY_NAME = "QUERY_NAME";

    @GetMapping("/getDataByName")
    public Object getDataByName(String name) {

        String jsonStr = redisOperator.get(QUERY_NAME + ":" + name);

        if (StringUtils.isBlank(jsonStr)) {
            List<Map<String, Object>> dataList = queryStatementService.findDataBySql(name);
            jsonStr = JsonUtils.objectToJson(dataList);
            redisOperator.set(QUERY_NAME + ":" + name, jsonStr);
        }

        return JSONResult.ok(jsonStr);
    }

}
