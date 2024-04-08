package accountmanager.supporttool.initializer;

import accountmanager.supporttool.service.AppUserService;
import accountmanager.supporttool.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * @author mohamed.bendary
 * this class is used to initialize the application's configured user roles and super admin user in database
 * this configured roles will be then loaded in several components in the app to be used
 */
@Component
public class ApplicationInitializer {

    private RoleService roleService;
    private AppUserService appUserService;

    @Autowired
    public ApplicationInitializer(RoleService roleService,
                                  AppUserService appUserService){
        this.roleService = roleService;
        this.appUserService = appUserService;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try{
            roleService.initApplicationRoles();
            appUserService.createSuperAdminUser();
        }catch (Exception exception){
            //TODO : HADNLE EXCEPTION PROPERLY
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }
}
