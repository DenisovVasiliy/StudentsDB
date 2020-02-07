package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.dao.DAOException;

public interface Listener {
    public void listen() throws DAOException;
    public int getCounter();
}
