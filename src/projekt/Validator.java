package projekt;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Validator {

    public List<String> validate(XmlElement xml, XsdElement xsd) {
        List<String> errors = new ArrayList<>();
        validateElement(xml, xsd, errors, "");
        return errors;
    }

    private void validateElement(XmlElement xml, XsdElement xsd, List<String> errors, String path) {
        String currentPath = path + "|" + xml.getTag();

        if (!xml.getTag().equals(xsd.getName())) {
            errors.add("Tag mismatch at " + currentPath + ": expected " + xsd.getName() + ", found " + xml.getTag());
            return;
        }

        Map<String, String> xmlAttrs = xml.getAttributes();
        for (Map.Entry<Pair<String, String>, String> entry : xsd.getAttributes().entrySet()) {
            String attrName = entry.getKey().getKey();
            String attrRequirement = entry.getValue();
            if ("required".equals(attrRequirement) && !xmlAttrs.containsKey(attrName)) {
                errors.add("Missing required attribute '" + attrName + "' at " + currentPath);
            }
        }

        Set<String> expectedChildNames = xsd.getChildren().stream()
                .map(XsdElement::getName)
                .collect(Collectors.toSet());

        if (xsd.isEmpty() && (xml.getContent() != null && !xml.getContent().trim().isEmpty())) {
            errors.add("Element at " + currentPath + " must be empty, but content found: " + xml.getContent());
        }

        if (!xsd.isEmpty() && !isContentValid(xml.getContent(), xsd.getType())) {
            errors.add("Invalid content at " + currentPath + ": '" + xml.getContent() + "' does not match type " + xsd.getType());
        }

        if (!xml.getChildren().isEmpty() && xsd.getChildren().isEmpty()) {
            errors.add("Element at " + currentPath + " is simple but has child elements.");
        } else {
            for (XmlElement actualChild : xml.getChildren()) {
                if (!expectedChildNames.contains(actualChild.getTag())) {
                    errors.add("Unexpected element '" + actualChild.getTag() + "' at " + currentPath);
                }
            }
        }
        if (!xml.getAttributes().isEmpty() && xsd.getAttributes().isEmpty()) {
            errors.add("Element at " + currentPath + " is simple but has attributes.");
        }

        Map<String, List<XmlElement>> groupedChildren = new HashMap<>();
        for (XmlElement child : xml.getChildren()) {
            groupedChildren.computeIfAbsent(child.getTag(), k -> new ArrayList<>()).add(child);
        }



        for (XsdElement expectedChild : xsd.getChildren()) {
            List<XmlElement> actualChildren = groupedChildren.getOrDefault(expectedChild.getName(), new ArrayList<>());

            int actualCount = actualChildren.size();
            int min = expectedChild.getMinOccur();
            String maxOccurStr = expectedChild.getMaxOccur();
            int max;
            if (maxOccurStr == null || maxOccurStr.isEmpty()) {
                max = 1;
            } else if ("unbounded".equals(maxOccurStr)) {
                max = Integer.MAX_VALUE;
            } else {
                max = Integer.parseInt(maxOccurStr);
            }

            if (actualCount < min) {
                errors.add("Too few occurrences of " + expectedChild.getName() + " at " + currentPath + ": found " + actualCount + ", expected at least " + min);
            } else if (actualCount > max) {
                errors.add("Too many occurrences of " + expectedChild.getName() + " at " + currentPath + ": found " + actualCount + ", max allowed is " + max);
            }

            for (XmlElement actualChild : actualChildren) {
                validateElement(actualChild, expectedChild, errors, currentPath);
            }
        }
    }

    private boolean isContentValid(String content, String expectedXsdType) {
        if (expectedXsdType == null || expectedXsdType.equals("xs:anyType")) {
            return true;
        }

        if (content == null || content.trim().isEmpty()) {
            return true;
        }

        String detectedType = XmlTypes.detectType(content.trim());
        return detectedType.equals(expectedXsdType);
    }
}