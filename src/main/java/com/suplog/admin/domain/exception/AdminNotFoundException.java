package com.suplog.admin.domain.exception;

import com.suplog.global.error.SupLogException;
import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends SupLogException {

    public AdminNotFoundException() {
        super("관리자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
