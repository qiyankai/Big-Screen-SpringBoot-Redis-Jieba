package com.datav.service;

import com.datav.pojo.QueryStatement;
import com.datav.utils.JSONResult;
import oracle.sql.BLOB;

import java.util.List;
import java.util.Map;

public interface QueryStatementService {

    List<QueryStatement> getList();

    public List<Map<String, Object>> findDataBySql(String name);
    public List<Map<String, Object>> findDataByQuery(QueryStatement name);
    public List<byte[]> selectProposalContent();
}
