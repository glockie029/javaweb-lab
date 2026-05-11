package com.example.lab02.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class DemoHarmlessGadget implements Serializable {

    private static final long serialVersionUID = 1L;

    private static boolean activated;
    private static String lastMessage;
    private static long activationCount;

    private String message;

    public DemoHarmlessGadget() {
    }

    public DemoHarmlessGadget(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        activated = true;
        activationCount++;
        lastMessage = message;
    }

    public static boolean isActivated() {
        return activated;
    }

    public static String getLastMessage() {
        return lastMessage;
    }

    public static long getActivationCount() {
        return activationCount;
    }

    public static void resetState() {
        activated = false;
        lastMessage = null;
        activationCount = 0L;
    }

    @Override
    public String toString() {
        return "DemoHarmlessGadget{message='" + message + "'}";
    }
}
