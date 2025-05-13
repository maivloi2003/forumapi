package com.project.forum.controller;

import com.project.forum.dto.requests.user.CreateUserDto;
import com.project.forum.dto.requests.user.StatusRequest;
import com.project.forum.dto.requests.user.UpdateUserDto;
import com.project.forum.dto.responses.user.UserResponse;
import com.project.forum.enums.StatusUser;
import com.project.forum.exception.ApiResponse;
import com.project.forum.service.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "02. Users")
public class UserController {

    IUserService userService;


    @SecurityRequirement(name = "BearerAuth")
    @PostMapping()
    ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody(required = true) CreateUserDto createUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<UserResponse>builder()
                .data(userService.create(createUserDto))
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @PatchMapping("")
    ResponseEntity<ApiResponse<UserResponse>> update(@RequestBody(required = true) UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(userService.update(updateUserDto))
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @PatchMapping("/{id}/status")
    ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable("id") String id, @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(userService.setStatus(id, statusRequest.getStatusUser()))
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping()
    ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(ApiResponse.<Page<UserResponse>>builder()
                .data(userService.getAllUsers(username,page, size))
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(id))
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/my-info")
    ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(userService.getMyInfo())
                .build());
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/find")
    ResponseEntity<ApiResponse<Page<UserResponse>>> findUserByName(@RequestParam(defaultValue = "") String name,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(ApiResponse.<Page<UserResponse>>builder()
                .data(userService.getUserByName(name, page, size))
                .build());
    }


}
