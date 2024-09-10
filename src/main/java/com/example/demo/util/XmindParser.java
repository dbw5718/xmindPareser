package com.example.demo.util;


import com.example.demo.entity.Label;
import com.example.demo.entity.NodeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

@Component
@Slf4j
public class XmindParser {
    public List<NodeData> parseXmindFile(String filePath) throws Exception {
        List<NodeData> result = new ArrayList<>();
        ZipFile zipFile = new ZipFile(new File(filePath));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse(zipFile.getInputStream(zipFile.getEntry("content.xml")));
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("topic");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            //todo
            NodeData nodeData = parseNode(element);
            if (i == 0) {
                nodeData.setRoot(true);
                result.add(nodeData);
            }
        }
        return result;
    }

    public NodeData parseNode(Element element) {
        NodeData nodeData = new NodeData();
        nodeData.setName(element.getElementsByTagName("title").item(0).getTextContent());
        nodeData.setRoot(element.getAttribute("structure-class").equals("root"));
        nodeData.setProgress(0);
        nodeData.setLabel(new Label());

        Label label = new Label();


        NodeList nodesList = element.getElementsByTagName("plain");
        if (nodesList.getLength() > 0) {
            label.getFlag().add(nodesList.item(0).getTextContent());
        }

        NodeList markerList = element.getElementsByTagName("marker-ref");
        for (int i = 0; i < markerList.getLength(); i++) {
            Element markerelement = (Element) markerList.item(i);
            label.getFlag().add(markerelement.getAttribute("marker-id"));
        }

        NodeList taskProgressList = element.getElementsByTagName("task-progress");
        for (int i = 0; i < taskProgressList.getLength(); i++) {
            Element taskProgressElement = (Element) taskProgressList.item(i);
            label.getTask().add(taskProgressElement.getAttribute("value"));  // 任务进度值作为 task
        }

        NodeList imageList = element.getElementsByTagName("label");
        for (int i = 0; i < imageList.getLength(); i++) {
            label.getBack().add(imageList.item(i).getTextContent());
        }


        nodeData.setLabel(label);

        NodeList childNodes = element.getElementsByTagName("topic");
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element childElement = (Element) childNodes.item(i);
            nodeData.getChildren().add(parseNode(childElement));
        }

        return nodeData;
    }
}
