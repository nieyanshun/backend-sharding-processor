package com.letv.backend.model;

public class ShardingItem {
    private Long id;
    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ShardingItem [id=" + id + ", state=" + state + "]";
    }
}
