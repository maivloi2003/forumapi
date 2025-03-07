package com.project.forum.service.implement;

import com.project.forum.dto.responses.notices.NoticeResponse;
import com.project.forum.enity.Notices;
import com.project.forum.enity.Users;
import com.project.forum.enums.ErrorCode;
import com.project.forum.exception.WebException;
import com.project.forum.repository.NoticesRepository;
import com.project.forum.repository.UsersRepository;
import com.project.forum.service.INoticeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoticeService implements INoticeService {

     final SimpMessagingTemplate messagingTemplate;
     final NoticesRepository noticesRepository;
     final UsersRepository usersRepository;

     @Transactional
    public void sendNotification(Users users, String type, String message, String postId) {

        if (noticesRepository.existsNoticesByTypeAndPost_idAndUser_id(type, postId, users.getId())) {
            noticesRepository.updateNoticeMessage(type,postId,users.getId(),message);
            String destination = "/user/" + users.getId() + "/queue/notifications";
            messagingTemplate.convertAndSend(destination, message);
        } else {
            Notices notice = Notices.builder()
                    .users(users)
                    .post_id(postId)
                    .type(type)
                    .message(message)
                    .status(false)
                    .build();
            noticesRepository.save(notice);

            String destination = "/user/" + users.getId() + "/queue/notifications";
            messagingTemplate.convertAndSend(destination, notice.getMessage());
        }




    }

    @Override
    public Page<NoticeResponse> getNotices(Pageable pageable) {
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
         Users users = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
         Page<NoticeResponse> noticeResponses = noticesRepository.getAllNoticesByUserId(users.getId(), pageable);

         return noticeResponses;
    }

    @Override
    public Integer getNumberNoRead() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));


        return noticesRepository.countNoticesByStatusAndUsers_Id(false,users.getId());
    }

    @Override
    public boolean readNotices() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new WebException(ErrorCode.E_USER_NOT_FOUND));
        noticesRepository.updateNoticesStatusByUserId(users.getId());
        return true;
    }

}
