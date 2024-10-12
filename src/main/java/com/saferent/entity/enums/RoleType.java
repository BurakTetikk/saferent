package com.saferent.entity.enums;

public enum RoleType {

    ROLE_CUSTOMER("Customer"),
    ROLE_ADMIN("Administrator");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    /*    private RoleType(String name) {
        this.name = name;
    }*/

    public String getName() {
        return name;
    }




}
