package projekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Element {
    private final String tag;
    private String content;
    private final Map<String, String> attributes = new HashMap<>();
    private final List<Element> children = new ArrayList<>();
    private final int nodeDepth;

    public Element(String tag, int nodeDepth){
        this.tag = tag;
        this.nodeDepth = nodeDepth;
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

    public void addAttribute(String name, String type){
        attributes.put(name, type);
    }

    public void setAttributes(Map <String, String> attributes){
        this.attributes.putAll(attributes);
    }

    public List<Element> getChildren(){
        return children;
    }

    public void addChild(Element child){
        children.add(child);
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
            for(Element child : children){
                stringBuilder.append(child.toString());
            }
        }

        elementsTree = stringBuilder.toString();

        return elementsTree;
    }
}
