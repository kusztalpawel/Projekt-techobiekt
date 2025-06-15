package projekt;

import javafx.util.Pair;

import java.util.*;

public class XsdElement {
    private final String name;
    private String type;
    private final Map<Pair<String, String>, String> attributes;
    private int minOccur;
    private String maxOccur;
    private final List<XsdElement> children;
    private final int nodeDepth;
    private final boolean isMixed;
    private final boolean isEmpty;

    public XsdElement(String name, String type, int nodeDepth, boolean isMixed, boolean isEmpty){
        this.name = name;
        this.type = type;
        this.nodeDepth = nodeDepth;
        attributes = new LinkedHashMap<>();
        children = new ArrayList<>();
        minOccur = 1;
        this.isMixed = isMixed;
        this.isEmpty = isEmpty;
        maxOccur = "";
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

    public boolean isMixed() {
        return isMixed;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public String getMaxOccur(){
        return maxOccur;
    }

    public int getMinOccur() {
        return minOccur;
    }

    public Map<Pair<String, String>, String> getAttributes(){
        return attributes;
    }

    public void addAttribute(String name, String type, String optional){
        Pair<String, String> pair = new Pair<>(name, type);
        attributes.put(pair, optional);
    }

    public void setAttributes(Map <Pair<String, String>, String> attributes){
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

    public void setMinOccur(int minOccur){
        this.minOccur = minOccur;
    }

    private void addAttributes(Map<Pair<String, String>, String> attributes){
        this.attributes.putAll(attributes);
    }

    private static XsdElement createXsdElement(XmlElement xmlElement){
        boolean mixed = false;
        boolean empty = false;
        if(!xmlElement.getChildren().isEmpty() && xmlElement.getContent() != null && !xmlElement.getContent().isEmpty() && !xmlElement.getContent().equals(" ")){
            mixed = true;
        }
        if(xmlElement.getContent() == null || xmlElement.getContent().isEmpty() || xmlElement.getContent().equals(" ")){
            empty = true;
        }

        return new XsdElement(xmlElement.getTag(), XmlTypes.detectType(xmlElement.getContent()), xmlElement.getNodeDepth(), mixed, empty);
    }

    public static XsdElement createXsdElement(XmlFile xmlFile){
        return createXsdElement(xmlFile.getRootElement());
    }

    private boolean optionalityCheck(List<XmlElement> xmlElements, XmlElement actualElement, String tag){
        boolean missing = false;
        for (XmlElement xmlElement : xmlElements) {
            if(actualElement.getTag().equals(xmlElement.getTag())){
                boolean hasElement = false;
                for (XmlElement child : xmlElement.getChildren()) {
                    if (child.getTag().equals(tag)) {
                        hasElement = true;
                        break;
                    }
                }
                if (!hasElement) {
                    missing = true;
                    break;
                }
            }
        }
        return missing;
    }

    private boolean manyOccursCheck(XsdElement xsdElement, List<XmlElement> children){
        int count = 0;
        for (XmlElement child : children) {
            if (xsdElement.getName().equals(child.getTag())) {
                count++;
                if (count > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setAttributes(XmlElement xmlElement, List<XmlElement> siblings){
        Map<String, String> elementAttributes = xmlElement.getAttributes();

        for (Map.Entry<String, String> entry : elementAttributes.entrySet()) {
            String attrName = entry.getKey();
            String attrValue = entry.getValue();

            String attrType = XmlTypes.detectType(attrValue);
            Pair<String, String> attrKey = new Pair<>(attrName, attrType);

            boolean isOptional = false;
            for (XmlElement sibling : siblings) {
                if (sibling.getTag().equals(xmlElement.getTag()) && !sibling.getAttributes().containsKey(attrName)) {
                    isOptional = true;
                    break;
                }
            }

            String usage = isOptional ? "optional" : "required";

            this.attributes.computeIfAbsent(attrKey, k -> usage);
        }
    }

    private XsdElement checkIfChildAdded(XmlElement xmlChild){
        for (XsdElement child : children) {
            if (child.getName().equals(xmlChild.getTag())) {
                if (!XmlTypes.detectType(xmlChild.getContent()).equals(child.getType())) {
                    child.setType(XmlTypes.XmlSimpleType.ANY);
                }

                return child;
            }
        }

        return null;
    }


    public void createXsdTree(XmlElement xmlElement){
        List<XmlElement> xmlSiblings;
        xmlSiblings = xmlElement.getSameDepthNodes();

        setAttributes(xmlElement, xmlSiblings);

        for (XmlElement xmlChild : xmlElement.getChildren()) {
            XsdElement existingChild;
            existingChild = checkIfChildAdded(xmlChild);

            if (existingChild == null) {
                XsdElement xsdChild = createXsdElement(xmlChild);
                if (optionalityCheck(xmlSiblings, xmlElement, xmlChild.getTag())) {
                    xsdChild.setMinOccur(0);
                }

                if(manyOccursCheck(xsdChild, xmlChild.getParent().getChildren())){
                    xsdChild.maxOccur = "unbounded";
                }
                children.add(xsdChild);
                xsdChild.createXsdTree(xmlChild);
            } else {
                existingChild.createXsdTree(xmlChild);
            }
        }
    }

    @Override
    public String toString() {
        String elementsTree = "";
        StringBuilder stringBuilder = new StringBuilder(elementsTree);
        stringBuilder.repeat("  ", this.nodeDepth);
        stringBuilder.append("Type: ").append(type)
                .append(" | name: ").append(name)
                .append(" | atrybuty: ").append(attributes)
                .append(" | minOccurs: ").append(minOccur)
                .append(" | maxOccurs: ").append(maxOccur)
                .append(" | isMixed: ").append(isMixed)
                .append(" | isEmpty: ").append(isEmpty)
                .append("\n");
        if(!children.isEmpty()){
            for(XsdElement child : children){
                stringBuilder.append(child.toString());
            }
        }

        elementsTree = stringBuilder.toString();

        return elementsTree;
    }
}