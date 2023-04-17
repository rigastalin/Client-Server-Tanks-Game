package edu.school21.servertanks.servertanks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.school21.servertanks.servertanks.models.Points;
import edu.school21.servertanks.servertanks.repositories.PointsRepository;

@Component
public class PointsServiceImpl implements PointsService {
    private PointsRepository pointsRepository;

    @Autowired
    public PointsServiceImpl(PointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    @Override
    public void createClient(int numberClient) {
        pointsRepository.saveClient(numberClient);
    }

    @Override
    public void addShot(int numberClient) {
        pointsRepository.updateShot(numberClient);
    }

    @Override
    public void addHit(int numberClient) {
        pointsRepository.updateHit(numberClient);
    }

    @Override
    public String getStatistics(int num) {
        Points info1 = pointsRepository.getInfo(num);
        Points info2 = pointsRepository.getInfo(num == 1 ? 2 : 1);

        int[] stats1 = getPlayerStats(info1);
        int[] stats2 = getPlayerStats(info2);

        return "stat:" + stats1[0] + ":" + stats1[1] + ":" + stats1[2] + ":" + stats2[0] + ":" + stats2[1] + ":" + stats2[2];
    }

    private int[] getPlayerStats(Points info) {
        int shot = info.getShot();
        int hit = info.getHit();
        int miss = calculateMissCount(shot, hit);
        return new int[]{shot, hit, miss};
    }

    private int calculateMissCount(int shot, int hit) {
        return shot - hit;
    }
}
