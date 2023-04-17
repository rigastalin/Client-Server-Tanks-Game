package edu.school21.servertanks.servertanks.repositories;

import java.util.List;

public interface CrudRepository<T> {
    void saveClient(Integer numberClient);
    void updateShot(Integer numberClient);
    void updateHit(Integer numberClient);
}