package cgl.iotcloud.core.message;

/**
 * A message generated by a sensor and consumed by the clients.
 */
public interface SensorMessage {
    /**
     * Get the unique id of the message
     * @return an id of the message
     */
    public String getId();

    /**
     * Set an unique id to the message
     * @param id of the message
     */
    public void setId(String id);
}
