package com.example.lab02.service;

import com.example.lab02.model.DemoHarmlessGadget;
import com.example.lab02.model.DemoSerializableNote;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DeserializationDemoService {

    public Map<String, Object> buildSamplePayload() throws IOException {
        DemoSerializableNote note = new DemoSerializableNote(
                "stage8-demo",
                "java native deserialization sample payload");

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("objectType", note.getClass().getName());
        data.put("objectPreview", note.toString());
        data.put("payload", serializeToBase64(note));
        return data;
    }

    public Map<String, Object> buildHarmlessGadgetPayload() throws IOException {
        DemoHarmlessGadget gadget = new DemoHarmlessGadget(
                "deserialization side effect triggered");

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("objectType", gadget.getClass().getName());
        data.put("objectPreview", gadget.toString());
        data.put("payload", serializeToBase64(gadget));
        return data;
    }

    public Map<String, Object> deserializeVulnerable(String payload) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(Base64.getDecoder().decode(payload)));
        Object object = objectInputStream.readObject();
        return toResultMap("vulnerable-java-deserialization", object);
    }

    public Map<String, Object> deserializeWithAllowList(String payload) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(Base64.getDecoder().decode(payload)));
        objectInputStream.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                "com.example.lab02.model.DemoSerializableNote;java.lang.String;!*"));
        Object object = objectInputStream.readObject();
        return toResultMap("safe-java-deserialization", object);
    }

    public Map<String, Object> getHarmlessGadgetState() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("activated", DemoHarmlessGadget.isActivated());
        data.put("activationCount", DemoHarmlessGadget.getActivationCount());
        data.put("lastMessage", DemoHarmlessGadget.getLastMessage());
        return data;
    }

    public Map<String, Object> resetHarmlessGadgetState() {
        DemoHarmlessGadget.resetState();
        return getHarmlessGadgetState();
    }

    private String serializeToBase64(Object object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private Map<String, Object> toResultMap(String mode, Object object) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("mode", mode);
        data.put("deserializedClass", object == null ? "null" : object.getClass().getName());
        data.put("objectPreview", String.valueOf(object));
        return data;
    }
}
