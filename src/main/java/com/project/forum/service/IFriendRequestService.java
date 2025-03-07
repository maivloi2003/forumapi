package com.project.forum.service;

import com.project.forum.dto.requests.friend.CreateRequestFriendDto;
import com.project.forum.dto.responses.Friend.FriendRequestResponse;
import com.project.forum.dto.responses.Friend.FriendShipResponse;

public interface IFriendRequestService {

    FriendRequestResponse sendRequest(CreateRequestFriendDto createRequestFriendDto);

   FriendShipResponse getFriendShip(String userId);

   boolean acceptFriendRequest(String userId);

   boolean rejectFriendRequest(String userId);

}
