package com.datav.config;

import com.datav.pojo.QueryStatement;
import com.datav.pojo.Test;
import com.datav.service.QueryStatementService;
import com.datav.service.TestService;
import com.datav.utils.DateUtil;
import com.datav.utils.JsonUtils;
import com.datav.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ScannerJob {


    @Autowired
    private QueryStatementService queryStatementService;
    @Autowired
    private RedisOperator redisOperator;

    public static String QUERY_NAME = "QUERY_NAME";

    /**
     * 设置定时器10min刷新数据
     */
    @Scheduled(cron = "* 0/10 * * * ?")
    public void autoRefreshRedisData(){
        List<QueryStatement> queryStatementList = queryStatementService.getList();
        queryStatementList.stream().forEach(queryStatement -> {
            List<Map<String, Object>> dataList = queryStatementService.findDataByQuery(queryStatement);
            String jsonStr = JsonUtils.objectToJson(dataList);
            redisOperator.set(QUERY_NAME + ":" + queryStatement.getName(), jsonStr);
        });

        System.out.println(DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }

}
