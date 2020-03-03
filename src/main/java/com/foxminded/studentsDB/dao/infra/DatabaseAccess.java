package com.foxminded.studentsDB.dao.infra;

public class DatabaseAccess {
    private String url;
    private String user;
    private String password;

    public DatabaseAccess(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseAccess)) return false;

        DatabaseAccess that = (DatabaseAccess) o;

        if (!url.equals(that.url)) return false;
        if (!user.equals(that.user)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
