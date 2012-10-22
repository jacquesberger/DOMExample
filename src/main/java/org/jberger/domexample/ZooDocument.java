/* Copyright 2011 Jacques Berger

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.jberger.domexample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ZooDocument {
    
    private Document document;
    
    public ZooDocument(String documentFilePath) 
            throws ParserConfigurationException, SAXException, IOException {
        parseXmlDocument(documentFilePath);
    }

    private void parseXmlDocument(String documentFilePath) 
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentFactory = initializeDocumentFactory();
        DocumentBuilder parser = documentFactory.newDocumentBuilder();
        document = parser.parse(documentFilePath);
    }

    private DocumentBuilderFactory initializeDocumentFactory() {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setIgnoringComments(true);
        documentFactory.setCoalescing(true);
        documentFactory.setNamespaceAware(true);
        return documentFactory;
    }
    
    public List<String> getAnimalNameList() {
        List<String> list = new ArrayList();
        NodeList animals = document.getElementsByTagName("animal");
        for (int i = 0; i < animals.getLength(); i++){
            Element animalName = (Element) animals.item(i);
            list.add(animalName.getTextContent());
        }
        return list;
    }
    
    public void addZone(String zoneName) {
        Element newZone = document.createElement("zone");
        newZone.setAttribute("nom", zoneName);
        document.getDocumentElement().appendChild(newZone);
    }
    
    public void addAnimalToZone(String animalName, String zoneName) {
        Element newAnimal = document.createElement("animal");
        newAnimal.setTextContent(animalName);
        Element zone = getZoneByName(zoneName);
        zone.appendChild(newAnimal);
    }
    
    private Element getZoneByName(String zoneName) {
        NodeList zones = document.getElementsByTagName("zone");
        for (int i = 0; i < zones.getLength(); i++){
            Element zone = (Element) zones.item(i);
            if (zoneName.compareTo(zone.getAttribute("nom")) == 0) {
                return zone;
            }
        }
        return null;
    }
    
    public void saveToFile(String filePath) throws Exception {
        Source domSource = new DOMSource(document);
        File xmlFile = new File(filePath);
        Result serializationResult = new StreamResult(xmlFile);
        Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTransformer.transform(domSource, serializationResult);
    }
}
