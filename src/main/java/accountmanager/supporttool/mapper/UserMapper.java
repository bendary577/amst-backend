package accountmanager.supporttool.mapper;

import accountmanager.supporttool.dto.AppUserDTO;
import accountmanager.supporttool.model.app.AppUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    AppUserDTO entityToDTO(AppUser user);

    List<AppUserDTO> entityToDTO(Iterable<AppUser> project);

    AppUser dtoToEntity(AppUserDTO project);

    List<AppUser> dtoToEntity(Iterable<AppUserDTO> projects);
}