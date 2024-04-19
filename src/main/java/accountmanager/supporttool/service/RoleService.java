package accountmanager.supporttool.service;

import accountmanager.supporttool.dto.RoleDTO;
import accountmanager.supporttool.enumeration.UserRole;
import accountmanager.supporttool.mapper.RoleMapper;
import accountmanager.supporttool.mapper.UserMapper;
import accountmanager.supporttool.model.app.Role;
import accountmanager.supporttool.repository.RoleRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

   private final RoleRepository roleRepository;
   private RoleMapper roleMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
        this.roleMapper = Mappers.getMapper(RoleMapper.class);
    }

    public Role findByName(UserRole roleName){
        Optional<Role> role =  roleRepository.findByName(roleName);
        return role.isPresent() ? role.get() : null;
    }

    //TODO : refactor as per the application business
    public Set<Role> generatUserRolesListForNewUser(int userType){
        Set<Role> roles = new HashSet<>();
        UserRole role = UserRole.valueOfRole(userType);
        switch(role){
            case SUPER_ADMIN:
                Role superAdmin = findByName(UserRole.SUPER_ADMIN);
                roles.add(superAdmin);
                break;
            case INTERNAL_L2_SUPPORT:
                Role internalL2Support = findByName(UserRole.INTERNAL_L2_SUPPORT);
                roles.add(internalL2Support);
                break;
            case EXTERNAL_L2_SUPPORT:
                Role externalL2Support = findByName(UserRole.EXTERNAL_L2_SUPPORT);
                roles.add(externalL2Support);
                break;
            case L3_SUPPORT:
                Role l3Support = findByName(UserRole.L3_SUPPORT);
                roles.add(l3Support);
                break;
            default:
                //TODO :: handle this exception properly
                System.out.println("throw an exception");
        }
        return roles;
    }

    public void initApplicationRoles(){
        for(UserRole UserRole : UserRole.values()){
            if(findByName(UserRole)==null){
                Role role = new Role();
                role.setName(UserRole);
                this.roleRepository.save(role);
            }
        }
    }

    @Transactional
    public void newRole(){
        Role role = new Role();
        role.setName(UserRole.SUPER_ADMIN);
        Role role1 = this.roleRepository.save(role);
    }

    public List<RoleDTO> getAllRoles(){
        return this.roleMapper.entityToDTO(this.roleRepository.findAll());
    }

}
