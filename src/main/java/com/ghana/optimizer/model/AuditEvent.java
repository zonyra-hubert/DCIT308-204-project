package com.ghana.optimizer.model;

/**
 * Domain entity recording operational dispatch, state mutations, and undo actions
 * for the campus operations audit trail.
 */
public class AuditEvent {

    private String id;
    private String actionType; // CREATE, UPDATE, DELETE, DISPATCH, ROUTE_CHANGE, UNDO
    private String entityName; // ServiceRequest, Road, Resource, Location
    private String entityId;
    private String details;
    private String timestamp;

    public AuditEvent() {}

    public AuditEvent(String id, String actionType, String entityName, String entityId,
                      String details, String timestamp) {
        this.id = id;
        this.actionType = actionType;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "AuditEvent{[" + timestamp + "] " + actionType + " " + entityName + " (" + entityId + "): " + details + "}";
    }
}
