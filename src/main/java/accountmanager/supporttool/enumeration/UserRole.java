package accountmanager.supporttool.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum UserRole {
    SUPER_ADMIN(1),
    INTERNAL_L2_SUPPORT(2),
    EXTERNAL_L2_SUPPORT(3),
    L3_SUPPORT(4);

    public final int roleValue;
    private static final Map<Integer, UserRole> ROLE_Values = new HashMap<>();

    static {
        for (UserRole role: values()) {
            ROLE_Values.put(role.roleValue, role);
        }
    }

    private UserRole(int roleValue){
        this.roleValue = roleValue;
    }

    public static UserRole valueOfRole(Integer value) {
        return ROLE_Values.get(value);
    }

}
