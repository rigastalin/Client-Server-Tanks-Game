package edu.school21.servertanks.servertanks.repositories;

import edu.school21.servertanks.servertanks.models.Points;

public interface PointsRepository extends CrudRepository<Points> {
    Points getInfo(int num);
}
