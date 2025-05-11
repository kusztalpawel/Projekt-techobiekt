package projekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlFile {
    private final String fileContent;
    private Element rootElement;

    public XmlFile(String fileContent){
        this.fileContent = fileContent;
    }

    public void insertAttributes(Map<String, String> attributes, String attributeString){
        Pattern pattern = Pattern.compile("(\\w+)|\"(.*?)\"");
        Matcher matcher = pattern.matcher(attributeString);
        String[] attributesArray = new String[2];

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                attributesArray[0] = (matcher.group(1));
            } else {
                attributesArray[1] = (matcher.group(2));
            }

            if(attributesArray[0] != null && attributesArray[1] != null){
                attributes.put(attributesArray[0],attributesArray[1]);
                System.out.println(attributesArray[0] + " " + attributesArray[1]);
            }
        }
    }

    public void insertElement(List<Element> stack,  boolean isRoot, int iterator){
        boolean isSelfClosing = false;
        int tagNameEnd = fileContent.indexOf('>', iterator);
        Map<String, String> attributes = new HashMap<>();
        String tagString = fileContent.substring(iterator + 1, tagNameEnd);
        String tagName;

        if(tagString.endsWith("/")){
            tagString = tagString.substring(0, tagString.indexOf('/'));
            isSelfClosing = true;
        }

        if(!tagString.contains(" ")){
            tagName = tagString;
        } else {
            int attributeBegin = tagString.indexOf(' ');
            String attributesPart;
            tagName = tagString.substring(0, attributeBegin);
            attributesPart = tagString.substring(attributeBegin + 1);
            insertAttributes(attributes, attributesPart);
        }

        Element element = new Element(tagName, stack.size());

        if(!attributes.isEmpty()){
            element.setAttributes(attributes);
        }

        if(!isRoot){
            stack.getLast().addChild(element);
        } else {
            rootElement = element;
        }
        if(!isSelfClosing){
            stack.add(element);
        }
    }

    public void insertContent(int iterator, List<Element> stack){
        String content;
        int nextTagBeginning = fileContent.indexOf('<', iterator);
        if(nextTagBeginning >= 0){
            content = fileContent.substring(iterator, nextTagBeginning);
            if(!stack.isEmpty()){
                stack.getLast().setContent(content);
            }
        }
    }

    private boolean isProlog(int iterator, int fileLength){
        return ((iterator + 1) < fileLength)
                && (fileContent.charAt(iterator + 1) == '?'
                || fileContent.charAt(iterator + 1) == '!');
    }

    private boolean isClosing(int iterator, int fileLength){
        return ((iterator) < fileLength)
                && (fileContent.charAt(iterator) == '/');
    }

    public int createXml(){
        List<Element> stack = new ArrayList<>();
        int i = 0;
        int fileLength = fileContent.length();
        while(i >= 0 && i < fileLength){
            if(fileContent.charAt(i) == '<'){
                if(isProlog(i, fileLength)){
                    i = fileContent.indexOf('>', i);
                } else if(isClosing(i + 1, fileLength)){
                    i = fileContent.indexOf('>', i);
                    if(!stack.isEmpty()){
                        stack.removeLast();
                    }
                } else {
                    insertElement(stack, rootElement == null, i);

                    i = fileContent.indexOf('>', i);
                }
            } else {
                insertContent(i, stack);
                i = fileContent.indexOf('<', i) - 1;
            }
            i++;
        }
        System.out.println(rootElement.toString());
        return 0;
    }

    public Element getRootElement(){
        return rootElement;
    }
}
