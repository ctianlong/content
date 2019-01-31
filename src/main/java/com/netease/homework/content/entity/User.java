package com.netease.homework.content.entity;

import com.netease.homework.content.config.constant.Role;

import java.io.Serializable;

/**
 * @Description 用户信息
 * @Auther ctl
 * @Date 2019/1/15
 */
public class User implements Serializable {

    private static final long serialVersionUID = -2822737249538854527L;

    private long id;
    private String username;
    private String nickname;
    private Role role;
    private String passwordMd5;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", role=" + role +
                ", passwordMd5='" + passwordMd5 + '\'' +
                '}';
    }

    public int getRoleCode() {
        return role != null ? role.getIntValue() : -1;
    }
}
