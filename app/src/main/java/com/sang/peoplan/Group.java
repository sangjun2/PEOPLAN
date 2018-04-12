package com.sang.peoplan;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sanginLee on 2018-01-09.
 */
@SuppressWarnings("serial")
public class Group implements Serializable {
    @SerializedName("_id")
    private String _id;
    @SerializedName("administrator")
    private String administrator; // 관리자 ID
    @SerializedName("name")
    private String name; // 그룹 이름
    @SerializedName("category")
    private String category; //그룹 카테고리
    @SerializedName("state")
    private boolean state; //그룹 상태
    @SerializedName("members")
    private ArrayList<String> members; // 그룹 구성원


    //post, notice 나중으로

    public Group(String administrator, String name, String category, boolean state) {
        this.administrator = administrator;
        this.name = name;
        this.category = category;
        this.state = state;
        this.members = new ArrayList<>();
        // 관리자를 구성원으로 추가
        addMember(administrator);
    }

    public String get_id() {return _id;}

    public void set_id(String _id) {this._id = _id;}

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
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

    public void addMember(String kakaouid){
        if(!members.contains(kakaouid)){
            members.add(kakaouid);
        }
    }

}