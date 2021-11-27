package com.company.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 76822311119983123L;
    private ID id;

    /**
     *
     * @return the entity's id
     */
    public ID getId() {
        return id;
    }

    /**
     *
     * @param id the id to be set
     */
    public void setId(ID id) {
        this.id = id;
    }


}
