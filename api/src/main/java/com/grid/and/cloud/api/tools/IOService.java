package com.grid.and.cloud.api.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IOService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ResourceLoader resourceLoader;

    @Autowired
    public IOService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Object parseJsonFromResourceFile(String filePathFromResourcesFolder) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderFromResourceFile(filePathFromResourcesFolder);
            return parseJSON(reader);
        } finally {
            closeReader(reader);
        }
    }

    public void createFileIfNotExist(String filePath) {
        try {
            (new File(filePath)).createNewFile();
        } catch (Exception e) {
            String message = String.format("Can not create file=[%s]", filePath);
            throw new RuntimeException(message);
        }
    }

    public Object parseJsonFromFile(String filePath) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderFromFile(filePath);
            return parseJSON(reader);
        } finally {
            closeReader(reader);
        }
    }

    public Object extractDataFromJson(Map<String, Object> jsonObject, String jsonPath) {
        Map<String, Object> json = jsonHardCopy(jsonObject);
        String[] tokens = jsonPath.split("\\.");
        int last = tokens.length - 1;
        for (int i = 0; i < last; i++) {
            if (json == null)
                return null;
            json = (Map<String, Object>) json.get(tokens[i]);
        }

        if (json == null)
            return null;
        return json.get(tokens[last]);
    }

    public Object parseJsonFromString(String jsonContent) {
        try {
            return objectMapper.readValue(jsonContent, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Can not parse JSON: [%s]", jsonContent), e);
        }
    }

    public void writeToFile(String content, String filePath) {
        Writer writer = null;
        try {
            createFileIfNotExist(filePath);
            writer = getBufferedWriterFromFile(filePath);
            writer.write(content);
        } catch (Exception e) {
            String message = String.format("Can not write content=[%s] to file=[%s]", content, filePath);
            throw new RuntimeException(message, e);
        }
        finally {
            closeWriter(writer);
        }
    }

    public String toJSONString(Object json) {
        try {
            return objectMapper.writeValueAsString(json);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Can not cast map to JSON string: [%s]", json), e);
        }
    }


    public Map<String, Object> jsonHardCopy(Map<String, Object> json) {
        try {
            return (Map<String, Object>) parseJsonFromString(toJSONString(json));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Can not cast map to JSON string: [%s]", json), e);
        }
    }


    private BufferedReader getBufferedReaderFromResourceFile(String filePathFromResourcesFolder) {
        try {
            Resource res = resourceLoader.getResource("classpath:" + filePathFromResourcesFolder);
            return new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not open file=[" + filePathFromResourcesFolder + "]", e);
        }
    }

    private BufferedReader getBufferedReaderFromFile(String filePath) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not open file=[" + filePath + "]", e);
        }
    }

    private BufferedWriter getBufferedWriterFromResourceFile(String filePathFromResourcesFolder) {
        try {
            Resource res = resourceLoader.getResource("classpath:" + filePathFromResourcesFolder);
            FileOutputStream fileOutputStream = new FileOutputStream(res.getFile());
            return new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not open file=[" + filePathFromResourcesFolder + "]", e);
        }
    }

    private BufferedWriter getBufferedWriterFromFile(String filePath) {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not open file=[" + filePath + "]", e);
        }
    }

    private void closeReader(Reader reader) {
        try {
            if (reader != null)
                reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Can not close reader", e);
        }
    }

    private void closeWriter(Writer writer) {
        try {
            if (writer != null)
                writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Can not close writer", e);
        }
    }

    private Object parseJSON(Reader reader) {
        try {
            return objectMapper.readValue(reader, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse JSON", e);
        }
    }
}
