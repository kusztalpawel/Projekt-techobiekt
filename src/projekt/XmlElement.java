package projekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlElement {
    private final String tag;
    private String content;
    private final Map<String, String> attributes;
    private final List<XmlElement> children;
    private final int nodeDepth;

    public XmlElement(String tag, int nodeDepth){
        this.tag = tag;
        this.nodeDepth = nodeDepth;
        attributes = new HashMap<>();
        children = new ArrayList<>();
    }

    public String getTag(){
        return tag;
    }

    public String getContent(){
        return content;
    }

    public Map<String, String> getAttributes(){
        return attributes;
    }

    public void addAttribute(String name, String value){
        attributes.put(name, value);
    }

    public void setAttributes(Map <String, String> attributes){
        this.attributes.putAll(attributes);
    }

    public List<XmlElement> getChildren(){
        return children;
    }

    public void addChild(XmlElement child){
        children.add(child);
    }

    public void setChildren(List<XmlElement> children){
        this.children.addAll(children);
    }

    public void setContent(String content){
        this.content = content;
    }

    public int getNodeDepth(){
        return nodeDepth;
    }

    @Override
    public String toString() {
        String elementsTree = "";
        StringBuilder stringBuilder = new StringBuilder(elementsTree);
        stringBuilder.repeat("  ", this.nodeDepth);
        stringBuilder.append(tag).append(" : atrybuty: ").append(attributes).append(" : zawartosc: ").append(content).append("\n");
        if(!children.isEmpty()){
            for(XmlElement child : children){
                stringBuilder.append(child.toString());
            }
        }

        elementsTree = stringBuilder.toString();

        return elementsTree;
    }
}
