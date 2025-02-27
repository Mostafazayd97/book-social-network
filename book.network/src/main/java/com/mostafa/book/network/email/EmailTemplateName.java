package com.mostafa.book.network.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activateAccount");
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
