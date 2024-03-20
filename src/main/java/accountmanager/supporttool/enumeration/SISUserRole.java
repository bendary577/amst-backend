package accountmanager.supporttool.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum SISUserRole {
    STUDENT(1),
    STAFF(2);

    public final int roleValue;
    private static final Map<Integer, SISUserRole> ROLE_Values = new HashMap<>();

    static {
        for (SISUserRole role: values()) {
            ROLE_Values.put(role.roleValue, role);
        }
    }

    private SISUserRole(int roleValue){
        this.roleValue = roleValue;
    }

    public static SISUserRole valueOfRole(Integer value) {
        return ROLE_Values.get(value);
    }

}
