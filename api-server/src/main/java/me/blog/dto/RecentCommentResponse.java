package me.blog.dto;

import java.util.List;
import java.util.stream.IntStream;
import me.blog.domain.RecentComment;

public record RecentCommentResponse(
    int no,
    String comment,
    String url
) {

    public static List<RecentCommentResponse> listOf(List<RecentComment> comments) {
        return IntStream.range(0, comments.size())
            .mapToObj(i -> new RecentCommentResponse(
                i + 1,
                comments.get(i).getComment(),
                comments.get(i).getLink()
            )).toList();
    }
}
