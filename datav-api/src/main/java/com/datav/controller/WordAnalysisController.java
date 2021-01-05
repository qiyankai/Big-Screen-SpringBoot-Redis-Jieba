package com.datav.controller;

import com.datav.service.QueryStatementService;
import com.datav.utils.JSONResult;
import com.datav.utils.JsonUtils;
import com.datav.utils.RedisOperator;
import com.qianxinyao.analysis.jieba.keyword.Keyword;
import com.qianxinyao.analysis.jieba.keyword.TFIDFAnalyzer;
import oracle.sql.BLOB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WordAnalysisController {

    @Autowired
    private QueryStatementService queryStatementService;
    @Autowired
    private RedisOperator redisOperator;

    public static final String QUERY_NAME = "QUERY_NAME";
    public static final String name = "SUBJECT_SUM";

    // 分析词云数据
    @GetMapping("/analysisProposalContent")
    public Object analysisProposalContent() {

        String jsonStr = new String();

        List<byte[]> contentList = queryStatementService.selectProposalContent();

        List<Map<String, Object>> analysisResult = analysis(contentList);

        jsonStr = JsonUtils.objectToJson(analysisResult);

        redisOperator.set(QUERY_NAME + ":" + name, jsonStr);

        return JSONResult.ok(jsonStr);
    }

    public List<Map<String, Object>> analysis(List<byte[]> contentList) {
        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        StringBuilder content = new StringBuilder();
        contentList.stream().forEach(blob -> {
            try {
            content.append(new String(blob,"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        int topN = 300;
        TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();
        String contentStr = content.toString();
        contentStr = contentStr.replaceAll("\r|\n|。|《|》| |%", "");

        List<Keyword> list = tfidfAnalyzer.analyze(contentStr, topN);
        for (Keyword word : list) {
            System.out.print(word.getName() + ":" + word.getTfidfvalue() + ",");

            HashMap<String, Object> tempMap = new HashMap<>();
            tempMap.put(word.getName(),word.getTfidfvalue());
            result.add(tempMap);
        }
        return result;
    }
}
