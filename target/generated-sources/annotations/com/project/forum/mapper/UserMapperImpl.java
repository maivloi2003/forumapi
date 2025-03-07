package com.project.forum.mapper;

import com.project.forum.dto.requests.user.CreateUserDto;
import com.project.forum.dto.requests.user.UpdateUserDto;
import com.project.forum.dto.responses.user.UserResponse;
import com.project.forum.enity.Users;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-07T16:27:48+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Users toUsers(CreateUserDto createUserDto) {
        if ( createUserDto == null ) {
            return null;
        }

        Users.UsersBuilder users = Users.builder();

        users.name( createUserDto.getName() );
        users.language( createUserDto.getLanguage() );
        users.gender( createUserDto.getGender() );
        users.img( createUserDto.getImg() );
        users.email( createUserDto.getEmail() );
        users.username( createUserDto.getUsername() );
        users.password( createUserDto.getPassword() );

        return users.build();
    }

    @Override
    public UserResponse toUserResponse(Users users) {
        if ( users == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.name( users.getName() );
        userResponse.id( users.getId() );
        userResponse.language( users.getLanguage() );
        userResponse.gender( users.getGender() );
        userResponse.img( users.getImg() );
        userResponse.email( users.getEmail() );
        userResponse.username( users.getUsername() );

        return userResponse.build();
    }

    @Override
    public Users toUpdate(Users users, UpdateUserDto updateUserDto) {
        if ( updateUserDto == null ) {
            return users;
        }

        users.setName( updateUserDto.getName() );
        users.setLanguage( updateUserDto.getLanguage() );
        users.setGender( updateUserDto.getGender() );

        return users;
    }
}
