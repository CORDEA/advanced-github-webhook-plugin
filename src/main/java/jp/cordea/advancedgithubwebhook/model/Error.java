package jp.cordea.advancedgithubwebhook.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Created by Yoshihiro Tanaka on 2016/10/03.
 */
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    private int code;

    private String message;

}
