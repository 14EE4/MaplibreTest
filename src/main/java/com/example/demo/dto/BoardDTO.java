package com.example.demo.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

public class BoardDTO {
    private Long id;
    private String name;
    private Integer grid_x;
    private Integer grid_y;
    private BigDecimal center_lng;
    private BigDecimal center_lat;
    private Map<String,Object> meta;
    private Integer posts_count;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getGrid_x() { return grid_x; }
    public void setGrid_x(Integer grid_x) { this.grid_x = grid_x; }
    public Integer getGrid_y() { return grid_y; }
    public void setGrid_y(Integer grid_y) { this.grid_y = grid_y; }
    public BigDecimal getCenter_lng() { return center_lng; }
    public void setCenter_lng(BigDecimal center_lng) { this.center_lng = center_lng; }
    public BigDecimal getCenter_lat() { return center_lat; }
    public void setCenter_lat(BigDecimal center_lat) { this.center_lat = center_lat; }
    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }
    public Integer getPosts_count() { return posts_count; }
    public void setPosts_count(Integer posts_count) { this.posts_count = posts_count; }
    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
    public Timestamp getUpdated_at() { return updated_at; }
    public void setUpdated_at(Timestamp updated_at) { this.updated_at = updated_at; }
}
