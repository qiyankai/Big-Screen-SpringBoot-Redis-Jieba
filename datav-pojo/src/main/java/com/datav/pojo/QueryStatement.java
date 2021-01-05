package com.datav.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "query_statement")
public class QueryStatement {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * name
     */
    @Column(name = "name")
    private String name;

    /**
     * 存放sql语句
     */
    @Column(name = "statement")
    private String statement;

    /**
     * 功能描述
     */
    @Column(name = "description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}