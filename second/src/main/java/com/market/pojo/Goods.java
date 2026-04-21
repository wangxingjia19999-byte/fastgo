package com.market.pojo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Goods {
    private Integer id;
    private String name;
    private Double price;
    private String description;
    private String phoneNum;
    private String cid;
    private String owner;
    private String condition;
    private String createdAt;

    @JsonProperty("title")
    public String getTitle() {
        return name;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.name = title;
    }

    @JsonProperty("contact")
    public String getContact() {
        return phoneNum;
    }

    @JsonProperty("contact")
    public void setContact(String contact) {
        this.phoneNum = contact;
    }

    @JsonProperty("categoryId")
    public Integer getCategoryId() {
        try {
            return cid == null ? null : Integer.parseInt(cid);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @JsonProperty("categoryId")
    public void setCategoryId(Integer categoryId) {
        this.cid = categoryId == null ? null : String.valueOf(categoryId);
    }
}
