package accountmanager.supporttool.mapper;

import accountmanager.supporttool.dto.RoleDTO;
import accountmanager.supporttool.model.app.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    RoleDTO entityToDTO(Role role);

    List<RoleDTO> entityToDTO(Iterable<Role> roles);

    Role dtoToEntity(RoleDTO role);

    List<Role> dtoToEntity(Iterable<RoleDTO> roles);
}