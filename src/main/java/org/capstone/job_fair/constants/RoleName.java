package org.capstone.job_fair.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {

    COMPANY_MANAGER("company_manager"),
    ATTENDANT("attendant"),
    ADMIN("admin"),
    COMPANY_EMPLOYEE("company_employee"),
    STAFF("staff");




    private String displayRole;

    RoleName(String displayRole){
        this.displayRole = displayRole;
    }

    @JsonValue
    public String getDisplayRole() {
        return displayRole;
    }

    public void setDisplayRole(String displayRole) {
        this.displayRole = displayRole;
    }

}
