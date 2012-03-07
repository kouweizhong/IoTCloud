package cgl.iotcloud.core.stream;

import cgl.iotcloud.core.Control;
import cgl.iotcloud.core.ManagedLifeCycle;
import cgl.iotcloud.core.SCException;
import cgl.iotcloud.core.State;
import cgl.iotcloud.core.message.MessageHandler;
import cgl.iotcloud.core.message.data.StreamDataMessage;
import cgl.iotcloud.streaming.http.listener.HttpListener;
import cgl.iotcloud.streaming.http.listener.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class StreamingListener implements ManagedLifeCycle, Control {
    private Logger log = LoggerFactory.getLogger(StreamingListener.class);

    private HttpListener listener;

    private int port;

    private String path;

    private MessageHandler handler;

    private State state = State.DEFAULT;

    public StreamingListener(int port, String path, MessageHandler handler) {
        this.port = port;
        this.path = path;
        this.handler = handler;
    }


    public void start() {
        if (state != State.INITIALIZED) {
            throw new IllegalStateException("State should be initialized");
        }
        state = State.STARTED;
        listener.start();
    }

    public void stop() {
        if (state != State.STOPPED) {
            throw new IllegalStateException("State should be started");
        }
        state = State.STOPPED;
        listener.stop();
    }

    public String getState() {
        return state.toString();
    }

    public void init() {
        if (state != State.DEFAULT) {
            throw new IllegalStateException("Cannot initialize an already initialized listener");
        }
        listener = new HttpListener(port, new Receiver(), path);
        state = State.INITIALIZED;
    }

    private class Receiver implements MessageReceiver {
        public void messageReceived(InputStream in) {
            StreamDataMessage dataMessage = new StreamDataMessage();
            dataMessage.setInputStream(in);
            handler.onMessage(dataMessage);
        }
    }

    public void destroy() {
    }

    protected void handleException(String s, Exception e) {
        log.error(s, e);
        throw new SCException(s, e);
    }

    protected void handleException(String s) {
        log.error(s);
        throw new SCException(s);
    }
}
