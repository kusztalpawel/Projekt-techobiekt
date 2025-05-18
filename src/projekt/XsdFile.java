package projekt;

import java.io.File;

public class XsdFile {
    private File file;
    private String fileContent;
    private XsdElement rootElement;

    public File getFile(){
        return file;
    }

    public String getFileContent(){
        return fileContent;
    }

    public XsdElement getRootElement(){
        return rootElement;
    }

    public void setFileContent(String fileContent){
        this.fileContent = fileContent;
    }

    public void setRootElement(XsdElement xsdElement){
        this.rootElement = xsdElement;
    }
}
