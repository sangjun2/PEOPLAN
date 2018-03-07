package com.sang.peoplan;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sanginLee on 2018-01-09.
 */
@SuppressWarnings("serial")
public class Group implements Serializable {
    @SerializedName("groupId")
    private String groupId; // 그룹 id, 어떻게 제작?
    @SerializedName("groupName")
    private String groupName; // 그룹 이름
    @SerializedName("groupMember")
    private ArrayList<String> groupMember; // 그룹 구성원
    //post, notice, event, category 생략

    public Group(String groupId, String groupName){
        this.groupId = groupId;
        this.groupName = groupName;
        groupMember = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(ArrayList<String> groupMember) {
        this.groupMember = groupMember;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setSetGroupId(String GroupId) {
        this.groupId = GroupId;
    }

    public void addMember(String kakaouid){
        if(!groupMember.contains(kakaouid)){
            groupMember.add(kakaouid);
        }
    }
}