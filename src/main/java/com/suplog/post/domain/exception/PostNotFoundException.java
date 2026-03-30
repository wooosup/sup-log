package com.suplog.post.domain.exception;

import com.suplog.global.error.SupLogException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends SupLogException {

    public PostNotFoundException() {
        super("존재하지 않는 글입니다.", HttpStatus.NOT_FOUND);
    }
}
