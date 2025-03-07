package com.project.forum.controller;

import com.project.forum.dto.requests.friend.CreateRequestFriendDto;
import com.project.forum.dto.responses.Friend.FriendRequestResponse;
import com.project.forum.dto.responses.Friend.FriendShipResponse;
import com.project.forum.exception.ApiResponse;
import com.project.forum.service.IFriendRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/friend-request")
@Tag(name = "11. Friend Request")
public class FriendRequestController {

    private final IFriendRequestService friendRequestService;

    @PostMapping()
    public ResponseEntity<ApiResponse<FriendRequestResponse>> sendRequest(@RequestBody CreateRequestFriendDto createRequestFriendDto) {
        return ResponseEntity.ok(ApiResponse.<FriendRequestResponse>builder()
                .data(friendRequestService.sendRequest(createRequestFriendDto))
                .build());
    }

    @GetMapping("/friendship/{userId}")
    public ResponseEntity<ApiResponse<FriendShipResponse>> getFriendShip(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.<FriendShipResponse>builder()
                .data(friendRequestService.getFriendShip(userId))
                .build());
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> acceptFriendRequest(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .data(friendRequestService.acceptFriendRequest(userId))
                .build());
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> rejectFriendRequest(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .data(friendRequestService.rejectFriendRequest(userId))
                .build());
    }
}