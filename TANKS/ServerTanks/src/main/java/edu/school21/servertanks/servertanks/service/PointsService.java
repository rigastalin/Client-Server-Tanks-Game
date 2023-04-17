package edu.school21.servertanks.servertanks.service;

public interface PointsService {
    void createClient(int numberClient);
    void addShot(int numberClient);
    void addHit(int numberClient);
    String getStatistics(int num);
}