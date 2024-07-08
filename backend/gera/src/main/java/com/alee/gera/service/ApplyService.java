package com.alee.gera.service;

import com.alee.gera.entity.Application;
import com.alee.gera.entity.R;

public interface ApplyService {
    R addApply(Application application,String userToken);
    R dealApply(Application application,String userToken);
}
