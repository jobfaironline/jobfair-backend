package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CvController {

    @Autowired
    CvService cvService;
}
