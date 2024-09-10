package com.example.demo.service;

import com.example.demo.entity.NodeData;
import com.example.demo.util.XmindParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XmindService {
    @Autowired
    private XmindParser xmindParser;
//    @Autowired
//    private NodeDataRepository nodeDataRepository;


    public List<NodeData> processXmindFile(String filePath) throws  Exception{
        List<NodeData> nodeDataList = xmindParser.parseXmindFile(filePath);
//        return nodeDataRepository.saveAll(nodeDataList);
        return  nodeDataList;
    }
}
