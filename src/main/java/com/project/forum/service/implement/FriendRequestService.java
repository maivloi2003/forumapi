package com.project.forum.service.implement;

import com.project.forum.dto.requests.friend.CreateRequestFriendDto;
import com.project.forum.dto.responses.Friend.FriendRequestResponse;
import com.project.forum.dto.responses.Friend.FriendShipResponse;
import com.project.forum.enity.FriendRequest;
import com.project.forum.enity.Users;
import com.project.forum.enums.ErrorCode;
import com.project.forum.enums.StatusFriend;
import com.project.forum.exception.WebException;
import com.project.forum.repository.FriendRequestRepository;
import com.project.forum.repository.UsersRepository;
import com.project.forum.service.IFriendRequestService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendRequestService implements IFriendRequestService {

    FriendRequestRepository friendRequestRepository;

    UsersRepository usersRepository;

    @Override
    public FriendRequestResponse sendRequest(CreateRequestFriendDto createRequestFriendDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users userSend = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        Users userReceiver = usersRepository.findById(createRequestFriendDto.getReceiver()).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        if (friendRequestRepository.existsByReceiver_IdAndSender_IdAndStatus(userReceiver.getId(), userSend.getId(), StatusFriend.FRIENDS.getStatus())) {
            throw new WebException(ErrorCode.E_USERS_ARE_FRIEND);
        }
        FriendRequest friendRequest = FriendRequest.builder()
                .status(StatusFriend.PENDING.getStatus())
                .created_at(LocalDateTime.now())
                .receiver(userReceiver)
                .sender(userSend)
                .build();
        friendRequestRepository.save(friendRequest);

        return FriendRequestResponse.builder()
                .receiver(userReceiver.getName())
                .sender(userSend.getName())
                .build();
    }

    @Override
    public FriendShipResponse getFriendShip(String userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username.equals("anonymousUser")) {
            return FriendShipResponse.builder()
                    .status(StatusFriend.NONE.getStatus())
                    .build();
        }
        Users users1 = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        Users users2 = usersRepository.findById(userId).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        FriendRequest friendRequest = friendRequestRepository
                .findByReceiver_IdAndSender_Id(users1.getId(), users2.getId()).orElseThrow(() -> new WebException(ErrorCode.E_REQUEST_NOT_FOUND));
        if (Objects.isNull(friendRequest)) {
            FriendRequest friendRequest2 = friendRequestRepository
                    .findByReceiver_IdAndSender_Id(users2.getId(), users1.getId()).orElseThrow(() -> new WebException(ErrorCode.E_REQUEST_NOT_FOUND));
            return FriendShipResponse.builder()
                    .status(friendRequest2.getStatus().toString())
                    .createdAt(friendRequest2.getCreated_at())
                    .build();
        } else {
            return FriendShipResponse.builder()
                    .status(friendRequest.getStatus().toString())
                    .createdAt(friendRequest.getCreated_at())
                    .build();
        }
    }

    @Override
    public boolean acceptFriendRequest(String userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        FriendRequest  friendRequest = friendRequestRepository.findByReceiver_IdAndSender_Id(users.getId(),userId)
                .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        friendRequest.setStatus(StatusFriend.FRIENDS.getStatus());
        friendRequestRepository.save(friendRequest);
        return true;
    }

    @Override
    public boolean rejectFriendRequest(String userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        FriendRequest  friendRequest = friendRequestRepository.findByReceiver_IdAndSender_Id(users.getId(),userId)
                .orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        friendRequestRepository.delete(friendRequest);
        return true;
    }


}
