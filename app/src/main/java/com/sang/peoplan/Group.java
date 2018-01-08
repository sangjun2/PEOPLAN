package com.sang.peoplan;

import java.util.ArrayList;

/**
 * Created by sanginLee on 2018-01-09.
 */

public class Group {
    private String groupId;
    private String groupName;
    private ArrayList<String> groupMember;
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

    public void addMember(String uid){
        if(!groupMember.contains(uid)){
            groupMember.add(uid);
        }
    }
}
