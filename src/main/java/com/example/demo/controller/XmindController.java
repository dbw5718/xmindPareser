package com.example.demo.controller;

import com.example.demo.entity.NodeData;
import com.example.demo.service.XmindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping("/xmind")
public class XmindController {
    @Autowired
    private XmindService xmindService;

    @PostMapping("/upload")
    public  ResponseEntity<List<NodeData>> uploadXmindFile(@RequestParam("file") MultipartFile file) throws Exception{
        File tempFile = File.createTempFile("xmind_", ".xmind");
        file.transferTo(tempFile);
        List<NodeData> nodeDataList = xmindService.processXmindFile(tempFile.getAbsolutePath());
        return ResponseEntity.ok(nodeDataList);
    }
}
