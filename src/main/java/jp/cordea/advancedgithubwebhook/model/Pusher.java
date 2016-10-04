package jp.cordea.advancedgithubwebhook.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yoshihiro Tanaka on 2016/10/01.
 */
@Getter
@Setter
public class Pusher {

    private String name;

    private String email;

    public boolean nameEquals(String name) {
        if (name != null && !name.isEmpty() && !this.name.isEmpty()) {
            if (name.equals(this.name)) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean emailEquals(String email) {
        if (email != null && !email.isEmpty() && !this.email.isEmpty()) {
            if (email.equals(this.email)) {
                return true;
            }
            return false;
        }
        return true;
    }

}
