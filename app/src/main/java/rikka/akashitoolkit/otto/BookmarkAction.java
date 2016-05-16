package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/5/14.
 */
public class BookmarkAction {
    public static class NoItem {
    }

    public static class Changed {
        private boolean bookmarked;

        public Changed(boolean bookmarked) {
            this.bookmarked = bookmarked;
        }

        public boolean isBookmarked() {
            return bookmarked;
        }

        public void setBookmarked(boolean bookmarked) {
            this.bookmarked = bookmarked;
        }
    }
}
