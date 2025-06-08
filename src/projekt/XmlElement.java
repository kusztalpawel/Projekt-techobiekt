package projekt;

import java.util.*;

public class XmlElement {
    private final String tag;
    private String content;
    private final Map<String, String> attributes;
    private final List<XmlElement> children;
    private final int nodeDepth;
    private final XmlElement parent;

    public XmlElement(String tag, int nodeDepth, XmlElement parent){
        this.tag = tag;
        this.nodeDepth = nodeDepth;
        attributes = new LinkedHashMap<>();
        children = new ArrayList<>();
        this.parent = parent;
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

    public XmlElement getParent() {
        return parent;
    }

    public List<XmlElement> getSameDepthNodes() {
        XmlElement root = this;
        while (root.parent != null) {
            root = root.parent;
        }

        List<XmlElement> listElements = new ArrayList<>();
        nodesSearch(root, this.nodeDepth, listElements);

        return listElements;
    }

    private void nodesSearch(XmlElement node, int targetDepth, List<XmlElement> result) {
        if (node.nodeDepth == targetDepth) {
            result.add(node);
            return;
        }
        for (XmlElement child : node.getChildren()) {
            nodesSearch(child, targetDepth, result);
        }
    }

    @Override
    public String toString() {
        String elementsTree = "";
        StringBuilder stringBuilder = new StringBuilder(elementsTree);
        stringBuilder.repeat("  ", this.nodeDepth);
        stringBuilder.append(tag).append(": atrybuty: ").append(attributes).append(" | zawartosc: ").append(content).append("\n");
        if(!children.isEmpty()){
            for(XmlElement child : children){
                stringBuilder.append(child.toString());
            }
        }

        elementsTree = stringBuilder.toString();

        return elementsTree;
    }
}
