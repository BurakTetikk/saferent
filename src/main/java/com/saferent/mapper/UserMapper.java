package com.saferent.mapper;

import com.saferent.dto.UserDTO;
import com.saferent.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDTO userToUserDTO(User user);

    List<UserDTO> map(List<User> userList);


}
