package projekt;

public class XmlTypes {
    public enum XmlSimpleType {
        STRING("xs:string"),
        BOOLEAN("xs:boolean"),
        DECIMAL("xs:decimal"),
        DURATION("xs:duration"),
        DATETIME("xs:dateTime"),
        TIME("xs:time"),
        DATE("xs:date");

        public final String label;

        XmlSimpleType(String label) {
            this.label = label;
        }
    }

    public static String detectType(String value) {
        if (value == null) {
            return XmlSimpleType.STRING.label;
        }
        if (value.equals("true") || value.equals("false")) {
            return XmlSimpleType.BOOLEAN.label;
        }
        if(value.matches("^-?\\d+(\\.\\d+)?$")){
            return XmlSimpleType.DECIMAL.label;
        }
        if(value.matches("^P(?:(\\d+)Y)?(?:(\\d+)M)?(?:(\\d+)D)?T?(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?$")){
            return XmlSimpleType.DURATION.label;
        }
        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return XmlSimpleType.DATE.label;
        }
        if (value.matches("\\d{2}:\\d{2}:\\d{2}")) {
            return XmlSimpleType.TIME.label;
        }
        if (value.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
            return XmlSimpleType.DATETIME.label;
        }

        return XmlSimpleType.STRING.label;
    }
}
