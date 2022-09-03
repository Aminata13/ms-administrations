package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.Set;

public class Role {

    private String id;

    private String libelle;

    @JsonIgnore
    private boolean editable;

    @JsonIgnore
    private String type;

    private Map<String, Set<String>> privilegesActions;

    public Role() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Set<String>> getPrivilegesActions() {
        return this.privilegesActions;
    }

    public void setPrivilegesActions(Map<String, Set<String>> privilegesActions) {
        this.privilegesActions = privilegesActions;
    }

    public boolean hasPrivilegeAction(String _privilege) {
        for (String privilege : this.privilegesActions.keySet()) {
            if (privilege.equals(_privilege)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrivilegeAction(String _privilege, String action) {
        for (String privilege : this.privilegesActions.keySet()) {
            if (privilege.equals(_privilege) && this.privilegesActions.get(_privilege).contains(action)) {
                return true;
            }
        }
        return false;
    }
}
