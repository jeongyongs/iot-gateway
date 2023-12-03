package com.nhnacademy.aiot.util;

import javax.tools.ToolProvider;
import com.nhnacademy.aiot.port.Packet;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class FunctionFactory {

    private static final String CODE_TEMPLATE = //
            "package function;\n" //
                    + "import java.util.List;\n" //
                    + "import org.json.JSONObject;\n" //
                    + "import java.util.function.Function;\n" //
                    + "import com.nhnacademy.aiot.port.Packet;\n" //
                    + "\n" //
                    + "public class Function%d implements Function<Packet, List<Packet>> {\n" //
                    + "    @Override\n" //
                    + "    public List<Packet> apply(Packet packet) {\n" //
                    + "        %s\n" //
                    + "    }\n" //
                    + "}\n";

    private static final AtomicInteger id = new AtomicInteger();

    private FunctionFactory() {}

    @SuppressWarnings("unchecked")
    public static Function<Packet, List<Packet>> getInstance(String code) {
        String className = "Function" + id.incrementAndGet();

        File javaFile = new File("function/" + className + ".java");
        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(String.format(CODE_TEMPLATE, id.get(), code));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            ToolProvider.getSystemJavaCompiler().run(null, null, null, javaFile.getPath());
            byte[] classBytes = Files.readAllBytes(Path.of("function/" + className + ".class"));

            ClassLoader classLoader = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    return defineClass(name, classBytes, 0, classBytes.length);
                }
            };

            return (Function<Packet, List<Packet>>) Class
                    .forName("function." + className, true, classLoader).getConstructor()
                    .newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
