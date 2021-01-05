package com.datav.mapper;

import com.datav.my.mapper.MyMapper;
import com.datav.pojo.QueryStatement;
import com.datav.pojo.Test;
import oracle.sql.BLOB;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface QueryStatementMapper extends MyMapper<QueryStatement> {
    @Select("${sql}")
    List<Map<String,Object>> findDataBySql(@Param("sql") String sql);

    @Select("select content from BCC_PROPOSAL where sessions=(select value from BCC_CONFIG where key = 'dpSessions') and content is not null")
    List<byte[]> selectProposalContent();
}