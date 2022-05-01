package org.capstone.job_fair.services.interfaces.dynamoDB;

public interface JobFairVisitService {
    void visitJobFair(String userId, String jobFairId);

    void leaveJobFair(String userId, String jobFairId);

    void visitBooth(String userId, String jobFairBoothId);

    void leaveBooth(String userId, String jobFairBoothId);

    int getCurrentVisitOfJobFair(String jobFairId);

    int getCurrentVisitOfJobFairBooth(String jobFairBootId);
}
