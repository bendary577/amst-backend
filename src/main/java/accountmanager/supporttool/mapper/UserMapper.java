package accountmanager.supporttool.mapper;

import accountmanager.supporttool.dto.AppUserDTO;
import accountmanager.supporttool.model.app.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserMapper {

    @Mapping(source = "roles", target = "roles")
    AppUserDTO entityToDTO(AppUser user);

    List<AppUserDTO> entityToDTO(Iterable<AppUser> user);

    AppUser dtoToEntity(AppUserDTO userDto);

    List<AppUser> dtoToEntity(Iterable<AppUserDTO> userDTOS);
}