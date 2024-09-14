package com.example.demo.service;

import com.example.demo.entity.Topic;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class XmindService {

//    @Autowired
//    private NodeDataRepository nodeDataRepository;


    // 解压 XMind 文件并返回 content.xml 的 InputStream
    public InputStream extractContentXmlFromXmind(String xmindFilePath) throws IOException {
        ZipFile zipFile = new ZipFile(xmindFilePath);
        ZipEntry entry = zipFile.getEntry("content.xml");

        if (entry != null) {
            return zipFile.getInputStream(entry);
        }
        return null;
    }

    // 解析 content.xml 并生成 Topic 列表
    public void parseContentXml(InputStream contentXmlStream, List<Topic> topicList) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(contentXmlStream);

        // 获取根 <sheet> 元素
        Element sheetElement = document.getRootElement().element("sheet");
        Element rootTopicElement = sheetElement.element("topic");

        // 创建 Topic 树结构并添加到列表
        createTopicFromElement(rootTopicElement, true, null, topicList);
    }
    // 递归处理每个 <topic> 节点
    // 递归处理每个 <topic> 节点，创建 Topic 对象并添加到列表
    public Topic createTopicFromElement(Element topicElement, boolean isRoot, Topic parent, List<Topic> topicList) {
        Topic topic = new Topic();
        topic.setName(topicElement.elementText("title"));
        topic.setDataStream(false);
        topic.setComponentModule(false);
        topic.setModuleId(0);
        topic.setRoot(isRoot);
        topic.setImage("");
        topic.setProgress(0);
        topic.setParent(parent);

        // 解析 labels
        List<String> frontLabels = new ArrayList<>();
        List<String> flagLabels = new ArrayList<>();
        List<String> taskLabels = new ArrayList<>();
        List<String> backLabels = new ArrayList<>();

        Element markerRefs = topicElement.element("marker-refs");
        if (markerRefs != null) {
            List<Element> markerRefList = markerRefs.elements("marker-ref");
            for (Element markerRef : markerRefList) {
                String markerId = markerRef.attributeValue("marker-id");
                if (markerId.contains("flag")) {
                    flagLabels.add(markerId);
                } else if (markerId.contains("priority")) {
                    frontLabels.add(markerId);
                } else if (markerId.contains("task")) {
                    taskLabels.add(markerId);
                }
            }
        }

        Element labels = topicElement.element("labels");
        if (labels != null) {
            List<Element> labelList = labels.elements("label");
            for (Element label : labelList) {
                backLabels.add(label.getText());
            }
        }

        topic.setFrontLabels(frontLabels);
        topic.setFlagLabels(flagLabels);
        topic.setTaskLabels(taskLabels);
        topic.setBackLabels(backLabels);

        // 将当前 Topic 添加到列表
        topicList.add(topic);

        // 递归处理子节点
        Element childrenElement = topicElement.element("children");
        if (childrenElement != null) {
            Element topicsElement = childrenElement.element("topics");
            if (topicsElement != null) {
                List<Element> topicListElements = topicsElement.elements("topic");
                for (Element childTopicElement : topicListElements) {
                    createTopicFromElement(childTopicElement, false, topic, topicList);
                }
            }
        }

        return topic;
    }
}
