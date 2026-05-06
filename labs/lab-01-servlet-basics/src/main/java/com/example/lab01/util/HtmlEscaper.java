package com.example.lab01.util;

public final class HtmlEscaper {

    private HtmlEscaper() {
    }

    public static String escape(String input) {
        if (input == null) {
            return "";
        }
        String escaped = input;
        escaped = escaped.replace("&", "&amp;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");
        escaped = escaped.replace("\"", "&quot;");
        escaped = escaped.replace("'", "&#39;");
        return escaped;
    }
}
