package com.example.demo.controller;

import com.example.demo.entity.Topic;
import com.example.demo.service.XmindService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/xmind")
public class XmindController {
    @Resource
    private XmindService xmindService;

    @PostMapping("/upload")
    public  ResponseEntity<ObjectNode> uploadXmindFile(@RequestParam("file") MultipartFile file) throws Exception{

        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xmind")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            // 将上传的文件保存到临时文件
            File tempFile = File.createTempFile("uploaded", ".xmind");
            file.transferTo(tempFile);

            // 解压并解析 content.xml 文件
            InputStream contentXmlStream = xmindService.extractContentXmlFromXmind(tempFile.getPath());
            if (contentXmlStream == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 解析 content.xml 并生成 Topic 列表
            List<Topic> topicList = new ArrayList<>();
            xmindService.parseContentXml(contentXmlStream, topicList);

            // 存储到数据库中
           // topicRepository.saveAll(topicList);

            // 删除临时文件
            tempFile.delete();

            // 返回解析的结果
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultJson = mapper.createObjectNode();
            resultJson.put("message", "XMind file parsed and stored successfully.");
            return new ResponseEntity<>(resultJson, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
