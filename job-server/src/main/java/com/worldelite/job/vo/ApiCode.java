package com.worldelite.job.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yeguozhong yedaxia.github.com
 */
public interface ApiCode {
    int SUCCESS = 0;
    int FAIL = -1;
    int INVALID_PARAM = 401;
    int NEED_LOGIN = 402;
    int PERMISSION_DENIED = 403;
    int OBJECT_NOT_FOUND = 404;
    int NOT_ACTIVATE = 405;
    int INVALID_OPERATION = 406;

    int UNCOMPLETE_RESUME = 5000; //简历未完善
}
