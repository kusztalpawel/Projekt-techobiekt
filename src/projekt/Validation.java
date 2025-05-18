package projekt;

import java.util.Map;

public class Validation {
    private Validation(){
    }

    public static boolean validateXml(XmlElement xmlElement, XsdElement xsdElement){
        if(!xmlElement.getTag().equals(xsdElement.getName())){
            return false;
        }

        if(!XmlTypes.detectType(xmlElement.getContent()).equals(xsdElement.getType())){
            return false;
        }

        if(xmlElement.getAttributes().size() != xsdElement.getAttributes().size()){
            return false;
        }

        for (String required : xsdElement.getAttributes().keySet()) {
            if (!xmlElement.getAttributes().containsKey(required)) {
                return false;
            }
        }

        for (Map.Entry<String, String> entry : xmlElement.getAttributes().entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            String expectedType = xsdElement.getAttributes().get(name);

            if (!XmlTypes.detectType(value).equals(expectedType)) {
                return false;
            }
        }

        if(xmlElement.getChildren().size() != xsdElement.getChildren().size()){
            return false;
        }

        for(XsdElement child : xsdElement.getChildren()){
            int childIndex = xsdElement.getChildren().indexOf(child);

            validateXml(xmlElement.getChildren().get(childIndex), child);
        }

        return true;
    }
}
