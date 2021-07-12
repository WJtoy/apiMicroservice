package com.wolfking.jeesite.modules.api.controller;

import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RestNotFoundFilter implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RestResult handleError() {
        return RestResultGenerator.custom(ErrorCode.NOT_FOUND.code, ErrorCode.NOT_FOUND.message);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
