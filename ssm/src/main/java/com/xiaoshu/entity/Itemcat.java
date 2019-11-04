package com.xiaoshu.entity;

public class Itemcat {
    private Long itemcatid;

    private Long parentId;

    private String name;

    private Long level;

    public Long getItemcatid() {
        return itemcatid;
    }

    public void setItemcatid(Long itemcatid) {
        this.itemcatid = itemcatid;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
}