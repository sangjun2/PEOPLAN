package com.sang.peoplan.group_pack;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sanginLee on 2018-01-09.
 */

@SuppressWarnings("serial")

public class Group implements Serializable {
    @SerializedName("_id")
    private String _id; // Group _id
    @SerializedName("owner")
    private String owner; // 관리자 ID
    @SerializedName("name")
    private String name; // 그룹 이름
    @SerializedName("category")
    private String category; //그룹 카테고리
    @SerializedName("state")
    private boolean state; //그룹 상태
    @SerializedName("members")
    private ArrayList<String> members; // 그룹 구성원
    @SerializedName("img")
    private String img; // 카테고리별, 스토리지 서버의 img url, NULL이면 기본이미지
    @SerializedName("posts")
    private ArrayList<String> posts;
    @SerializedName("notices")
    private ArrayList<String> notices;
    @SerializedName("waitinglist")
    private ArrayList<String> waitinglist; // 대기열 user._id

    public Group(String owner, String name, String category, boolean state, String img) {
        this.owner = owner;
        this.name = name;
        this.category = category;
        this.state = state;
        this.img = null;
        // this.notices = new ArrayList<>();
        this.members = new ArrayList<>();
        // 관리자를 구성원으로 추가
        addMember(owner);
        /*
        notices.add(notice)
        */
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void addMember(String kakaouid) {
        if (!members.contains(kakaouid)) {
            members.add(kakaouid);
        }
    }

    public void setImg( String img){ this.img = img;}
    public String getImg(){ return this.img; }
}