package org.step.linked.step.model.projection;

public interface UsernameIdProjection {

    String getId();
    String getUsername();

    default String getFullDescription() {
        return String.format("ID: %s, Username: %s%n", this.getId(), this.getUsername());
    }
}
