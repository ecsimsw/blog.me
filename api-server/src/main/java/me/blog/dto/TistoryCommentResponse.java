package me.blog.dto;

import me.blog.domain.RecentComment;

public record TistoryCommentResponse(
    String comment,
    String link
){

    public RecentComment toRecentComment() {
        return new RecentComment(comment, link);
    }
}

