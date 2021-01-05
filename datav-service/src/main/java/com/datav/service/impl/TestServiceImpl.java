package com.datav.service.impl;

//import com.datav.mapper.TestMapper;
import com.datav.pojo.Test;
import com.datav.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

//    @Autowired
//    private TestMapper testMapper;

    @Override
    public List<Test> getList() {
//        return testMapper.selectAll();
        return null;
    }
}
