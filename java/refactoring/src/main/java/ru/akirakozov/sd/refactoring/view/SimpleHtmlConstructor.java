package ru.akirakozov.sd.refactoring.view;

import java.util.List;

public class SimpleHtmlConstructor {
    private StringBuilder state = new StringBuilder();

    public SimpleHtmlConstructor() {
        state.append("<html><body>");
    }

    public SimpleHtmlConstructor text(String text) {
        state.append(text);
        return this;
    }

    public SimpleHtmlConstructor newLine() {
        state.append("</br>");
        return this;
    }

    public SimpleHtmlConstructor theme(String text) {
        state.append("<h3>").append(text).append("</h3>");
        return this;
    }

    public SimpleHtmlConstructor list(List<String> toprint) {
        for (String s : toprint) {
            state.append(s).append("</br>");
        }
        return this;
    }

    @Override
    public String toString() {
        return state.append("</body></html>").toString();
    }
}
