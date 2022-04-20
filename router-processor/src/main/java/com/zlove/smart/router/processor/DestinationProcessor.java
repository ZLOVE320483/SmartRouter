package com.zlove.smart.router.processor;

import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zlove.smart.router.annotations.Destination;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class DestinationProcessor extends AbstractProcessor {

    private static final String TAG = "DestinationProcessor";

    @Override public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Destination.class.getCanonicalName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            return false;
        }
        System.out.println(TAG + ">>>>> process start...");
        Set<Element> allDestinationElements = (Set<Element>) roundEnvironment.getElementsAnnotatedWith(Destination.class);
        System.out.println(TAG + ">>>> all Destination elements count = " + allDestinationElements.size());
        if (allDestinationElements.size() < 1) {
            return false;
        }
        String className = "RouterMapping_" + System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();

        builder.append("package com.zlove.smart.router.mapping;\n");
        builder.append("import java.util.HashMap;\n");
        builder.append("import java.util.Map;\n\n");
        builder.append("public class ").append(className).append(" {\n");
        builder.append("\tpublic static Map<String, String> get() {\n");
        builder.append("\t\tMap<String, String> mapping = new HashMap<>();\n");

        final JsonArray destinationJsonArray = new JsonArray();
        for (Element element : allDestinationElements) {
             final TypeElement typeElement = (TypeElement) element;
             final Destination destination = typeElement.getAnnotation(Destination.class);
             if (destination == null)
                 continue;
             final String url = destination.url();
             final String description = destination.description();
             final String realPath = typeElement.getQualifiedName().toString();

             System.out.println(TAG + " >>> url = " + url);
             System.out.println(TAG + " >>> description = " + description);
             System.out.println(TAG + " >>> realPath = " + realPath);

             builder.append("\t\tmapping.put(")
                     .append("\"")
                     .append(url)
                     .append("\"")
                     .append(", ")
                     .append("\"")
                     .append(realPath)
                     .append("\"")
                    .append(");\n");
             // 组装json对象
            JsonObject item = new JsonObject();
            item.addProperty("url", url);
            item.addProperty("description", description);
            item.addProperty("realPath", realPath);

            destinationJsonArray.add(item);
        }

        builder.append("\t\treturn mapping;\n");
        builder.append("\t}\n");
        builder.append("}");

        String mappingFullClassName = "com.zlove.smart.router.mapping." + className;

        System.out.println(TAG + " >>> mappingFullClassName = " + mappingFullClassName);
        System.out.println(TAG + " >>> class content = \n" + builder);

        // 写入自动生成的类到本地文件中
        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(mappingFullClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while create file", e);
        }

        // ------------------ 生成 mapping_xxx.json ------------------

        // 获取 kapt 的参数 root_project_dir
        String rootDir = processingEnv.getOptions().get("root_project_dir");

        // 写入json到本地文件中
        File rootDirFile = new File(rootDir);
        if (!rootDirFile.exists()) {
            throw new RuntimeException("root_project_dir not exist!");
        }

        File routerFileDir = new File(rootDirFile, "router_mapping");
        if (!routerFileDir.exists()) {
            routerFileDir.mkdir();
        }

        File mappingFile = new File(routerFileDir, "mapping_" + System.currentTimeMillis() + ".json");

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(mappingFile));
            String jsonStr = destinationJsonArray.toString();
            out.write(jsonStr);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing json", e);
        }

        System.out.println(TAG + " >>> process finish ...");

        return false;
    }

}