package jp.cordea.advancedgithubwebhook.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yoshihiro Tanaka on 2016/10/01.
 */
@Getter
@Setter
public class Payload {

    private String ref;

    private Pusher pusher;

    public boolean refEquals(String ref) {
        if (ref != null && !ref.isEmpty() && !this.ref.isEmpty()) {
            if (!ref.contains("*")) {
                return this.ref.equals(ref);
            }
            ref = ref.replace("*", ".+");
            return this.ref.matches(ref);
        }
        return true;
    }

}
