package projekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XsdElement {
    private final String name;
    private final String type;
    private final Map<String, String> attributes;
    private final List<XsdElement> children;
    private final int nodeDepth;

    public XsdElement(String name, String type, int nodeDepth){
        this.name = name;
        this.type = type;
        this.nodeDepth = nodeDepth;
        attributes = new HashMap<>();
        children = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public Map<String, String> getAttributes(){
        return attributes;
    }

    public void addAttribute(String name, String type){
        attributes.put(name, type);
    }

    public void setAttributes(Map <String, String> attributes){
        this.attributes.putAll(attributes);
    }

    public List<XsdElement> getChildren(){
        return children;
    }

    public void addChild(XsdElement child){
        children.add(child);
    }

    public void setChildren(List<XsdElement> children){
        this.children.addAll(children);
    }

    public int getNodeDepth(){
        return nodeDepth;
    }

    private Map<String, String> setXsdAttributes(Map<String, String> attributes){
        Map<String, String> xsdAttributes = new HashMap<>();

        for (Map.Entry<String, String> attribute : attributes.entrySet()){
            xsdAttributes.put(attribute.getKey(), XmlTypes.detectType(attribute.getValue()));
        }

        return xsdAttributes;
    }

    private XsdElement insertXsdElement(XmlElement xmlElement){
        return new XsdElement(xmlElement.getTag(), XmlTypes.detectType(xmlElement.getContent()), xmlElement.getNodeDepth());
    }

    public void createXsdObject(XmlElement xmlElement){
        this.setAttributes(setXsdAttributes(xmlElement.getAttributes()));
        if(!xmlElement.getChildren().isEmpty()){
            for(XmlElement child : xmlElement.getChildren()){
                XsdElement xsdChild = insertXsdElement(child);
                xsdChild.createXsdObject(child);
                this.addChild(xsdChild);
            }
        }
    }

    @Override
    public String toString() {
        String elementsTree = "";
        StringBuilder stringBuilder = new StringBuilder(elementsTree);
        stringBuilder.repeat("  ", this.nodeDepth);
        stringBuilder.append("Type: ").append(type).append(" : name: ").append(name).append(" : atrybuty: ").append(attributes).append("\n");
        if(!children.isEmpty()){
            for(XsdElement child : children){
                stringBuilder.append(child.toString());
            }
        }

        elementsTree = stringBuilder.toString();

        return elementsTree;
    }
}
