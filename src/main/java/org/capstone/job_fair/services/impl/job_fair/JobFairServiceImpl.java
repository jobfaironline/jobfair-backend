package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JobFairServiceImpl implements JobFairService {


}
