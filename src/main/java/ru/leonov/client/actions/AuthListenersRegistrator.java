package ru.leonov.client.actions;

import java.util.ArrayList;
import java.util.List;

public class AuthListenersRegistrator {

    private final List<AuthListener> listeners = new ArrayList<>();

    public void addListener(AuthListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(AuthListener listener) {
        listeners.remove(listener);
    }

    public void fireAction(String nick){
        for (AuthListener listener : listeners) {
            listener.AuthListenerPerformAction(nick);
        }
    }

}