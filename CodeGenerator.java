import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeGenerator {
    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.err.println("请输入文件名称");
            return;
        }
        String fileName = args[0].trim(); 
        System.out.println("File name: " + fileName);
        String fileContent = "";
        try {
            fileContent = Files.readString(Paths.get(fileName)); 
        } catch (IOException e) {
            System.err.println("无法读取文件 " + fileName);
            e.printStackTrace();
            return;
        }

        
        List<String> classNames = FileReader.extractClassNames(fileContent);
        if (classNames.isEmpty()) {
            System.err.println("无法从文件中找到类名");
            return;
        }

        
        Map<String, StringBuilder> classJavaCodeMap = Parser.parseMermaid(fileContent, classNames);

        
        for (Map.Entry<String, StringBuilder> entry : classJavaCodeMap.entrySet()) {
            String className = entry.getKey();
            String javaCode = entry.getValue().toString();
            try {
                String outputFileName = className + ".java";
                File file = new File(outputFileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw.write("public class " + className + " {\n");
                    bw.write(javaCode);
                    bw.write("}\n");
                }
                System.out.println("Java class has been generated: " + outputFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class FileReader {
        
        public static List<String> extractClassNames(String fileContent) {
            List<String> classNames = new ArrayList<>();
            Pattern pattern = Pattern.compile("\\bclass\\s+(\\w+)");
            Matcher matcher = pattern.matcher(fileContent);
            while (matcher.find()) {
                classNames.add(matcher.group(1)); 
            }
            return classNames;
        }
    }


    public class Parser {
       
        public static Map<String, StringBuilder> parseMermaid(String fileContent, List<String> classNames) {
            Map<String, StringBuilder> classJavaCodeMap = new HashMap<>();
            for (String className : classNames) {
                classJavaCodeMap.put(className, new StringBuilder());
            }
    
            String[] lines = fileContent.split("\n");
            String currentClass = null;
            for (String line : lines) {
                line = line.replaceAll("^\\s+", "");
                if (!line.isEmpty() && line.contains(":")) { 
                    line = line.trim(); 
                    String[] parts = line.split(":");
                    if (parts.length > 1) { 
                        String[] attributes = parts[1].trim().split("\\s+");
                        if (attributes.length > 1) {
                            if (attributes[0].startsWith("-") || attributes[0].startsWith("+")) {
                                String classIdentifier = parts[0].trim();
                                currentClass = classIdentifier;
                                StringBuilder javaCode = classJavaCodeMap.get(currentClass);
                                if (javaCode != null) {
                                    if (attributes[0].startsWith("+")) {
                                        String methodSignature = attributes[0].substring(1).trim();
                                        String[] methodParts = methodSignature.split("\\(");
                                        if (methodParts.length > 1) {
                                            String methodHeader = methodParts[0].trim();
                                            String methodArgs = methodParts[1].trim();
                                            
                                            if (methodArgs.endsWith(")")) {
                                                methodArgs = methodArgs.substring(0, methodArgs.length() - 1);
                                            }
    
                                           
                                            String returnType = attributes[attributes.length - 1];
    
                                            
                                            if (methodHeader.startsWith("get")) {
                                                
                                                String methodName = methodHeader.substring(3).toLowerCase();
                                                
                                                javaCode.append("    public ").append(returnType).append(" ").append(methodHeader).append("(").append(methodArgs).append(") {\n")
                                                        .append("        return ").append(methodName).append(";\n")
                                                        .append("    }\n");
                                            } else if (methodHeader.startsWith("set")) {
                                               
                                                String methodName = methodHeader.substring(3).toLowerCase();
                                                
                                                javaCode.append("    public void ").append(methodHeader).append("(").append(methodArgs).append(" ").append(methodName).append(") {\n")
                                                        .append("        this.").append(methodName).append(" = ").append(methodName).append(";\n");
                                                
                                                if (methodName.equals("owner")) {
                                                    javaCode.append("        // 如果方法名为 setOwner，则生成对应的赋值语句\n")
                                                            .append("        this.owner = ").append(methodName).append(";\n");
                                                }
                                                javaCode.append("    }\n");
                                            } else {
                                                
                                                javaCode.append("    public ").append(returnType).append(" ").append(methodHeader).append("(").append(methodArgs).append(") {").append(getDefaultReturnValue(returnType)).append(";}\n");
                                            }
                                        } else {
                                            System.out.println("Skipping line due to invalid method format: " + line);
                                        }
                                    } else if (attributes[0].startsWith("-")) {
                                        String attributeType = attributes[0].substring(1);
                                        String attributeName = attributes[1];
                                        javaCode.append("    private ").append(attributeType).append(" ").append(attributeName).append(";\n");
                                    }
                                } else {
                                    System.out.println("Skipping line due to unknown class: " + currentClass);
                                }
                            }
                        } else {
                            System.out.println("Skipping line due to insufficient attributes: " + line);
                        }
                    } else {
                        System.out.println("Skipping line due to lack of ':' delimiter: " + line);
                    }
                }
            }
            return classJavaCodeMap;
        }
    
       
        private static String getDefaultReturnValue(String returnType) {
            switch (returnType) {
                case "int":
                    return "return 0";
                case "String":
                    return "return \"\"";
                case "boolean":
                    return "return false";
                default:
                    return "";
            }
        }
    }
}