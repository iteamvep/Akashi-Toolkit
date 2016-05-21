package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/5/14.
 */
public class BookmarkAction {
    public static class NoItem {
    }

    public static class Changed {
        private String tag;
        private boolean bookmarked;

        public Changed(String tag, boolean bookmarked) {
            this.tag = tag;
            this.bookmarked = bookmarked;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isBookmarked() {
            return bookmarked;
        }

        public void setBookmarked(boolean bookmarked) {
            this.bookmarked = bookmarked;
        }
    }

    public static class Changed2 {
        private String tag;
        private boolean bookmarked;
        private int type;

        public Changed2(String tag, boolean bookmarked, int type) {
            this.tag = tag;
            this.bookmarked = bookmarked;
            this.type = type;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
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
