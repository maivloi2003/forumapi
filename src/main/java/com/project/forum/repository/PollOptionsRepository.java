package com.project.forum.repository;

import com.project.forum.dto.responses.post.PollOptionResponse;
import com.project.forum.enity.PollOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollOptionsRepository extends JpaRepository<PollOptions, String> {
    @Query("SELECT NEW com.project.forum.dto.responses.post.PollOptionResponse( " +
            "po.id, po.option_text, COUNT(DISTINCT pv.id), " +
            "CASE WHEN pv.users.id = :userId THEN true ELSE false END ," +
            "po.created_at) " +
            "FROM poll_options po " +
            "LEFT JOIN po.pollVotes pv " +
            "LEFT JOIN po.postPoll pp " +
            "LEFT JOIN pp.posts p " +
            "WHERE pp.id = :pollId " +
            "GROUP BY po.id, po.option_text, pv.users.id")
    List<PollOptionResponse> getPollOptionsWithVotes(@Param("pollId") String pollId,
                                                     @Param("userId") String userId);



}