package com.update.preference.publishPreference.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

    /* Created by suditi on 2021-08-11 */
    private Integer code;
    private String message;
    T data;

}
