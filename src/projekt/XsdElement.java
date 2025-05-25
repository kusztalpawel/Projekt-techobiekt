package projekt;

import java.util.*;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class XsdElement {
    private final String name;
    private String type;
    private Map<String, Pair<String, String>> attributes;
    private int minOccur, maxOccur;
    private final Map<XsdElement, String> children;
    private final int nodeDepth;

    public XsdElement(String name, String type, int nodeDepth){
        this.name = name;
        this.type = type;
        this.nodeDepth = nodeDepth;
        attributes = new HashMap<>();
        children = new HashMap<>();
    }

    public String getName(){
        return name;
    }

    public void setType(XmlTypes.XmlSimpleType type){
        this.type = type.label;
    }

    public String getType(){
        return type;
    }

    public Map<String, Pair<String, String>> getAttributes(){
        return attributes;
    }

    public void addAttribute(String name, String type, String optional){
        Pair<String, String> pair = new Pair<>(type, optional);
        attributes.put(name, pair);
    }

    public void setAttributes(Map <String, Pair<String, String>> attributes){
        this.attributes.putAll(attributes);
    }

    public void changeChildOptional(XsdElement key, String optional) {
        children.replace(key, optional);
    }

    public Map<XsdElement, String> getChildren(){
        return children;
    }

    public void addChild(XsdElement child, String optional){
        children.put(child, optional);
    }

    public void setChildren(Map<XsdElement, String> children){
        this.children.putAll(children);
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

    private XsdElement createXsdElement(XmlElement xmlElement){
        return new XsdElement(xmlElement.getTag(), XmlTypes.detectType(xmlElement.getContent()), xmlElement.getNodeDepth());
    }

    public static void updateExistingParentsChildrenIfMissingInNewParent(Map<XsdElement, String> parents, XmlElement newParent) {
        if (newParent.getChildren() == null) return;

        Map<String, XmlElement> newChildrenByName = new HashMap<>();
        for(XmlElement child : newParent.getChildren()){
            newChildrenByName.put(child.getTag(), child);
        }

        for (XsdElement existingParent : parents.keySet()) {
            if (existingParent.getName().equals(newParent.getTag())) {
                if (existingParent.getChildren() == null) {
                    existingParent.setChildren(new HashMap<>());
                }

                Set<String> existingChildNames = existingParent.getChildren().keySet().stream()
                        .map(XsdElement::getName)
                        .collect(Collectors.toSet());

                for (XsdElement child : existingParent.getChildren().keySet()) {
                    if (!newChildrenByName.containsKey(child.getName())) {
                        existingParent.changeChildOptional(child, "optional");
                    }
                }

                for (Map.Entry<String, XmlElement> entry : newChildrenByName.entrySet()) {
                    String childName = entry.getKey();
                    XsdElement newChild = existingParent.createXsdElement(entry.getValue());

                    if (!existingChildNames.contains(childName)) {
                        existingParent.getChildren().put(newChild, "optional");
                    }
                }
            }
        }
    }

    public void insertXsdElement(XmlElement xmlElement){
        /*List<XsdElement> xsdChildren = new ArrayList<>();
        for(XmlElement child : xmlElement.getChildren()){
            if(xsdChildren.isEmpty()){
                xsdChildren.add(createXsdElement(child));
            } else {
                for (XsdElement xsdChild : xsdChildren) {
                    if (child.getTag().equals(xsdChild.getName())) {
                        if(!XmlTypes.detectType(child.getContent()).equals(xsdChild.type)){
                            xsdChild.setType(XmlTypes.XmlSimpleType.ANY);
                        }
                    }
                }
            }
        }*/

        boolean exists = false;

        //this.setAttributes(setXsdAttributes(xmlElement.getAttributes()));
        for(XmlElement xmlChild : xmlElement.getChildren()){
            if(children.isEmpty()) {
                XsdElement xsdChild = createXsdElement(xmlChild);
                xsdChild.insertXsdElement(xmlChild);
                this.addChild(xsdChild, "required");
            } else {
                for (XsdElement element : children.keySet()) {
                    if (xmlChild.getTag().equals(element.getName())){
                        if(!XmlTypes.detectType(xmlChild.getContent()).equals(element.getType())) {
                            element.setType(XmlTypes.XmlSimpleType.ANY);
                        }
                        updateExistingParentsChildrenIfMissingInNewParent(children, xmlChild);
                        exists = true;
                    }
                }

                if(!exists){
                    XsdElement xsdChild = createXsdElement(xmlChild);
                    xsdChild.insertXsdElement(xmlChild);
                    this.addChild(xsdChild, "required");
                }
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
            for(XsdElement child : children.keySet()){
                stringBuilder.append(child.toString());
            }
        }

        elementsTree = stringBuilder.toString();

        return elementsTree;
    }
}
