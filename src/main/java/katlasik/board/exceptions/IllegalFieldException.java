package katlasik.board.exceptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IllegalFieldException extends RuntimeException {

    public IllegalFieldException() {
    }

}

