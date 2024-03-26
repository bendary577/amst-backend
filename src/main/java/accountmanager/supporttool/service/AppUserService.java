package accountmanager.supporttool.service;

import accountmanager.supporttool.dto.AppUserDTO;
import accountmanager.supporttool.enumeration.UserRole;
import accountmanager.supporttool.http.request.NewAppUserRequestDTO;
import accountmanager.supporttool.http.response.PageableResponse;
import accountmanager.supporttool.mapper.UserMapper;
import accountmanager.supporttool.model.app.AppUser;
import accountmanager.supporttool.model.app.Role;
import accountmanager.supporttool.repository.AppUserRepository;
import accountmanager.supporttool.util.PasswordUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment environment;

    private UserMapper userMapper;

    public AppUserService(){
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    public void createSuperAdminUser() throws Exception {
        //TODO : handle default value if this one is not configured
        String email = environment.getProperty("app.conf.superAdmin");
        Role role = roleService.findByName(UserRole.SUPER_ADMIN);
        if(role == null){
            throw new Exception();
        }
        Optional<AppUser> existingUser = appUserRepository.findByEmail(email);
        if(existingUser.isPresent()){
            return;
        }
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        AppUser user = new AppUser();
        user.setEmail(email);
        String password = PasswordUtil.generateRandomPassword();
        System.out.println("generated password " + password);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        this.appUserRepository.save(user);
    }

    public PageableResponse<AppUserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize, Sort.by(sortBy).descending());
        Page<AppUser> pagedUsers = appUserRepository.findAll(pageRequest);
        List<AppUserDTO> users = userMapper.entityToDTO(pagedUsers.getContent());
        return new PageableResponse<AppUserDTO>(pagedUsers.getTotalPages(),
                pagedUsers.getTotalElements(),
                pagedUsers.getNumberOfElements(),
                pagedUsers.getSize(),
                pagedUsers.isLast(),
                pagedUsers.isFirst(),
                "users returned successfully",
                users
        );
    }

    public AppUserDTO blockUser(String email) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if(appUserOptional.isPresent()){
            AppUser appUser = appUserOptional.get();
            if(!appUser.isBlocked()){
                appUser.setBlocked(true);
                appUser = appUserRepository.save(appUser);
            }
            return userMapper.entityToDTO(appUser);
        }
        throw new UsernameNotFoundException("");
    }

    public AppUserDTO addUser(NewAppUserRequestDTO newAppUserRequestDTO) throws RoleNotFoundException {
        String generatedPassword = PasswordUtil.generateRandomPassword();

        Role role = roleService.findByName(newAppUserRequestDTO.getUserRole());
        if(role == null){
            throw new RoleNotFoundException();
        }
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        AppUser appUser = new AppUser();
        appUser.setEmail(newAppUserRequestDTO.getEmail());
        appUser.setPassword(passwordEncoder.encode(generatedPassword));
        appUser.setRoles(roles);
        appUser.setBlocked(false);
        appUserRepository.save(appUser);
        //TODO :: event to add new email with the registered password
        return userMapper.entityToDTO(appUser);
    }


}
