package com.biengual.userapi.content.domain;



import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.bookmark.BookmarkEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserContentBookmarks {
    private final List<BookmarkEntity> bookmarks;

    public UserContentBookmarks(List<BookmarkEntity> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public List<ContentInfo.UserScript> getUserScripts(List<Script> scripts) {
        Map<Long, BookmarkEntity> bookmarkMapBySentenceIndex = getBookmarkMapBySentenceIndex();

        return IntStream.range(0, scripts.size())
            .mapToObj(i -> {
                BookmarkEntity bookmark = bookmarkMapBySentenceIndex.get((long) i);
                return ContentInfo.UserScript.builder()
                    .script(scripts.get(i))
                    .isHighlighted(bookmark != null)
                    .description(bookmark != null ? bookmark.getDescription() : null)
                    .build();
            })
            .toList();
    }

    // Internal Method =================================================================================================

    private Map<Long, BookmarkEntity> getBookmarkMapBySentenceIndex() {
        return this.bookmarks.stream()
            .collect(Collectors.toMap(
                BookmarkEntity::getSentenceIndex,
                bookmark -> bookmark
            ));
    }
}
