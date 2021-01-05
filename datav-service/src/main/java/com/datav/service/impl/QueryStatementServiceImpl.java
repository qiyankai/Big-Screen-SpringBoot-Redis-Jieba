package com.datav.service.impl;

import com.datav.mapper.QueryStatementMapper;
import com.datav.pojo.QueryStatement;
import com.datav.service.QueryStatementService;
import com.datav.utils.JSONResult;
import oracle.sql.BLOB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryStatementServiceImpl implements QueryStatementService {

    @Autowired
    private QueryStatementMapper queryStatementMapper;

    @Override
    public List<QueryStatement> getList() {
        return queryStatementMapper.selectAll();
    }

    @Override
    public List<Map<String, Object>> findDataByQuery(QueryStatement statement) {
        List<Map<String, Object>> dataList = queryStatementMapper.findDataBySql(statement.getStatement());
        return dataList;
    }

    @Override
    public List<byte[]> selectProposalContent() {
        return queryStatementMapper.selectProposalContent();
    }

    @Override
    public List<Map<String, Object>> findDataBySql(String name) {
        QueryStatement queryStatement = new QueryStatement();
        queryStatement.setName(name);
        QueryStatement statement = queryStatementMapper.selectOne(queryStatement);

        if (statement == null) {
            return null;
        }
        List<Map<String, Object>> dataList = queryStatementMapper.findDataBySql(statement.getStatement());
        return dataList;
    }
}
