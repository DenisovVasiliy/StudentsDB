package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.dao.exceptions.DAOException;

public interface Listener {
    public void listen() throws DAOException;

    public int getCounter();

    public int getCourseNumber(int limit);
}
