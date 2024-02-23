package com.nn.chatbot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MapInitializer {

//    @Value("${Divisions.filepath}")
    public String filePath = "Divisions.xml";
    public Map<Integer, String> initializeMapFromPropertiesFile() {
        Map<Integer, String> map = new HashMap<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            Element root = document.getDocumentElement();
            NodeList entryList = root.getElementsByTagName("entry");

            for (int i = 0; i < entryList.getLength(); i++) {
                Element entry = (Element) entryList.item(i);
                int key = Integer.parseInt(entry.getAttribute("key"));
                String value = entry.getTextContent();
                map.put(key, value);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return map;
    }
}
