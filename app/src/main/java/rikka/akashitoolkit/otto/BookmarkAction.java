package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/5/14.
 */
public class BookmarkAction {
    public static class NoItem {
    }

    public static class Changed {
        private boolean bookmarked;
        private int type;

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

    public static class Changed2 {
        private boolean bookmarked;
        private int type;

        public Changed2(boolean bookmarked, int type) {
            this.bookmarked = bookmarked;
            this.type = type;
        }

        public boolean isBookmarked() {
            return bookmarked;
        }

        public void setBookmarked(boolean bookmarked) {
            this.bookmarked = bookmarked;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
